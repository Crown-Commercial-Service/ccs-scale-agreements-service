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


resource "cloudfoundry_app" "agreements_service" {
  annotations = {}
  buildpack   = var.buildpack
  disk_quota  = var.disk_quota
  enable_ssh  = true
  environment = {
    #GOPACKAGENAME: var.go_package_name
    #GOVERSION: var.go_version
    #SECURITY_USER_NAME: var.security_user_name
    #SECURITY_USER_PASSWORD: var.security_user_password
    #VAULT_ADDR: var.vault_addr
    #VAULT_TOKEN: var.vault_token
  }
  health_check_timeout = var.healthcheck_timeout
  health_check_type    = "port"
  instances            = var.instances
  labels               = {}
  memory               = var.memory
  name                 = var.app_name
  path                 = var.path
  ports                = [8080]
  space                = data.cloudfoundry_space.space.id
  stopped              = false
  timeout              = 600
}

resource "cloudfoundry_route" "agreements_service" {
  domain    = data.cloudfoundry_domain.domain.id
  space     = data.cloudfoundry_space.space.id
  #hostname  = var.cloudfoundry_route

  target {
    app = cloudfoundry_app.agreements_service.id
    port = 8080
  }
}
