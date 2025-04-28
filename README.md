CCS Agreements Service API
===========

Overview
--------
This is the code for the implementation of Crown Commercial Service's (_CCS_)
Agreements Service API, used by various downstream applications.

The specification for the API can be found in the [Open API Specification][].

Technology Overview
---------
The project is implemented as a Spring Boot 3 web application, implemented using Maven.

The core technologies for the project are:

* Java 23
* [Spring Boot][]
* [Spring Security][]
* [Ehcache][] for caching
* [MapStruct][] for entity mapping
* [JUnit][] for unit testing

Building and Running Locally
----------------------------
To run the application locally, you simply need to run the core ccs-scale-agreements-service module.

You will need to be supplied with a several environment variables to enable the project to run, which can be supplied by any member of the development team.

You will also need to setup a local database named "agreements" in order to run the service locally.  Scripts to setup the database for you are located in the following two repos [CCS Scale DB Scripts][] and [CCS Scale DB Scripts Data][].

Once the application has started it can be accessed via Postman using the URL http://localhost:9010/agreements-service.

Branches
--------
When picking up tickets, branches should be created using the **_feature/*_** format.

When completed, these branches should be pull requested against _**develop**_ for review and approval.  _**develop**_ is then built out onto the **Development** environment.

The **Pre-Production** environment is controlled via means of release and hotfix branches.

Release branches should be created from _**develop**_ using the **_release/*_** format, whilst hotfixes should be created from _**main**_ using the **_hotfix/*_** format.  These branches can then be built out to **Pre-Production** as appropriate.

When releases/hotfixes are ready for deployment to **Production**, the **_release/*_** or **_hotfix/*_** branch in question should be pull requested against the _**main**_ branch for review and approval.  This branch should then be built out to **Production**.

Once a release/hotfix has been completed you should be sure to merge _**main**_ back down into _**develop**_.

[Spring Boot]: https://spring.io/projects/spring-boot
[Spring Security]: https://spring.io/projects/spring-security
[JUnit]: https://junit.org/junit5/
[Ehcache]: https://www.ehcache.org/
[MapStruct]: https://mapstruct.org/
[Open API Specification]: https://github.com/Crown-Commercial-Service/ccs-scale-api-definitions/blob/master/agreements/agreements-service.yaml
[CCS Scale DB Scripts]: https://github.com/Crown-Commercial-Service/ccs-scale-db-scripts
[CCS Scale DB Scripts Data]: https://github.com/Crown-Commercial-Service/ccs-scale-db-scripts-data