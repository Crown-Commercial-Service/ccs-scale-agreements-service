# SCALE project Agreements Java microservice.

This is the implementation of the Agreements Service [Open API specification](https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/agreements/agreements-service.yaml).

It is deployed as part of the Terraform AWS environment provisioning scripts in [ccs-scale-infra-services-shared](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared).

CodeBuild project(s) exist in the Management account to build from source and deploy as a Docker image to ECR.


## Provision the service infrastructure via Travis

The main environments are provisioned automatically via Travis CI. Merges to key branches will trigger an automatic deployment to certain environments - mapped below:

* `develop` branch -> `development` space
* `release/int` branch -> `INT` space
* `release/nft` branch -> `nft` space
* other environments TBD (these mappings may change as we evolve the process as more environments come online)
* feature branches can be deployed to specific sandboxes by making minor changes in the `travis.yml` file (follow instructions)

## Deploying to UK.Gov PaaS (from local machine)

These are instructions on how to deploy the Agreements Service manually from your local machine to a space in UK.Gov PaaS (this will later be extended to be run in a Travis pipeline)

1. `cd` to `/uk-gov-paas/terraform/environments/{env}`

2. `cf service-key terraform-state terraform-state-key` will retrieve the AWS credentials to access the S3 state bucket. Use these to run the following `terraform init` command:

```
terraform init \
   -backend-config="access_key=AWS_ACCESS_KEY_ID" \
   -backend-config="secret_key=AWS_SECRET_ACCESS_KEY" \
   -backend-config="{bucket=S3_STATE_BUCKET_NAME}" \
```

3. Build the Agreements Service using maven `mvn clean package`

4. We use Terraform to provision the underlying service infrastructure in a space. We will need to supply the Cloud Foundry login credentials for the user who will provision the infrastructure (Note: it may be better to create a special account for this).

These credentials can be supplied in one of 3 ways:

* via a `secret.tfvars` file, e.g.  `terraform apply -var-file="secret.tfvars"` (this file should not be committed to Git)
* via input variables in the terminal, e.g. `terraform apply -var="cf_username=USERNAME" -var="cf_password=PASSWORD"`
* via environment variables, e.g. `$ export TF_VAR_cf_username=USERNAME`

Assume one of these approaches is used in the commands below (TBD)

5. Run `terraform plan` to confirm the changes look ok
6. Run `terraform apply` to deploy to UK.Gov PaaS