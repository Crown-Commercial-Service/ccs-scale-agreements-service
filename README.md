# CCS Agreements Service (API)

## Overview

This is the Java 17 SpringBoot implementation of the Agreement Service API [Open API specification](https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/agreements/agreements-service.yaml).

It is deployed via AWS & Terraform to the AWS environments.

## Prerequisites

The Agreements Service scripts located in the following two repos [CCS Scale DB Scripts](https://github.com/Crown-Commercial-Service/ccs-scale-db-scripts) and [CCS Scale DB Scripts Data](https://github.com/Crown-Commercial-Service/ccs-scale-db-scripts-data) should have been provisioned against a database for the target environment(s), to ensure the necessary basic level of data is present for the service to use when running.

## Running the service locally

In order to run the application locally, the database must be setup on the local machine.  In order to do this:

1. PGAdmin must be installed on the desired machine.

2. A database named "agreements" must be setup within PGAdmin.

3. The scripts referenced in the Prerequisites section above must be run against that local database, to populate it with initial data.

4. Environment variables must be amended in your run configuration to reflect your database access credentials.

Following these steps the application should successfully run.  Postman scripts to run against and test the service can be found in the private Postman API scripts repository.

## Deploying the service

The service will automatically trigger builds when commits are detected to the following branches:

- `develop` branch -> DEV(TST) environment
- `staging` branch -> PRE-PRODUCTION environment
- `main` branch -> PRODUCTION environment

Once builds have completed, manual approval for the deployment is required.  This can be made by an authorised user within the relevant AWS account.

When deploying the service an additional step is currently required, whereby the ECR Image Variable in [ccs-scale-infra-services-shared](https://github.com/Crown-Commercial-Service/ccs-scale-infra-services-shared) needs to be updated to reflect the build version being deployed.  This should currently be done manually.