terraform {
  backend "s3" {
    region = "eu-west-2"
    key    = "shared/agreements-service"
  }
}
