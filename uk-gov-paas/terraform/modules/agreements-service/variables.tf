variable "organisation" {}

variable "space" {}

variable "environment" {}

variable "buildpack" {
  default = "https://github.com/cloudfoundry/java-buildpack#v4.33"
}

variable "disk_quota" {
  default = 2048
}

variable "healthcheck_timeout" {
  default = 0
}

variable "instances" {
  default = 1
}

variable "memory" {
  default = 1024
}

variable "path" {
  default = "../../../../target/ccs-scale-agreements-service-0.0.1-SNAPSHOT.jar"
}


variable "vault_addr" {
  default = "https://vault.ai-cloud.uk"
}

variable "cf_username" {
  #sensitive = true
}

variable "cf_password" {
  #sensitive = true
}

