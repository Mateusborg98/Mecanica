variable "cluster_name" {
  description = "Nome do cluster Kubernetes local criado com Kind."
  type        = string
  default     = "mecanica-local"
}

variable "kubeconfig_path" {
  description = "Arquivo kubeconfig gerado para acesso ao cluster local."
  type        = string
  default     = "./kubeconfig"
}
