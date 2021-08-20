data "cloudfoundry_org" "ccs_conclave_cii" {
  name = var.organisation
}

data "cloudfoundry_space" "cloudfoundry_space" {
  name = var.space
  org  = data.cloudfoundry_org.ccs_conclave_cii.id
}

resource "cloudfoundry_user_provided_service" "logit_service_broker_service" {
  name             = var.logit_service_broker_name
  space            = data.cloudfoundry_space.cloudfoundry_space.id
  syslog_drain_url = var.syslog_drain_url
}
