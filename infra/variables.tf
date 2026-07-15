variable "kubeconfig_path" {
  description = "Caminho do arquivo kubeconfig usado para acessar o cluster Kubernetes local."
  type        = string
  default     = "~/.kube/config"
}

variable "kube_context" {
  description = "Contexto Kubernetes usado pelo Terraform."
  type        = string
  default     = "docker-desktop"
}

variable "namespace" {
  description = "Namespace onde os recursos da aplicação serão criados."
  type        = string
  default     = "mecanica-tf"
}

variable "app_image" {
  description = "Imagem Docker da API Mecânica usada no Deployment Kubernetes."
  type        = string
  default     = "mecanica-api:latest"
}
