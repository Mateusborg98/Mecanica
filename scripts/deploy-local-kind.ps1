[CmdletBinding()]
param(
    [string]$ClusterName = "mecanica-local",
    [string]$Namespace = "mecanica",
    [string]$AppImage = "mecanica-api:latest",
    [string]$LoadImage = "busybox:1.36",
    [int]$TimeoutSeconds = 360,
    [int]$LocalPort = 8082,
    [switch]$RecreateCluster,
    [switch]$SkipBuild,
    [switch]$PreloadLoadImage,
    [switch]$StartPortForward
)

$ErrorActionPreference = "Stop"

$repositoryRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$clusterTerraformDirectory = Join-Path $repositoryRoot "infra\cluster"
$kubeconfigPath = Join-Path $clusterTerraformDirectory "kubeconfig"
$kubernetesDirectory = Join-Path $repositoryRoot "k8s"
$metricsServerScript = Join-Path $PSScriptRoot "install-metrics-server-kind.ps1"

function Write-Step {
    param([string]$Message)

    Write-Host ""
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Assert-LastExitCode {
    param([string]$Operation)

    if ($LASTEXITCODE -ne 0) {
        throw "$Operation falhou com código de saída $LASTEXITCODE."
    }
}

function Assert-Command {
    param([string]$Command)

    if (-not (Get-Command $Command -ErrorAction SilentlyContinue)) {
        throw "O comando '$Command' não foi encontrado no PATH."
    }
}

function Get-KindClusters {
    $clusters = & kind get clusters 2>$null

    if ($LASTEXITCODE -ne 0) {
        return @()
    }

    return @($clusters | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })
}

function Export-KindKubeconfig {
    param(
        [string]$Name,
        [string]$Destination
    )

    $content = & kind get kubeconfig --name $Name
    Assert-LastExitCode "Exportacao do kubeconfig do Kind"

    $utf8WithoutBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText(
        $Destination,
        ($content -join [Environment]::NewLine),
        $utf8WithoutBom
    )
}

Write-Step "Validando pre-requisitos"

@("docker", "terraform", "kind", "kubectl") | ForEach-Object {
    Assert-Command $_
}

& docker info *> $null
Assert-LastExitCode "Validacao do Docker"

if (-not $SkipBuild) {
    Write-Step "Construindo a imagem $AppImage"
    & docker build -t $AppImage $repositoryRoot
    Assert-LastExitCode "Build da imagem da aplicacaoo"
}
else {
    Write-Step "Build da aplicacao ignorado por solicitacao"
    & docker image inspect $AppImage *> $null
    Assert-LastExitCode "Validacao da imagem $AppImage"
}

if ($PreloadLoadImage) {
    & docker image inspect $LoadImage *> $null

    if ($LASTEXITCODE -ne 0) {
        Write-Step "Baixando a imagem auxiliar $LoadImage"
        & docker pull $LoadImage
        Assert-LastExitCode "Download da imagem auxiliar"
    }
}

$clusterExists = (Get-KindClusters) -contains $ClusterName

if ($clusterExists -and $RecreateCluster) {
    Write-Step "Removendo o cluster existente $ClusterName"
    & kind delete cluster --name $ClusterName
    Assert-LastExitCode "Remocao do cluster Kind"
    $clusterExists = $false
}

