#########################################################
# Environment: SBX
#
# Deploy CaT resources
#########################################################

module "deploy-all" {
  source           = "../../modules/configs/deploy-all"
  syslog_drain_url = "https://44f18302-59ca-4034-a82e-63f742e60a3e-ls.logit.io:12732"
}
