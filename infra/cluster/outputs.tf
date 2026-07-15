output "cluster_name" {
  description = "Nome do cluster local criado pelo Terraform."
  value       = kind_cluster.mecanica.name
}

output "kubeconfig_path" {
  description = "Caminho absoluto do kubeconfig gerado."
  value       = abspath(var.kubeconfig_path)
}

output "kube_context" {
  description = "Contexto kubectl correspondente ao cluster Kind."
  value       = "kind-${kind_cluster.mecanica.name}"
}
