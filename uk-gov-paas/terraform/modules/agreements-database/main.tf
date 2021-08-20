data "cloudfoundry_org" "ccs_shared_services" {
  name = var.organisation
}

data "cloudfoundry_space" "space" {
  name = var.space
  org  = data.cloudfoundry_org.ccs_shared_services.id
}

data "cloudfoundry_service" "postgres" {
  name = var.postgres_service
}

resource "cloudfoundry_service_instance" "postgres" {
  name         = var.postgres_instance_name
  space        = data.cloudfoundry_space.space.id
  service_plan = data.cloudfoundry_service.postgres.service_plans[var.postgres_service_plan]
}
