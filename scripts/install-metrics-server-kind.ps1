[CmdletBinding()]
param(
    [string]$Kubeconfig = (Join-Path $PSScriptRoot "..\infra\cluster\kubeconfig"),
    [string]$MetricsServerVersion = "v0.9.0",
    [int]$TimeoutSeconds = 180
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command kubectl -ErrorAction SilentlyContinue)) {
    throw "kubectl não foi encontrado no PATH."
}

$kubeconfigPath = (Resolve-Path -LiteralPath $Kubeconfig).Path
$patchPath = (Resolve-Path -LiteralPath (
    Join-Path $PSScriptRoot "metrics-server-kind-patch.yaml"
)).Path
$manifestUrl = "https://github.com/kubernetes-sigs/metrics-server/releases/download/$MetricsServerVersion/components.yaml"

function Invoke-Kubectl {
    & kubectl --kubeconfig $kubeconfigPath @args
    if ($LASTEXITCODE -ne 0) {
        throw "kubectl falhou: $($args -join ' ')"
    }
}

Write-Host "Instalando Metrics Server $MetricsServerVersion..."
Invoke-Kubectl apply -f $manifestUrl

Write-Host "Aplicando configuração compatível com o cluster local Kind..."
Invoke-Kubectl patch deployment metrics-server `
    -n kube-system `
    --type strategic `
    --patch-file $patchPath

Write-Host "Aguardando o Deployment ficar disponível..."
Invoke-Kubectl rollout status deployment/metrics-server `
    -n kube-system `
    "--timeout=${TimeoutSeconds}s"

$deadline = (Get-Date).AddSeconds($TimeoutSeconds)
$available = ""

do {
    $apiServiceJson = & kubectl --kubeconfig $kubeconfigPath get apiservice v1beta1.metrics.k8s.io `
        -o json 2>$null

    if ($LASTEXITCODE -eq 0) {
        $apiService = $apiServiceJson | ConvertFrom-Json
        $available = ($apiService.status.conditions |
            Where-Object type -eq "Available" |
            Select-Object -First 1).status

        if ($available -eq "True") {
            break
        }
    }

    Start-Sleep -Seconds 5
} while ((Get-Date) -lt $deadline)

if ($available -ne "True") {
    & kubectl --kubeconfig $kubeconfigPath logs -n kube-system deployment/metrics-server --tail=40
    throw "A Metrics API não ficou disponível em ${TimeoutSeconds}s."
}

Write-Host "Metrics API disponível. Uso atual dos nós:"
Invoke-Kubectl top nodes

Write-Host "Estado atual dos HPAs:"
Invoke-Kubectl get hpa -A