if (-not $clusterExists) {
    Write-Step "Inicializando o Terraform do cluster"
    & terraform "-chdir=$clusterTerraformDirectory" init -input=false
    Assert-LastExitCode "Inicializacao do Terraform"

    $terraformResources = & terraform "-chdir=$clusterTerraformDirectory" state list 2>$null

    if ($LASTEXITCODE -eq 0 -and $terraformResources -contains "kind_cluster.mecanica") {
        Write-Step "Removendo do state a referência ao cluster Kind que não existe mais"
        & terraform "-chdir=$clusterTerraformDirectory" state rm kind_cluster.mecanica
        Assert-LastExitCode "Limpeza da referência órfã no state do Terraform"
    }

    Write-Step "Criando o cluster Kind $ClusterName com Terraform"
    & terraform "-chdir=$clusterTerraformDirectory" apply `
        -auto-approve `
        -input=false `
        "-var=cluster_name=$ClusterName"
    Assert-LastExitCode "Criacao do cluster Kind"
}
else {
    Write-Step "Reutilizando o cluster Kind existente $ClusterName"
    Export-KindKubeconfig -Name $ClusterName -Destination $kubeconfigPath
}

if (-not (Test-Path -LiteralPath $kubeconfigPath)) {
    Export-KindKubeconfig -Name $ClusterName -Destination $kubeconfigPath
}

$env:KUBECONFIG = (Resolve-Path -LiteralPath $kubeconfigPath).Path

Write-Step "Validando o acesso ao cluster"
& kubectl cluster-info
Assert-LastExitCode "Acesso ao cluster Kubernetes"

Write-Step "Carregando as imagens no Kind"
& kind load docker-image $AppImage --name $ClusterName
Assert-LastExitCode "Carregamento da imagem da aplicacao"

if ($PreloadLoadImage) {
    & kind load docker-image $LoadImage --name $ClusterName

    if ($LASTEXITCODE -ne 0) {
        Write-Warning "Não foi possível pré-carregar $LoadImage no Kind. O deploy continuará; o Kubernetes tentará baixar essa imagem quando o gerador de carga for criado."
    }
}

Write-Step "Instalando e validando o Metrics Server"
& $metricsServerScript `
    -Kubeconfig $env:KUBECONFIG `
    -TimeoutSeconds ([Math]::Min($TimeoutSeconds, 300))
Assert-LastExitCode "Instalacao do Metrics Server"

Write-Step "Aplicando os manifestos Kubernetes"
& kubectl apply -f (Join-Path $kubernetesDirectory "namespace.yaml")
Assert-LastExitCode "Criacao do namespace"

& kubectl apply -f $kubernetesDirectory
Assert-LastExitCode "Aplicacao dos manifestos Kubernetes"

Write-Step "Reiniciando a API para utilizar a imagem carregada mais recente"
& kubectl rollout restart deployment/mecanica-api -n $Namespace
Assert-LastExitCode "Reinicio do Deployment da API"

Write-Step "Aguardando o PostgreSQL"
& kubectl rollout status deployment/mecanica-db `
    -n $Namespace `
    "--timeout=${TimeoutSeconds}s"
Assert-LastExitCode "Inicializacao do PostgreSQL"

Write-Step "Aguardando a API"
& kubectl rollout status deployment/mecanica-api `
    -n $Namespace `
    "--timeout=${TimeoutSeconds}s"
Assert-LastExitCode "Inicializacao da API"

Write-Step "Validando recursos e métricas"
& kubectl get deployments,services,pods,hpa,pvc -n $Namespace
Assert-LastExitCode "Consulta dos recursos Kubernetes"

& kubectl top nodes
Assert-LastExitCode "Consulta das metricas dos nos"

& kubectl top pods -n $Namespace
Assert-LastExitCode "Consulta das metricas dos pods"

& kubectl get hpa -n $Namespace
Assert-LastExitCode "Consulta do HPA"

Write-Host ""
Write-Host "Deploy concluído com sucesso." -ForegroundColor Green
Write-Host "Kubeconfig: $env:KUBECONFIG"
Write-Host "Swagger após o port-forward: http://localhost:${LocalPort}/swagger-ui/index.html"

if ($StartPortForward) {
    Write-Step "Iniciando port-forward; pressione Ctrl+C para encerrar"
    & kubectl port-forward `
        service/mecanica-api `
        "${LocalPort}:8080" `
        -n $Namespace
}
else {
    Write-Host ""
    Write-Host "Para acessar a aplicacao, execute:"
    Write-Host "kubectl port-forward service/mecanica-api ${LocalPort}:8080 -n $Namespace"
}
