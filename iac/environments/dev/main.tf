#########################################################
# Environment: DEV
#
# Deploy CaT resources
#########################################################
module "deploy-all" {
  source      = "../../modules/configs/deploy-all"
  space       = "develop"
  environment = "dev"
  cf_username = var.cf_username
  cf_password = var.cf_password
}
