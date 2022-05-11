#########################################################
# Environment: Pre-production
#
# Deploy CaT resources
#########################################################
module "deploy-all" {
  source                   = "../../modules/configs/deploy-all"
  space                    = "pre-production"
  environment              = "pre"
  cf_username              = var.cf_username
  cf_password              = var.cf_password
  agreement_service_memory = 2048
}
