provider "kind" {}

resource "kind_cluster" "mecanica" {
  name            = var.cluster_name
  kubeconfig_path = abspath(var.kubeconfig_path)
  wait_for_ready  = true
}
