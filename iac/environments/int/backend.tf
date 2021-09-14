terraform {
  backend "s3" {
    region = "eu-west-2"
    key    = "ccs-scale-agreements-service-int"
  }
}
