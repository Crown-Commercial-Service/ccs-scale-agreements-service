# SCALE project Agreements Java microservice.

This service allows access to CCS Commercial Agreement data. This is used both to provide information to users and to govern the behaviour of other systems for example 'Buy a Thing' and 'Contract for a Thing', where processes may differ based on the configuration of the underlying Commercial Agreement and Lot.

This is based on the [Open API specification](https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/agreements/agreements-service.yaml).

It is deployed as part of the Terraform AWS environment provisioning scripts in [ccs-scale-infra-services-shared](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared).

## Deployment

### Creating a Docker Image
The [DEV-agreements-service](https://eu-west-2.console.aws.amazon.com/codesuite/codebuild/016776319009/projects/DEV-agreements-service) CodeBuild project in the Management account will trigger on a merge to `develop`. It will build a docker image and push it to the [scale/agreements-service](https://eu-west-2.console.aws.amazon.com/ecr/repositories/scale/agreements-service/?region=eu-west-2) ECR repository.

The image tag contains the short version of the Git commit hash.

### Deploying the new Docker Image to DEV
The Terraform script at [ccs-scale-infra-services-shared/blob/develop/terraform/modules/configs/deploy-all/variables.tf](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared/blob/develop/terraform/modules/configs/deploy-all/variables.tf) needs updating with the new docker image (update the `ecr_image_id_agreements` variable).

The [ccs-scale-infrastructure-shared](https://eu-west-2.console.aws.amazon.com/codesuite/codepipeline/pipelines/ccs-scale-infrastructure-shared/view?region=eu-west-2) CodePipeline can then be triggered to deploy the change, initially to DEV, and then also up through the environments. Approval is required at each stage.

### Automating deployment to an SBX environment
It is possible to automatically update the Fargate task on a merge to `develop` to speed up development. This is usually only recommended for SBX environments.

To do this - we just need to override the docker image tag that environment references to be `latest`. This needs to to be done in the relevant environment script in [ccs-scale-infra-services-shared/tree/develop/terraform/environments](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared/tree/develop/terraform/environments)

An example is shown below - the `ecr_image_id_agreements` variable needs to be added to the `deploy` module and set to `latest`.
```
module "deploy" {
  source         = "../../modules/configs/deploy-all"
  aws_account_id = data.aws_ssm_parameter.aws_account_id.value
  environment    = local.environment

  ecr_image_id_agreements = "latest"
}
```

This means that whenever a merge to develop is made - the latest version is pushed to the Fargate service.