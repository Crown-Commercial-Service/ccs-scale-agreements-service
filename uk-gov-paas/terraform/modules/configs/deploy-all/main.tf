#########################################################
# Config: deploy-all
#
# This configuration will deploy all components.
#########################################################

module "agreements-service" {
  source = "../../agreements-service"
}

module "logit-ups" {
  source                    = "../../logit-ups"
  logit_service_broker_name = "logit-ssl-drain"
  organisation              = var.organisation
  space                     = var.space
  syslog_drain_url          = var.syslog_drain_url
}
