data "cloudfoundry_org" "ccs_shared_services" {
  name = var.organisation
}

data "cloudfoundry_space" "space" {
  name = var.space
  org  = data.cloudfoundry_org.ccs_shared_services.id
}

data "cloudfoundry_domain" "domain" {
  name = "london.cloudapps.digital"
}

data "cloudfoundry_service_instance" "agreements_database" {
  name_or_id = "${var.environment}-ccs-scale-shared-agreements-pg-db"
  space      = data.cloudfoundry_space.space.id
}

data "cloudfoundry_user_provided_service" "logit" {
  name  = "${var.environment}-ccs-scale-shared-logit-ssl-drain"
  space = data.cloudfoundry_space.space.id
}

data "cloudfoundry_user_provided_service" "ip_router" {
  name  = "${var.environment}-ccs-scale-shared-ip-router"
  space = data.cloudfoundry_space.space.id
}

resource "cloudfoundry_app" "agreements_service" {
  annotations = {}
  buildpack   = var.buildpack
  disk_quota  = var.disk_quota
  enable_ssh  = true
  environment = {
    #ENV_VAR: var.variable_name
  }
  health_check_timeout = var.healthcheck_timeout
  health_check_type    = "port"
  instances            = var.instances
  labels               = {}
  memory               = var.memory
  name                 = "${var.environment}-ccs-scale-shared-agreements-service"
  path                 = var.path
  source_code_hash     = filebase64sha256(var.path)
  ports                = [8080]
  space                = data.cloudfoundry_space.space.id
  stopped              = false
  timeout              = 600

  service_binding {
    service_instance = data.cloudfoundry_service_instance.agreements_database.id
  }

  service_binding {
    service_instance = data.cloudfoundry_user_provided_service.logit.id
  }
}

resource "cloudfoundry_route" "agreements_service" {
  domain   = data.cloudfoundry_domain.domain.id
  space    = data.cloudfoundry_space.space.id
  hostname = "${var.environment}-ccs-scale-shared-agreements-service"

  target {
    app  = cloudfoundry_app.agreements_service.id
    port = 8080
  }
}

# Bind to nginx IP Router UPS
resource "cloudfoundry_route_service_binding" "agreements-service" {
  service_instance = data.cloudfoundry_user_provided_service.ip_router.id
  route            = cloudfoundry_route.agreements_service.id
}
