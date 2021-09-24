#########################################################
# Environment: REF
#
# Deploy CaT resources
#########################################################
module "deploy-all" {
  source      = "../../modules/configs/deploy-all"
  space       = "ref"
  environment = "ref"
  cf_username = var.cf_username
  cf_password = var.cf_password
}
