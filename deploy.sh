#!/bin/bash
#
# Deploys the CF shared infra
#

NEW_VAR=$S3_STATE_BUCKET_NAME

echo "Executing Terraform scripts to build shared space"
echo "Access: $ACCESS_KEY_ID"
echo "Secret: $SECRET_ACCESS_KEY"
echo "Bucket: $S3_STATE_BUCKET_NAME"
echo "User: $CF_USERNAME"
echo "Env: $CF_ENVIRONMENT"
echo "NEW_VAR: $NEW_VAR"
cd ./iac/environments/$CF_ENVIRONMENT/
terraform init -backend-config="access_key=$ACCESS_KEY_ID" -backend-config="secret_key=$SECRET_ACCESS_KEY" -backend-config="bucket=$S3_STATE_BUCKET_NAME"
terraform validate
terraform apply -auto-approve -input=false -var="cf_username=$CF_USERNAME" -var="cf_password=$CF_PASSWORD"
