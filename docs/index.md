---
layout: default
---

Text can be **bold**, _italic_, or ~~strikethrough~~.



# **Introduction**

springdoc-openapi java library helps automating the generation of API documentation using spring boot projects.
springdoc-openapi works by examining an application at runtime to infer API semantics based on spring configurations, class structure and various nnotations.

This library supports:
*  OpenAPI 3
*  Spring-boot 2
*  JSR-303, specifically for @NotNull, @Min, @Max, and @Size.
*  Swagger-ui
*  Oauth 2

In order to use it


# **Getting Started**
## Dependencies

The springdoc-openapi libraries are hosted on maven central repository. 
The artifacts can be viewed accessed at the following locations:

Release:
https://oss.sonatype.org/content/groups/public/org/springdoc/


Snapshot
https://oss.sonatype.org/content/repositories/snapshots/org/springdoc/

### Library for springdoc-openapi generator 
*   Generates documentation in JSON and YAML format
*   Documentation can be available at the following enpoint for json format: /openapi.json
*   Documentation can be available at the following enpoint for YAML format: /openapi.yml

### Integration of the libray in a spring-boot 2 projet:
*   You just need to add the library to the list of your project dependencies.
*   No additional configuration is needed.

```maven
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-core</artifactId>
			<version>1.0</version>
		</dependency>


### Library for springdoc-openapi integration with swagger-ui 
*   Automatically deploys swagger-ui to a spring-boot 2 application

```maven
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-ui</artifactId>
			<version>0.0.2-SNAPSHOT</version>
		</dependency>

### Integration of the libray in a spring-boot 2 projet:
*   You just need to add the library to the list of your project dependencies.
*   No additional configuration is needed.

## Adding API Information documentation
  The library uses spring-boot application auto-configured packages to scan for annations: OpenAPIDefinition and Info.
  These annotations declare, API Information: Title, version, licence, security, servers, tags, security and externalDocs

## Example application
*  https://springdoc-openapi-v3-rest-test-pipeline-nice-gecko.eu-de.mybluemix.net/


## Support for documentation from property file lookup



### Small image

![Octocat](https://github.githubassets.com/images/icons/emoji/octocat.png)

### Large image

![Branching](https://guides.github.com/activities/hello-world/branching.png)

