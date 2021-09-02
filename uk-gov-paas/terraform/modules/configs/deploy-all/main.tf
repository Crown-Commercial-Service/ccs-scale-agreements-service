#########################################################
# Config: deploy-all
#
# This configuration will deploy all components.
#########################################################

module "agreements-service" {
  source       = "../../agreements-service"
  organisation = var.organisation
  space        = var.space
  environment  = var.environment
  cf_username  = var.cf_username
  cf_password  = var.cf_password
}
