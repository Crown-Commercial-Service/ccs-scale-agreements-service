terraform {
  required_providers {
    cloudfoundry = {
      source  = "cloudfoundry-community/cloudfoundry"
      version = "0.13.0"
    }
    aws = {
      version = ">= 2.7.0"

    }
  }
}

provider "cloudfoundry" {
  api_url = "https://api.london.cloud.service.gov.uk"
  user     = var.cf_username
  password = var.cf_password
}
