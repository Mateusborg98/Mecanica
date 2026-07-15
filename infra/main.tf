resource "kubernetes_namespace" "mecanica" {
  metadata {
    name = var.namespace
  }
}

resource "kubernetes_secret" "postgres" {
  metadata {
    name      = "mecanica-db-secret"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  type = "Opaque"

  data = {
    POSTGRES_DB       = "mecanica"
    POSTGRES_USER     = "postgres"
    POSTGRES_PASSWORD = "admin"
  }
}

resource "kubernetes_secret" "api" {
  metadata {
    name      = "mecanica-api-secret"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  type = "Opaque"

  data = {
    SPRING_DATASOURCE_USERNAME = "postgres"
    SPRING_DATASOURCE_PASSWORD = "admin"
    APP_JWT_SECRET             = "tech-challenge-fase-2-jwt-secret-demo"
    APP_AUTH_USERNAME          = "admin"
    APP_AUTH_PASSWORD          = "123"
  }
}

resource "kubernetes_config_map" "api" {
  metadata {
    name      = "mecanica-api-config"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  data = {
    SPRING_PROFILES_ACTIVE        = "docker"
    SPRING_DATASOURCE_URL         = "jdbc:postgresql://mecanica-db:5432/mecanica"
    SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
  }
}

resource "kubernetes_persistent_volume_claim_v1" "postgres" {
  wait_until_bound = false

  metadata {
    name      = "mecanica-db-data"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  spec {
    access_modes = ["ReadWriteOnce"]

    resources {
      requests = {
        storage = "1Gi"
      }
    }
  }
}

resource "kubernetes_deployment" "postgres" {
  metadata {
    name      = "mecanica-db"
    namespace = kubernetes_namespace.mecanica.metadata[0].name

    labels = {
      app = "mecanica-db"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "mecanica-db"
      }
    }

    template {
      metadata {
        labels = {
          app = "mecanica-db"
        }
      }

      spec {
        container {
          name  = "postgres"
          image = "postgres:16"

          port {
            container_port = 5432
          }

          volume_mount {
            name       = "postgres-data"
            mount_path = "/var/lib/postgresql/data"
          }

          env {
            name = "POSTGRES_DB"

            value_from {
              secret_key_ref {
                name = kubernetes_secret.postgres.metadata[0].name
                key  = "POSTGRES_DB"
              }
            }
          }

          env {
            name = "POSTGRES_USER"

            value_from {
              secret_key_ref {
                name = kubernetes_secret.postgres.metadata[0].name
                key  = "POSTGRES_USER"
              }
            }
          }

          env {
            name = "POSTGRES_PASSWORD"

            value_from {
              secret_key_ref {
                name = kubernetes_secret.postgres.metadata[0].name
                key  = "POSTGRES_PASSWORD"
              }
            }
          }

          readiness_probe {
            exec {
              command = ["pg_isready", "-U", "postgres"]
            }

            initial_delay_seconds = 10
            period_seconds        = 5
          }

          liveness_probe {
            exec {
              command = ["pg_isready", "-U", "postgres"]
            }

            initial_delay_seconds = 30
            period_seconds        = 10
          }
        }

        volume {
          name = "postgres-data"

          persistent_volume_claim {
            claim_name = kubernetes_persistent_volume_claim_v1.postgres.metadata[0].name
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "postgres" {
  metadata {
    name      = "mecanica-db"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  spec {
    selector = {
      app = "mecanica-db"
    }

    port {
      name        = "postgres"
      port        = 5432
      target_port = 5432
    }

    type = "ClusterIP"
  }
}

resource "kubernetes_deployment" "api" {
  metadata {
    name      = "mecanica-api"
    namespace = kubernetes_namespace.mecanica.metadata[0].name

    labels = {
      app = "mecanica-api"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "mecanica-api"
      }
    }

    template {
      metadata {
        labels = {
          app = "mecanica-api"
        }
      }

      spec {
        container {
          name              = "mecanica-api"
          image             = var.app_image
          image_pull_policy = "IfNotPresent"

          port {
            container_port = 8080
          }

          env_from {
            config_map_ref {
              name = kubernetes_config_map.api.metadata[0].name
            }
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.api.metadata[0].name
            }
          }

          resources {
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }

            limits = {
              cpu    = "500m"
              memory = "768Mi"
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }

            initial_delay_seconds = 120
            period_seconds        = 10
            timeout_seconds       = 5
            failure_threshold     = 12
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }

            initial_delay_seconds = 150
            period_seconds        = 20
            timeout_seconds       = 5
            failure_threshold     = 3
          }
        }
      }
    }
  }

  depends_on = [
    kubernetes_deployment.postgres,
    kubernetes_service.postgres
  ]
}

resource "kubernetes_service" "api" {
  metadata {
    name      = "mecanica-api"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  spec {
    selector = {
      app = "mecanica-api"
    }

    port {
      name        = "http"
      port        = 8080
      target_port = 8080
    }

    type = "ClusterIP"
  }
}

resource "kubernetes_horizontal_pod_autoscaler_v2" "api" {
  metadata {
    name      = "mecanica-api-hpa"
    namespace = kubernetes_namespace.mecanica.metadata[0].name
  }

  spec {
    min_replicas = 1
    max_replicas = 3

    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = kubernetes_deployment.api.metadata[0].name
    }

    behavior {
      scale_up {
        stabilization_window_seconds = 0

        policy {
          type           = "Pods"
          value          = 2
          period_seconds = 15
        }
      }

      scale_down {
        stabilization_window_seconds = 60

        policy {
          type           = "Pods"
          value          = 1
          period_seconds = 30
        }
      }
    }

    metric {
      type = "Resource"

      resource {
        name = "cpu"

        target {
          type                = "Utilization"
          average_utilization = 70
        }
      }
    }

    metric {
      type = "Resource"

      resource {
        name = "memory"

        target {
          type          = "AverageValue"
          average_value = "650Mi"
        }
      }
    }
  }
}
