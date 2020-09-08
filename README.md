# SCALE project Agreements Java microservice.

This is the implementation of the Agreements Service [Open API specification](https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/agreements/agreements-service.yaml).

It is deployed as part of the Terraform AWS environment provisioning scripts in [ccs-scale-infra-services-shared](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared).

## Creating a Docker Image
The [DEV-agreements-service](https://eu-west-2.console.aws.amazon.com/codesuite/codebuild/016776319009/projects/DEV-agreements-service) CodeBuild project in the Management account will trigger on a merge to `develop`. It will build a docker image and push it to the [scale/agreements-service](https://eu-west-2.console.aws.amazon.com/ecr/repositories/scale/agreements-service/?region=eu-west-2) ECR repository.

The image tag contains the shirt version of the Git commit hash.

## Deploying the new Docker Image to DEV
The Terraform script at [ccs-scale-infra-services-shared/blob/develop/terraform/modules/configs/deploy-all/variables.tf](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared/blob/develop/terraform/modules/configs/deploy-all/variables.tf) needs updating with the new docker image (it is the `ecr_image_id_agreements` variable).

The [ccs-scale-infrastructure-shared](https://eu-west-2.console.aws.amazon.com/codesuite/codepipeline/pipelines/ccs-scale-infrastructure-shared/view?region=eu-west-2) CodePipeline can then be triggered to deploy the change, initially to DEV, and then also up through the environments.