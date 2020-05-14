[![Build Status](https://travis-ci.org/springdoc/springdoc-openapi.svg?branch=master)](https://travis-ci.org/springdoc/springdoc-openapi)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=springdoc_springdoc-openapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=springdoc_springdoc-openapi)
[![Known Vulnerabilities](https://snyk.io/test/github/springdoc/springdoc-openapi.git/badge.svg)](https://snyk.io/test/github/springdoc/springdoc-openapi.git)

![Octocat](https://springdoc.org/assets/images/springdoc-openapi.png)

# [Full documentation](https://springdoc.org)

# **Introduction**

The springdoc-openapi Java library helps automating the generation of API documentation using Spring Boot projects.
springdoc-openapi works by examining an application at runtime to infer API semantics based on Spring configurations, class structure and various annotations.

The library automatically generates documentation in JSON/YAML and HTML formatted pages. The generateed documentation can be complemented using `swagger-api` annotations.

This library supports:
*  OpenAPI 3
*  Spring-boot (v1 and v2)
*  JSR-303, specifically for @NotNull, @Min, @Max, and @Size.
*  Swagger-ui
*  Oauth 2

This is a community-based project, not maintained by the Spring Framework Contributors (Pivotal)

# **Getting Started**

## Library for springdoc-openapi integration with spring-boot and swagger-ui 
*   Automatically deploys swagger-ui to a Spring Boot 2.x application
*   Documentation will be available in HTML format, using the official [swagger-ui jars](https://github.com/swagger-api/swagger-ui.git).
*   The Swagger UI page should then be available at http://server:port/context-path/swagger-ui.html and the OpenAPI description will be available at the following url for json format: http://server:port/context-path/v3/api-docs
    * `server`: The server name or IP
    * `port`: The server port
    * `context-path`: The context path of the application
*   Documentation can be available in yaml format as well, on the following path: /v3/api-docs.yaml
*   Add the `springdoc-openapi-ui` library to the list of your project dependencies (No additional configuration is needed):

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-ui</artifactId>
      <version>last-release-version</version>
   </dependency>
```
*   This step is optional: For custom path of the swagger documentation in HTML format, add a custom springdoc property, in your spring-boot configuration file:

```properties
# swagger-ui custom path
springdoc.swagger-ui.path=/swagger-ui.html
```

## [Demo Spring Boot 2 Web MVC with OpenAPI 3](https://springdoc-openapi-test-app2-rested-ardvark.eu-de.mybluemix.net/).
## [Demo Spring Boot 2 WebFlux with OpenAPI 3](https://springdoc-openapi-test-app3-generous-springhare.eu-de.mybluemix.net/swagger-ui.html).
## [Demo Spring Boot 1 Web MVC with OpenAPI 3](https://springdoc-openapi-test-app1-exhausted-civet.eu-de.mybluemix.net/).
## [Demo Spring Boot 2 WebFlux with Functional endpoints OpenAPI 3](https://springdoc-openapi-test-app4-happy-ostrich.eu-de.mybluemix.net/swagger-ui.html).

![Branching](https://springdoc.org/assets/images/pets.png)

## Source code of the Demo Applications
* [https://github.com/springdoc/springdoc-openapi-demos.git](https://github.com/springdoc/springdoc-openapi-demos.git).

## Integration of the library in a Spring Boot 2.x.x project without the swagger-ui:
*   Documentation will be available at the following url for json format: http://server:port/context-path/v3/api-docs
    * `server`: The server name or IP
    * `port`: The server port
    * `context-path`: The context path of the application
*   Documentation will be available in yaml format as well, on the following path : `/v3/api-docs.yaml`
*   Add the library to the list of your project dependencies. (No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-webmvc-core</artifactId>
      <version>last-release-version</version>
   </dependency>
```
* This step is optional: For custom path of the OpenAPI documentation in Json format, add a custom springdoc property, in your spring-boot configuration file:

```properties
# /api-docs endpoint custom path
springdoc.api-docs.path=/api-docs
```

* This step is optional: If you want to disable `springdoc-openapi` endpoints, add a custom springdoc property, in your `spring-boot` configuration file:

```properties
# /api-docs endpoint custom path
springdoc.api-docs.enabled=false
```
 
## Error Handling for REST using @ControllerAdvice
To generate documentation automatically, make sure all the methods declare the HTTP Code responses using the annotation: @ResponseStatus

## Adding API Information and Security documentation
  The library uses spring-boot application auto-configured packages to scan for the following annotations in spring beans: OpenAPIDefinition and Info.
  These annotations declare, API Information: Title, version, licence, security, servers, tags, security and externalDocs.
  For better performance of documentation generation, declare `@OpenAPIDefinition` and `@SecurityScheme` annotations within a Spring managed bean.  
  
## spring-webflux support with Annotated Controllers
*   Documentation can be available in yaml format as well, on the following path : /v3/api-docs.yaml
*   Add the library to the list of your project dependencies ( No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-webflux-ui</artifactId>
      <version>last-release-version</version>
   </dependency>
```
*   This step is optional: For custom path of the swagger documentation in HTML format, add a custom springdoc property, in your spring-boot configuration file:

```properties
# swagger-ui custom path
springdoc.swagger-ui.path=/swagger-ui.html
```

## spring-webflux with Functional Endpoints, will be available in the future release
## Dependencies repository

The `springdoc-openapi` libraries are hosted on maven central repository. 
The artifacts can be viewed accessed at the following locations:

Releases:
* [https://oss.sonatype.org/content/groups/public/org/springdoc/](https://oss.sonatype.org/content/groups/public/org/springdoc/).

Snapshots:
* [https://oss.sonatype.org/content/repositories/snapshots/org/springdoc/](https://oss.sonatype.org/content/repositories/snapshots/org/springdoc/).


