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
  api_url  = "https://api.london.cloud.service.gov.uk"
  user     = "adrian.milne@crowncommercial.gov.uk"
  password = "{{TEMPORARY TEST APPROACH}}"

}

provider "aws" {
  region = "eu-west-2"
}
