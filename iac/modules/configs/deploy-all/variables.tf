variable "organisation" {
  default = "ccs-shared-services"
}

variable "space" {}

variable "environment" {}

variable "cf_username" {
  sensitive = true
}

variable "cf_password" {
  sensitive = true
}


variable "agreement_service_instances" {
  default = 1
}

variable "agreement_service_memory" {
  default = 1024
}
