[![Build Status](https://travis-ci.org/springdoc/springdoc-openapi.svg?branch=master)](https://travis-ci.org/springdoc/springdoc-openapi)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.springdoc%3Aspringdoc-openapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.springdoc%3Aspringdoc-openapi)

# **Introduction**

springdoc-openapi java library helps automating the generation of API documentation using spring boot projects.
springdoc-openapi works by examining an application at runtime to infer API semantics based on spring configurations, class structure and various nnotations.

Automatically generates documentation in JSON/YAML and HTML format APIs. 
This documentation can be completed by comments using swagger-api annotations.

This library supports:
*  OpenAPI 3
*  Spring-boot 2
*  JSR-303, specifically for @NotNull, @Min, @Max, and @Size.
*  Swagger-ui
*  Oauth 2

# **Getting Started**
## Integration of the libray in a spring-boot 2 project:
*   Documentation will be available at the following url for json format: http://server:port/context-path/v3/api-docs
    * server: The server name or IP
    * port: The server port
    * context-path: The context path of the application
*   Documentation will be available in yaml format as well, on the following path : /v3/api-docs.yml
*   Add the library to the list of your project dependencies. (No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-core</artifactId>
      <version>1.1.1</version>
   </dependency>
```
*   For custom path of the OpenAPI documentation in Json format, add a custom springdoc property, in your spring-boot configuration file:

```properties
# /api-docs endpoint custom path
springdoc.api-docs.path=/api-docs
```
## Library for springdoc-openapi integration with swagger-ui 
*   Automatically deploys swagger-ui to a spring-boot 2 application
*   Documentation will be available in HTML format, using the official [swagger-ui jars]: (https://github.com/swagger-api/swagger-ui.git).
*   The Swagger UI page should then be available at http://server:port/context-path/swagger-ui.html
    * server: The server name or IP
    * port: The server port
    * context-path: The context path of the application
*   Add the library to the list of your project dependencies (No additional configuration is needed)

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-ui</artifactId>
      <version>1.1.1</version>
   </dependency>
```
*   For custom path of the swagger documentation in HTML format, add a custom springdoc property, in your spring-boot configuration file:

```properties
# swagger-ui custom path
springdoc.swagger-ui.path=/swagger.html
```

## [Demo application 1](https://springdoc-openapi-test-app2-silly-numbat.eu-de.mybluemix.net/).
## [Demo application 2](https://springdoc-openapi-test-app1-courteous-puku.eu-de.mybluemix.net/).

![Branching](https://springdoc.github.io/springdoc-openapi-demos/images/pets.png)


## Source code of the Demo Applications
*   https://github.com/springdoc/springdoc-openapi-demos.git

