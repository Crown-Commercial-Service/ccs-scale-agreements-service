#########################################################
# Environment: SBX1
#
# Deploy CaT resources
#########################################################
module "deploy-all" {
  source                      = "../../modules/configs/deploy-all"
  space                       = "sandbox"
  environment                 = "sbx1"
  cf_username                 = var.cf_username
  cf_password                 = var.cf_password
  agreement_service_instances = 1
}
