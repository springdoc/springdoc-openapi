---
layout: default
---
![Octocat](https://springdoc.github.io/springdoc-openapi/images/springdoc-openapi.png)

# **Introduction**

springdoc-openapi java library helps automating the generation of API documentation using spring boot projects.
springdoc-openapi works by examining an application at runtime to infer API semantics based on spring configurations, class structure and various nnotations.

Automatically generates basic documentation of APIs. 
This documentation can be completed by comments using swagger-api annotations.

This library supports:
*  OpenAPI 3
*  Spring-boot 2
*  JSR-303, specifically for @NotNull, @Min, @Max, and @Size.
*  Swagger-ui
*  Oauth 2

# **Getting Started**
## Dependencies

The springdoc-openapi libraries are hosted on maven central repository. 
The artifacts can be viewed accessed at the following locations:

Releases:
* [https://oss.sonatype.org/content/groups/public/org/springdoc/](https://oss.sonatype.org/content/groups/public/org/springdoc/).

Snapshots:
* [https://oss.sonatype.org/content/repositories/snapshots/org/springdoc/](https://oss.sonatype.org/content/repositories/snapshots/org/springdoc/).

### Library for springdoc-openapi generator 
*   Generates documentation in JSON and YAML format
*   Documentation can be available at the following enpoint for json format: /v3/api-docs
*   Documentation can be available at the following enpoint for YAML format: /v3/api-docs.yml
*   Documentation will be available at the following url: http://server:port/context-path/v3/api-docs
    * server: The server name or IP
    * port: The server port
    * context-path: The context path of the application


### Integration of the libray in a spring-boot 2 projet:
*   Add the library to the list of your project dependencies. (No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-core</artifactId>
      <version>0.0.8</version>
   </dependency>
```

### Library for springdoc-openapi integration with swagger-ui 
*   Automatically deploys swagger-ui to a spring-boot 2 application
*   The Swagger UI page should then be available at http://server:port/context-path/swagger-ui.html
    * server: The server name or IP
    * port: The server port
    * context-path: The context path of the application

*   Add the library to the list of your project dependencies (No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-ui</artifactId>
      <version>0.0.8</version>
   </dependency>
```
## [Example application 1](https://springdoc-openapi-test-app2-silly-numbat.eu-de.mybluemix.net/swagger-ui.html).
## [Example application 2](https://springdoc-openapi-test-app1-courteous-puku.eu-de.mybluemix.net/).


![Branching](https://springdoc.github.io/springdoc-openapi/images/pets.png)


## Adding API Information documentation
  The library uses spring-boot application auto-configured packages to scan for annations: OpenAPIDefinition and Info.
  These annotations declare, API Information: Title, version, licence, security, servers, tags, security and externalDocs
 







