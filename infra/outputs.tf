output "namespace" {
  description = "Namespace criado pelo Terraform."
  value       = kubernetes_namespace.mecanica.metadata[0].name
}

output "api_configmap_name" {
  description = "Nome do ConfigMap da API."
  value       = kubernetes_config_map.api.metadata[0].name
}

output "api_secret_name" {
  description = "Nome do Secret da API."
  value       = kubernetes_secret.api.metadata[0].name
}

output "postgres_secret_name" {
  description = "Nome do Secret do PostgreSQL."
  value       = kubernetes_secret.postgres.metadata[0].name
}

output "postgres_service_name" {
  description = "Nome do Service interno do PostgreSQL."
  value       = kubernetes_service.postgres.metadata[0].name
}

output "postgres_deployment_name" {
  description = "Nome do Deployment do PostgreSQL."
  value       = kubernetes_deployment.postgres.metadata[0].name
}

output "postgres_pvc_name" {
  description = "Nome do volume persistente do PostgreSQL."
  value       = kubernetes_persistent_volume_claim_v1.postgres.metadata[0].name
}

output "api_service_name" {
  description = "Nome do Service interno da API."
  value       = kubernetes_service.api.metadata[0].name
}

output "api_deployment_name" {
  description = "Nome do Deployment da API."
  value       = kubernetes_deployment.api.metadata[0].name
}

output "api_hpa_name" {
  description = "Nome do HPA da API."
  value       = kubernetes_horizontal_pod_autoscaler_v2.api.metadata[0].name
}
