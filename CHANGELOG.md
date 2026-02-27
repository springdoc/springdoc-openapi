# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.8.16] - 2026-02-27

### Added

- Add support for `springdoc.swagger-ui.document-title` property to customize the browser tab title

### Changed

- Upgrade Spring Boot to version **3.5.11**
- Upgrade swagger-core to version **2.2.43**
- Upgrade swagger-ui to version **5.31.2**
- Upgrade Scalar to version **0.5.55**

### Fixed

- #3230 – Scalar source URLs resolve to `null/<groupName>` on second request when using `GroupedOpenApi`
- #3226 – Propagate `@JsonView` context when resolving `Page<T>` schema in `PageOpenAPIConverter`
- #3205 – springdoc-ui does not work with native compile GraalVM 25
- #3219 – Upgrade swagger-core from 2.2.42 to 2.2.43 (fixes schema resolution issues)
- #3193 – OpenApi field in `SpringDocConfigProperties` does not comply with camelCase naming conventions
- #3161 – Prevent duplicate `_links` in `allOf` child schemas extending `RepresentationModel`
- Fix type annotation not considered when Kotlin is not present
- Fix property resolution for parameter default values

## [2.8.15] - 2026-01-01

### Added

- #3122 – Add log notifications when SpringDocs / Scalar are enabled by default
- #3123 – Add support for serving static resources
- #3151 – Add `@Order` to `ApplicationReadyEvent` listener
- #3158 – Add support for API groups in Scalar
- #3187 – Add Scalar WebMVC and WebFlux support
- #3185 – Disable creation of blank GitHub issues (GitHub settings & workflow)
- #3186 – Decouple Web Server APIs following Spring Boot modularization
- #3131 - Improve warning messages when documentation is explicitly enabled
- #3183 - Remove unused operations consumer from route builder methods
- #3141 - Change handling so `useReturnTypeSchema` is evaluated at HTTP status code level instead of method level

### Changed

- Upgrade Spring Boot to version **3.5.9**
- Upgrade swagger-core to version **2.2.41** 
- Upgrade swagger-ui to version **5.31.0**
- Upgrade Scalar to version **0.4.3**

### Fixed

- #3133 – Fix regression where content type from Swagger `@RequestBody` did not take precedence
- #3146 – Fix WebJar resource handler mappings for Swagger UI resources
- #3168 – Support `@Schema` annotations on Kotlin value classes
- #3178 – Fix regression when generating documentation for Kotlin `LinkedHashSet`
- #3170 – Fix warnings when setting title and description in `application.yml`
- #3187 – Add scalar scalar-webmvc and scalar-webflux support

## [2.8.14] - 2025-11-02

### Added

- #3090 - Add logs to notify when SpringDocs/Scalar is enabled because SpringDocs/Scalar is enabled by default

### Changed

- Upgrade swagger-ui to v5.30.1
- Upgrade swagger-core to v2.2.38
- Upgrade spring-boot to v3.5.7
- Upgrade commons-lang3 to v3.18.0
- Upgrade scalar to v0.3.12

### Fixed

- #3107 - Fix:compatible with lower version of getOpenApi().
- #3121 - NPE in KotlinDeprecatedPropertyCustomizer - resolvedSchema is null

## [2.8.13] - 2025-09-07

### Added

- #3084 - Add Scalar Support

### Changed

- Upgrade swagger-ui to v5.28.1

### Fixed

- #3076 - With oneOf the response schema contains an extra type: string

## [2.8.12] - 2025-09-01

### Changed

- Upgrade swagger-ui to v5.28.0

### Fixed

- #3073 - Duplicate key class Parameter when documenting two GET methods with same path and PathVariable.
- #3071 - @io.swagger.v3.oas.annotations.parameters.RequestBody does not work well with @RequestPart
- #3066 - Parameter is now required after upgrading to springdoc-openapi 2.8.10

## [2.8.11] - 2025-08-23

### Added

- #3065 - javadoc and overall performance optimization

### Changed

- Upgrade spring-boot to v3.5.5

### Fixed

- #3064 -ClassNotFoundException: kotlin.reflect.full.KClasses

## [2.8.10] - 2025-08-20

### Added

- #3046 - Feature Request: Support @jakarta.annotation.Nonnull.
- #3042 - Support externalDocs configure on SpecPropertiesCustomizer
- #3057 - Refactor webhook discovery and scanning mechanism 

### Changed

- Upgrade spring-boot to v3.5.4
- Upgrade swagger-ui to v5.27.1
- Upgrade swagger-core to 2.2.36

### Fixed

- #3050 - @RequestPart JSON parameters missing Content-Type in generated curl commands, causing 415 errors.
- #2978 - Parameter is no longer optional after upgrade to 2.8.8
- #3022 - NullPointerException thrown in SchemaUtils.
- #3026 - Fix unexpected merging of media types
- #3036 - Fixed "desciption" 
- #3039 - Fix: Property resolution for extensions within @OpenAPIDefinition Info object
- #3051 -  Fixes so that a RequestPart with a Map is added to the RequestBody
- #3060 - Use adaptFromForwardedHeaders instead of deprecated fromHttpRequest 

## [2.8.9] - 2025-06-10

### Added

- #2944 - Support for @Positive
- #3011 - type-use for method parameters

### Changed

- Upgrade spring-boot to version 3.5.0

### Fixed

- #2982 - application/problem+json content type is not set for ProblemDetails
- #2990 - Issues with POST Request, application/x-www-form-urlencoded and only one parameter
- #2998 - io.swagger.v3.oas.annotations.Webhook does not work when defined on the method level
- #3012 - Order of examples is (sometimes) not preserved

## [2.8.8] - 2025-05-04

### Fixed

- #2977 - Handle projects not using kotlin-reflect #2977

## [2.8.7] - 2025-05-04

### Added

- #2944 - Introducing springdoc-openapi-bom project
- #2948 - Customize Servers via application.yml
- #2963 - Set default content type for problem details object to application/problem+jso
- #2971 - List of value classes in Kotlin

### Changed

- Upgrade swagger-ui to v5.21.0
- Upgrade swagger-core to 2.2.30
- Upgrade spring-boot to version 3.4.5
- Upgrade spring-security-oauth2-authorization-server to version 1.4.3

### Fixed

- #2947 - Unexpected warning "Appended trailing slash to static resource location"
- #2960 - NPE when customizing group's open-api without specifying any schema
- #2969 - fix path to register resource handler to work SwaggerIndexPageTransformer
  considering /webjar path prefix
- #2964 - Cannot add custom description and example for java.time.Duration since v2.8.6
- #2972 - @Header(schema = @Schema(type = "string")) generates empty or broken schema in
  OpenAPI output since 2.8.0
- #2976, #2967 - Build Failure due to Private Inner Class.

## [2.8.6] - 2025-03-23

### Added

- #2909 - Check both SerDe BeanPropertyDefinition for @JsonUnwrapped/@Schema
- #2927 - Bail sealed class subtype introspection on Schema
- #2917 - Add Future to ignored response wrappers
- #2938 - Add out of the box support for LocalTime, YearMonth, MonthDay

### Changed

- Upgrade swagger-ui to v5.20.1
- Upgrade swagger-core to 2.2.29
- Upgrade spring-cloud-function to 4.2.2
- Upgrade spring-boot to version 3.4.4

### Fixed

- #2928 - Add missing builder methods in SchemaBuilder
- #2905 - ModelResolver.enumAsRef = true result in invalid openapi with actuator using
  enum param
- #2939 - Duplicate ModelConverter registration with Spring Boot DevTools
- #2941 - SpringBoot native fails /v3/api-docs when using a Map as an http entity field

## [2.8.5] - 2025-02-16

### Added

- #2696 - Do not require JsonSubType annotation for sealed classes
- #2898 - add needed runtime reflection hints for native image
- #2891 - Refactor trimIndent Method
- #2931 - OpenAPIService serverBaseUrl is not thread safe
- #2933 - Wrong schema generation with PagedModel generated VIA_DTO and wrapped in
  ResponseEntity

### Changed

- Upgrade swagger-ui to v5.18.3

### Fixed

- #2902 - Schema replaced by String when using @ApiResponse with RepresentationModel (
  Hateoas links)
- #2876 - Restentpoints with same name get mix up
- #2895 - Only filter out actuator endpoints with double asterisks.
- #2894 - respect @JsonUnwrapped & @Schema on props not fields only
- #2881 - fix defaultValue when using @PageableDefault together with
  one-indexed-parameters
- #2888 - Provide a better consistency for parameters and responses order.

## [2.8.4] - 2025-01-25

### Added

- #2873 - Improve performance of getGenericMapResponse
- #2836 - Provide option to set allowed locales
- 2862 - Align Swagger-UI Prefix Path with Swagger-WebMvc Behavior

### Changed

- Upgrade spring-boot to 3.4.2
- Upgrade spring-cloud-function to 4.2.1
- Upgrade swagger-core to 2.2.28

### Fixed

- #2870 - Springdoc 2.8.x + Spring Boot 3.4.1 breaks native image support
- #2869 - Exception logged when generating schema for delete method of Spring Data
  repository.
- #2856 - @JsonUnwrapped is ignored in new version of lib.
- #2852 - @Schema(types = "xxx") does not work for multipart param with enabled
  springdoc.default-support-form-data config option.

## [2.8.3] - 2025-01-12

### Added

- #2851 - Refine condition, for ignoring types when using PolymorphicModelConverter

## [2.8.2] - 2025-01-12

### Added

- #2849 - Provide better compatibility for projects migrating from OAS 3.0 to OAS 3.1

### Fixed

- #2846 - ClassCastException with spring-data-rest and openapi version 3.1 bug
- #2844 - PageableObject and SortObject are called Pageablenull and Sortnull

## [2.8.1] - 2025-01-06

### Fixed

- #2834 - java.lang.ClassNotFoundException: kotlin.reflect.full.KClasses when upgrade from
  2.7.0 to 2.8.0

## [2.8.0] - 2025-01-03

### Added

- #2790 - Moving to OpenAPI 3.1 as the default implementation for springdoc-openapi
- #2817 - Obey annotations when flattening ParameterObject fields
- #2826 - Make it possible to mark parameters with @RequestParam annotation to be sent in
  form instead of query.
- #2822 - Support returning null in ParameterCustomizer
- #2830 - Add support for deprecated fields.
- #2780 - Add Security Schema by AutoConfigure

### Changed

- Upgrade spring-boot to 3.4.1
- Upgrade spring-cloud-function to 4.2.0
- Upgrade swagger-core to 2.2.27

### Fixed

- #2804 - Stable release 2.7.0 depends on Spring Cloud Milestone 4.2.0-M1
- #2828 - Required a bean of type '
  org.springframework.data.rest.webmvc.mapping.Associations' that could not be found.
- #2823 - Capturing pattern in identical paths only renders the path element of one method
- #2817 - Automatically add required if a field is @notNull or @NotBlank.
- #2814 - An unresolvable circular reference with
  management.endpoint.gateway.enabled=true.
- #2798 - Object schema generated for Unit Kotlin type.
- #2797 - Removing operationId via customizer does not work anymore.
- #2833 - Resolve infinite recursion and add example test with OpenAPI v3.1
- #2827 - Ignoring @Parameter(required = false)

## [2.7.0] - 2024-23-11

### Added

- #2777 - Add SortAsQueryParam annotation
- #2786 - No static resource swagger-ui/index.html error after migration to 2.7.0-RC1

### Changed

- Upgrade spring-boot to 3.4.0
- Upgrade swagger-ui to 5.18.2
- Upgrade spring-security-oauth2-authorization-server to 1.4.0

## [2.7.0-RC1] - 2024-11-06

### Added

- #2649 - Add Encoding to multiple files and JSON payloads request test case
- #2653 - Trim indent apply schema description
- #2664 - Refactor Replace hardcoded schema prefix length
- #2509, #2668 - Replace swagger urls in
  org.springdoc.core.properties.AbstractSwaggerUiConfigProperties#urls only if url is
  changed
- #2727 - Display nullable request body with map type
- #2746 - Readme.md add gradle import
- #2760 - Added support for RequestBody as a meta-annotation
- #2703 - Display nullable request body with map type
- #2657 - Add support for OAS v3.1 webhooks

### Changed

- Upgrade spring-boot to 3.4.0-RC1
- Upgrade swagger-core to 2.2.25
- Upgrade swagger-ui to 5.18.1
- Upgrade spring-cloud-function to 4.2.0-M1
- Upgrade spring-security-oauth2-authorization-server to 1.4.0-M2

### Fixed

- #2752 - Swagger doesn't work after custom annotation replacing request parameters
- #2747 - Move to webjars-locator-lite, in preparation for spring-boot 3.4 GA
- #2705 - @Schema oneOf config is ignored when generate the api-docs
- #2744 - SpringDocUI doest add Javadoc into swagger from abstract class
- #2708 - Spring Boot (Webflux) - Swagger UI - redirect URI does not include Gateway
  Prefix
- #2725 - Serialization to openapi of org.springframework.data.domain.Sort is wrong for
  Spring Boot >2.x
- #2740 - Swagger-ui ignores property springdoc.swagger-ui.supported-submit-methods
- #2733 - Bad schema return type when created a generic wrapper class for response entity
- #2687 - Failed to load api definition after spring boot 3.4.0-M2
- #2642 - Calling Swagger UI via different context paths fails
- #2709 - Annotation @Hidden on rest controller class level doesn't work due to spring
  default proxying mechanism CGLIB
- #2642 - Calling Swagger UI via different context paths fails
- #2663 - Content definition in @ApiResponse remove schema generated based on the returned
  value
- #2646 - The operationId is unnecessarily deduplicated for a requestBody with multiple
  content types
- #2643 - UpperSnakeCaseStrategy is not working with spring boot and ParameterObject
- #2640 - @JsonUnwrapped is ignored when PolymorphicConverter is enabled
- #2638 - Boolean Parameter with @Schema Annotation Changes Type to string in OpenAPI
  Documentation
- #2659 - Fix typo in SpringSecurityLoginEndpointCustomizer method name
- #2660 - Update Response Code
- #2442, #2669 - Fix SpringDocApp193Test for Java 21 and above
- #2671 - Ensure default media type order is preserved using LinkedHashSet in mergeArrays
- #2711 - Missing descriptions on Kotlin ByteArray fields
- #2733 - Bad schema return type when created a generic wrapper class for response entity

## [2.6.0] - 2024-06-30

### Added

- #2561 - NPE occurs when outputting an OpenAPI document since 2.5.0
- #2579 - Add support for leading tab characters with trim-kotlin-indent.
- #2589 - Pass HttpRequest to ServerBaseUrlCustomizer
- #2596, #2600 - consumes and produces calculation. Fixes
- #2625, #2626 - Replace Page schema with PagedModel when pageSerializationMode is set to
  VIA_DTO
- #2627 - Ensure compatibility with previous version of spring data
- #2576 - GroupedApi orders by displayName instead of name.
- #2584 - Dynamically define ApiGroups does not work.
- #2595 - Spring security support of @RegisteredOAuth2AuthorizedClient

### Changed

- Upgrade spring-boot to 3.3.0
- Upgrade swagger-core to 2.2.22
- Upgrade swagger-ui to 5.17.14
- Upgrade spring-cloud-function to 4.1.2
- Upgrade spring-security-oauth2-authorization-server to 1.3.0

### Fixed

- #2577 - Fix missing exception response types in OpenAPI spec
- #2591 - When an entity class contains fields of Class<?> type, an infinite loop.
- #2603 - PolymorphicModelConverter only handles direct subtypes and misses indirect.
- #2606 - Spring Authorization Server Metadata Endpoint not compatible.
- #2621 - Content-type for POST endpoints with multipart/form-data does not work since
  v2.4.0.
- #2622 - Kotlin enums are always marked as required if used in Java controllers.
- #2601 - Multiple Superclasses Are Not Mapped To Multiple allOf If Used In Different
  Services.
- #2597 - Polymorphic fields on polymorphic parents don't get correct oneOf docs
  generated.

## [2.5.0] - 2024-04-01

### Added

- #2318 - Add Info to GroupedOpenAPI properties
- #2554 - Remove duplicate words from comments
- #2418 - Improve support for externalizing strings in generated openapi
- #2535 - Add 'springdoc.trim-kotlin-indent' property to handle Kotlin multiline string
  indentation

### Changed

- Upgrade spring-boot to 3.2.4
- Upgrade swagger-core to 2.2.21
- Upgrade swagger-ui to 5.13.0

### Fixed

- #2525 - Inherited Methods Not Included in Swagger Documentation with @RouterOperation in
  Spring Boot WebFlux Application
- #2526 - SpringDoc bean naming conflict error with GraphQL Spring boot starter
- #2540 - Fix typo in SpringRepositoryRestResourceProvider.java
- #2549 - Fix README.md

## [2.4.0] - 2024-03-12

### Added

- #2443 - Respect schema annotations when using spring mvc with kotlin
- #2492, #2488 - Support dynamic evaluation of description field in the RequestBody
- #2510 - Option to disable root api-docs path when using groups

### Changed

- Upgrade spring-boot to 3.2.3
- Upgrade swagger-core to 2.2.20
- Upgrade swagger-ui to 5.11.8

### Fixed

- #2453 - Fix CODE_OF_CONDUCT.md links
- #2454 - Fix typo in SwaggerWelcomeWebMvc
- #2507 - Fix typo in Constants
- #2472 - Update JavadocPropertyCustomizer.java
- #2495 - Fix broken links in README and CONTRIBUTING
- #2501 - bug fix when "exported" is set to false in RestResource annotation
- #2447 - Serialization to openapi of org.springframework.data.domain.Sort is not done
  correctly
- #2449 - Extensions in subobjects of OpenAPI no longer work
- #2461 - Springdoc OpenApi Annotations @ExtensionProperty Not Evaluating Properties from
  application.yml
- #2469 - Pom contains invalid organizationUrl
- #2518 - Duplicate GroupConfigs in SpringDocConfigProperties
- #2506 - Springdoc breaks (Unexpected value: TRACE) when a
  spring-cloud-starter-gateway-mvc universal gateway is configured.
- #2519 - Request parameter parsing error after using @NotBlank from type interface field
- #2516 - Spring Data REST fails when setting version to openapi_3_1
- #2509 - ArrayIndexOutOfBoundsException in SwaggerUiConfigParameters
- #2484 - JavaDoc integration not working with SnakeCaseStrategy property naming
- #2483 - Controller advice documents ApiResponse on every operation, even if the
  operation does not annotate the exception to be thrown
- #2477 - buildApiResponses ignores produced ContentType in case of many @Operation

## [2.3.0] - 2023-12-03

### Added

- #2340 - Add support OIDC with Spring Authorization Server
- #2345 - Support Schema added in OpenAPI Specification v3.1
- #2387 - Support get javadoc description from getter method
- #2404 - Update condition to register links schema customizer
- #2359 - Update condition to register links schema customizer
- #2348 - Enhance resource path processing
- #2438, #2315 - Support for @JsonProperty with Javadoc Change in springdoc-openapi

### Changed

- Upgrade spring-boot to 3.2.0
- Upgrade swagger-core to 2.2.19
- Upgrade swagger-ui to 5.10.3

### Fixed

- #2366 - Fix the failed test due to hardcoded file separators
- #2370, #2371 - No empty description for polymorphic subtypes
- #2373 - SchemaProperty.array Schema is ignored in /api-docs or api-docs.yaml
- #2366 - Refactoring AbstractSwaggerResourceResolver.findWebJarResourcePath
- #2320 - javadoc for class attribute ignored when in EntityModel.
- #2347 - Not working if a property of entity contains generic parameters.
- #2399 - SpringdocRouteBuilder.onError is overriding last route defined.
- #2426 - StackOverflowError when using @ParameterObject on groovy class.

## [2.2.0] - 2023-08-06

### Added

- #2189 - Add support for swagger-ui.url property
- #2200 - Support schema.requiredMode() on ParameterObject
- #2309 - Added function to preload by specifying locale
- #2332 - Group name cannot be null or empty
- #2281 - Initial Virtual thread support

### Changed

- Upgrade spring-boot to 3.1.2
- Upgrade swagger-core to 2.2.15
- Upgrade swagger-ui to 5.2.0

### Fixed

- #2199 - Fix Schema get condition of ArraySchema.
- #2194 - Fix Swagger UI with provided spec
- #2213 - Using both generated and configured specs stoped working in 1.6.5
- #2222 - String Index Out of Bounce Exception Fix when deployed on Azure
- #2243, #2235 - Fix StringIndexOutOfBoundsException when path is same webjar
- #2291 - Fix default-flat-param-object doesn't work when using http body
- #2310 - Change bean name of objectMapperProvider
- #2207 - swagger-initializer.js is sent endcoded in the JVM's default charset
- #2271, #2280 - Fix loop when response inherits generic class fixes
- #2312 - Spec for @ParameterObject disappears if building native-images
- #2326 - @QuerydslPredicate(root = X.class) annotation at Controller Method level not
  getting documented in Spring Boot 3

## [2.1.0] - 2023-04-01

### Added

- #2152 - Detect directions in default sort values
- #2167 #2166 - Add request parameter for token endpoint
- #2188 - Support of {*param} path patterns

### Changed

- Upgrade spring-boot to 3.0.5
- Upgrade swagger-core to 2.2.9
- Upgrade swagger-ui to 4.18.2
- Spring Native is now superseded by Spring Boot 3 official
- #2173 - Remove webjars-locator-core

### Fixed

- #2122 - Super tiny fix typo
- #2131 - Fixed a bug that javadoc of record class parameters was not recognized.
- #2140 - Javadoc record class parameters not recognized
- #2123 #2141 - fix spring authorization server response.
- #2148 - Fix properties show-oauth2-endpoints and
  SpringDocConfigProperties#showOauth2Endpoint properties name mismatch
- #2149 - Request parameters with default values are marked as required.
- #2155 - openApi.getServers() is null in OpenApiCustomiser when using different locales.
- #2152 - Redundant(wrong) direction appended to @PageableDefault.
- #2181 #2183 - Fixed DefaultFlatParamObject to work with annotated parameters.
- #2170 #2187 - All request parameters marked as required for Java controllers in mixed
  projects in 2.0.3
- #2165 - Custom Converters are not excluded if not registered for Http Message Converter.
- #2185 - Fix behaviour of required flag for schema class fields.
- #2139 - SpringDocSecurityConfiguration class not sufficiently constrained.
- #2142 - SpringDocJacksonModuleConfiguration is loaded even though there is no
  ObjectMapperProvider when springdoc.api-docs.enabled = false.

## [2.0.4] - 2023-03-15

### Changed

- Upgrade swagger-ui to 4.18.1

### Fixed

- #2123 - Fix spring authorization server response
- #2131 - Fixed a bug that javadoc of record class parameters was not recognized.
- #2114 - Exception during WebFlux tests: NoClassDefFoundError: KotlinModule$Builder

## [2.0.3] - 2023-03-07

### Added

- #2006 - Support for nullable request parameters in Kotlin.
- #2054 - Add copyright and license information to Jar.
- #2021 - Required field in Schema annotation ignored in Kotlin.
- #2094 - Initial support for Spring Authorization Server.

### Changed

- Upgrade spring-boot to 3.0.4
- Upgrade swagger-core to 2.2.8
- Upgrade swagger-ui to 4.17.1

### Fixed

- #2010 - findByNameContainingIgnoreCaseAndDateBefore throw NullPointerException.
- #2031 - Path variables parameters are not assigned correctly to endpoints.
- #2038 - When extends JpaRepository, using @Parameter over the method results in
  duplicate of the same parameter.
- #2046 - Map Fields Disappear with Groovy on Classpath.
- #2051 - Malformed api-docs JSON when StringHttpMessageConverter is not active
- #2062 - OperationCustomizer is not working with Spring Data REST.
- #2098 - When getting ExceptionHandler in the controller, use target class in case of AOP
  Proxy.
- #2107 - Ordering of GlobalOpenApiCustomizers different than for OpenApiCustomisers.
- #2089 - Fixed a bug that a NullPointerException is thrown when the description field of
  RequestBody is null and there is a javadoc description.
- #2104 - OpenAPI Extensions no longer work.

## [2.0.2] - 2022-12-16

### Fixed

- #2008 - Error when com.fasterxml.jackson.module.kotlin.KotlinModule is not present in
  classpath

## [2.0.1] - 2022-12-16

### Added

- #1965 - Prevents premature initialisation of factory-beans
- #2003 - Resolve property descriptions for arrays

### Fixed

- #1957 - AdditionalModelsConverter Schema params rewriting
- #1962 - override-with-generic-response shouldn't shallow copy
- #1985 - IllegalStateException: Duplicate key when two endpoints at the same URL with
  same header exist
- #1992 - Java enumeration and Spring Converter no longer generates enum drop-down
- #2001 - Enum Collection parameter missing type info in Spring Data Rest search method
- #1961 - ContinuationObject leaks into schema

## [2.0.0] - 2022-11-24

### Added

- #1284 - Add support for Jakarta EE

### What's Changed

- Upgrade spring-boot to v3.0.0

## [2.0.0-RC2] - 2022-11-20

### Added

- #1929 - Enables no cache on not cache swagger-initializer.js
- #1922 - Check existence of superclass before accessing its name
- #1923 - Javadoc description of the @RequestPart param of multipart/form-data to the
  parameter description

### Changed

- Upgrade spring-boot to 3.0.0-RC2
- Upgrade swagger-core to 2.2.7
- Upgrade swagger-ui to 4.15.5
- #1912 - Upgrade spring-security-oauth2 to 2.5.2.RELEASE

### Fixed

- #1892 - springdoc.model-and-view-allowed enhanced
- #1901 - When @Get, using @Parameter over the method results in duplicate of the same
  parameter
- #1909 - ExceptionHandler in controller is not used by another controller
- #1904 - springdoc-openapi-webflux-ui 2.0.0-M7 + spring actuator + spring cloud crashes
  at startup
- #1911 - Wrong type for
  springdoc.swagger-ui.oauth.useBasicAuthenticationWithAccessCodeGrant configuration
  property
- #1931 - Spring Security form login only offers application/json req body type.

## [2.0.0-RC1] - 2022-10-23

### Added

- #1284 - Additional hints for native support

### Changed

- Upgrade to spring-boot 3.0.0-RC1
- Upgrade swagger-ui to 4.15.0

### Fixed

- #1901 - When @Get, using @Parameter over the method results in duplicate of the same
  parameter.
- #1892 - springdoc.model-and-view-allowed.

## [2.0.0-M7] - 2022-10-17

### Added

- #1888 - custom Summary for actuator
- #1881 - Support @Hidden annotation on REST repositories.
- #1878 - Sort request methods

### Changed

- Upgrade swagger-core to 2.2.4
- Upgrade swagger-ui to 4.14.3

### Fixed

- #1829 - Wrong schema generation on endpoint consuming multipart form data combined with
  JsonView
- #1842 - A HTTP header in multipart/form-data is handled as form item instead of header
- #1845 - Wrong "response" description with two controllers having its own
  ExceptionHandler.

## [2.0.0-M6] - 2022-10-03

## Added

- #1860 - Replace auto-configuration registration by @eikemeier
  in https://github.com/springdoc/springdoc-openapi/pull/1860
- #1814 - Allow requestBody creation for GET on openapi resource endpoint

### Fixed

- #1855 - Fixed a bug that duplicate field were get for record classes by @uc4w6c
  in https://github.com/springdoc/springdoc-openapi/pull/1855
- #1820 - Refresh the browser each time, the global header is added in duplicate.
- #523 - No documented way to handle Mono/Flux without Webflux
- #1816 - Parameters with same name but different locations can't render in UI properly

## [2.0.0-M5] - 2022-08-21

### Added

- #1805 - Default flat param object

### Changed

- upgrade swagger-ui: 4.14.0

### Fixed

- #1801 - Duplicated header values when an endpoint can be called with different headers
- #1801 - NPE for request bodies with content that has media type
  MULTIPART_FORM_DATA_VALUE
- #1793 - Query parameter part of request body in controller with MultiPartFile
- #1791 - Override-with-generic-response not working from 1.6.8 onwards
- #1799 - Polymorphism - Support JsonTypeInfo.Id.CLASS

## [2.0.0-M4] - 2022-08-15

### Added

- #1700 - Support PageableDefault#value()
- #1706 - ßAdd RouterOperationCustomizer
- #1754 - Default value and description of the page parameter when enabled
  spring.data.web.pageable.one-indexed-parameters property
- #1755 - Adjust name of the parameters page and size when set
  spring.data.web.pageable.prefix property
- #1742 - Optimize the group order problem

### Changed

- update to spring-boot 3.0.0-M4
- upgrade swagger-ui: 4.13.2
- upgrade classgraph: 4.8.149
- upgrade spring-native: 0.12.1
- upgrqde swagger-core: 2.2.2
- upgrage therapi-runtime-javadoc: 0.15.0
- #1753 - Upgrade Spring Cloud Function to 4.0.0-M3 by @JohnNiang
  in https://github.com/springdoc/springdoc-openapi/pull/1753

### Fixed

- #1684 - incorrect generic param for multi interfaces
- #1687, #1688 - kotlin ByteArray property incorrect
- #1692 - More specific bean name for objectMapperProvider
- #1684 - Incorrect generic param for multi interfaces
- #1707 - Concurrent problems when initializing multiple GroupedOpenApi parallelly
- #1690 - Expected file to be in alphabetical order.
- #1713 - ObjectMapperProvider to sort all properties.
- #1717, #1718 - javadoc of JsonUnwrapped fields not set
- #1748, #1712, Generated server url computation not cleared
- #1696 - incorrect generic param for multi interfaces
- #1749 - Update server out of cache
- #1734 - springdoc-openapi-kotlin and springdoc-openapi-common do not agree on conditions
  for auto configuration
- #1761- Exception documenting RestControllerEndpoint with PostMapping

## [2.0.0-M3] - 2022-05-27

### Added

- #1664 - Add Schema properties support in method-level @RequestBody
- #1181 - Initial OpenAPI 3.1 support
- #1651 - Ease group declaration through code or properties with actuators
- #1616 - Add global customizer and filters
- #1620 - Allow ComposedSchemas to replace non-composed so we can respect polymorphic
  links discovered in later methods
- #1579 - Updated class and method javadoc handling
- #1647 - Support for Webflux springdoc behind a proxy v2.x by @wiiitek
  in https://github.com/springdoc/springdoc-openapi/pull/1647
- upgrade classgraph: 4.8.147
- upgrade spring-native: 0.11.5
- upgrade spring-cloud-function: 3.2.4
- #1603 - Update swagger-ui path from /swaggerui to /swagger-ui when using management
  port (actuator) .
- Prefer ComposedSchemas over non-composed so that method name order doesn't prevent
  polymorphic links generating into the spec

### Changed

- Update to spring-boot 3.0.0-M3

### Fixed

- #1663 - @Schema annotation with type String and allowableValues set doesn't generate
  enum drop-down in swagger-ui after upgrading from 1.6.6 (when Spring custom converter is
  used)
- #1655 - OpenAPIService is using ObjectMapper without configured modules since SpringDoc
  1.6.7.
- #1648 - Tags with only name provided are being added to the openAPI.tags field.
- #1641 - ConcurrentModificationException when querying /v3/api-docs/{group} concurrently
  for different groups
- #1634 - Generating doc for entities with map attribute does not work
- #1633 - GroupedOpenApi.builder addOpenApiCustomiser execution order is reversed
- #1630 - Remove repeated HttpSession
- #1659 - fix oauth redirection when used in spring-native
- #1621 - Redirection to UI broken with query-config-enabled when any other boolean
  parameter is defined.
- #1617 - spring cloud stream crashes at startup.
- #1605 - spring-native NullPointerException due to missing TypeHint

## [2.0.0-M2] - 2022-04-02

### Added

- #1596 - Add title property to GroupedOpenApi class for displaying a Human readable group
  name.
- #1554 - Configurable caching of OpenAPI instances.
- #1544 - @Deprecated on controller to mark all its operations as deprecated.
- #1534 - Support custom Spring type converters.
- #1543 - Add method to retrieve server base URL and ability to 'customize' the generated
  server base URL.
- #1505 - Support swagger-ui.withCredentials property.
- #1481 - Include /oauth/token endpoint using OpenApi Swagger 1.6.5.
- #1501 - Added CSRF Session Storage config properties and html transformer.
- #1498 - Pick up exception handler in case there is no controller advice at all.

### Changed

- update to spring-boot 3.0.0-M2
- upgrade to swagger-api 2.2.0 and swagger-ui 4.10.3
- spring-native upgrade to 0.11.4
- classgraph upgrade to 4.8.143
- Update webjars-locator-core to 0.50
- add owasp dependency-check-maven plugin

### Fixed

- #1586 - Typo in Pageable sort description.
- #1570 - Remove unused dependencies.
- #1565- Missing Request Body for Write Operation Actuator Endpoints.
- #1522 - parameter of type com.querydsl.core.types.Predicate ignored when unique.
- #1556 - oauth2RedirectUrl cached unexpectedly.
- #1546 - Custom Requestmapping consumes responses.
- #1552 - resolve-schema-properties is not replacing tokens from properties file.
- #1530 - Setting displayOperationId to false does not work.
- #1525 - Null vendor extensions are excluded from Json/Yaml.
- #1469 - #1036 - CSRF header should not be sent to cross domain sites
- #1480 - Fix the problem that the inconsistent newline characters of different platforms
- #1475 - Class level @Tag overwrites method level @Operation.tags.
- #1491 - Set containingClass at MethodParameter

## [2.0.0-M1] - 2022-02-06

### Added

- #1284 - Initial support for Jakarta EE
- #1430 - support custom login processing endpoints by @lipniak
  in https://github.com/springdoc/springdoc-openapi/pull/1430
- #1429 - Allow excluding individual methods from OpenApi output by @mc1arke
  in https://github.com/springdoc/springdoc-openapi/pull/
- #1372 - Customize operation and parameters by the return value
- #1453 - Reverse proxy context path aware support for manually provided files

### Changed

- update to spring-boot 3.0.0-M1
- #1424 - Upgrade to swagger-ui 4.5.0
- #1474 - Upgrade webjars-locator-core version to 0.48

### Fixed

- #1428 - Incorrect RequestBody type on schema/ui if class implements Map.
- #1455 - Post without @RequestBody not getting all fields
- #1442 - Springdoc-openapi-webmvc-core not enought for Springdoc-openapi-native
- #1446 - Upgrade from Springdoc 1.6.3 to 1.6.4 causes issues if springdoc-openapi-common
  is on the classpath but springdoc-openapi-ui is not
- #1458 - java.lang.NoSuchMethodError:
  org.springframework.core.MethodParameter.getParameter() at /v3/api-docs request
- #1469 - #1036 - CSRF header should not be sent to cross domain sites
- #1480 - Fix the problem that the inconsistent newline characters of different platforms
- #1475 - Class level @Tag overwrites method level @Operation.tags.
- #1491 - Set containingClass at MethodParameter

## [1.6.4] - 2022-01-06

### Added

- Add support out of the box for MultipartRequest
- #1418 - Support for therapi-runtime-javadoc 0.13.0.

### Changed

- #1415 - Moving PageableDefault support to springdoc-openapi-common
- #1424 - Update classgraph dependency to 4.8.138

### Fixed

- #1407 - Fix issue number in 1.6.3 CHANGELOG
- #1421 - Actuator endpoints have 2 appended to operationId
- #1425 - Parameter 2 of method indexPageTransformer in
  org.springdoc.webmvc.ui.SwaggerConfig required a single bean, but 2 were found

## [1.6.3] - 2021-12-25

### Added

- #1299 - Add support for Spring Cloud Function Web.

### Changed

- Upgrade to swagger-core 2.1.12
- Upgrade to spring-boot 2.6.2
- Upgrade spring-native to 0.11.1
- bump classgraph version to 4.8.117

### Fixed

- #1405 - Enhance springdoc-openapi as BOM for Gradle

## [1.6.2] - 2021-12-19

### Added

- #1386, #1385 - Disable validation by default
- #1384 - Enhance swagger-ui access

### Fixed

- #1392 - Consistent OperationId generation problem with Locale
- #1394 - Getting 404 when trying to access swagger-ui of a native spring-boot app

## [1.6.1] - 2021-12-12

### Added

- #1383 - Add support for BasePathAwareController

### Fixed

- #1380 - Jhipster tests failing after upgrading from 1.5.13 to 1.6.0
- #1381 - Related to show-actuator=true with no groups

## [1.6.0] - 2021-12-12

### Added

- #1356, #1360 - Add support for caching based on Locale
- #1355 - Add support for @Tag description using i18n
- #1376 - Add Support for Spring Native 0.11
- #1365 - Make MediaType for "loginRequestBody" as "application/json"
- #1375 - Support @JsonIgnore in @ParameterObject.
- #1379 - Enable support for queryConfigEnabled. Disable reading config params from URL
  search params by default

### Changed

- Upgrade swagger-ui to 4.1.3
- Upgrade to spring-boot 2.6.1
- #1373 - Enhance Pageable config

### Fixed

- #1353 - Servers from cached OpenAPI are overwritten and customizer is skipped.
- #1364 - Incorrect configUrl due to null path prefix
- #1370 - NullPointerException on JavadocPropertyCustomizer.java:81 - Warning level

## [1.5.13] - 2021-11-30

### Added

- #1317 - Add SpEL support for default values
- #1324 - springdoc-openapi-javadoc doesn't work with @ParameterObject
- #1334 - Allow customizing / disabling PolymorphicModelConverter
- #1328 - Add support for spring-native 0.11-RC1. (remove property
  springdoc.enable-native-image-support and introduce new springdoc module)
- #1348 - Treat java.nio.charset.Charset as string type by default

### Changed

- Upgrade swagger-ui to 4.1.2
- Upgrade to spring-boot 2.6.0
- #1325 - Add tests for @ParameterObject on spring boot webflux

### Fixed

- #1320 - Fixed exception handler order
- #1331 - Endpoint parameters are translated even if they should not
- #1333 - NPEs when using @ParameterObject with custom Pageable and adding descriptions to
  fields
- #1339 - Comments are not picked up for fields in Typed data classes
- #1351 - @Tag name parameter not consistently performing lookup on properties file
- #1347 - Break circular references which disabled by default since spring boot 2.6

## [1.5.12] - 2021-10-20

### Changed

- Upgrade swagger-ui to 3.52.5
- #1282 - #1288 - #1292 - #1293 Use Optional List to inject
  RequestMappingInfoHandlerMapping.
- #1290 - Use Publisher as response wrapper to ignore instead of Mono.
- #1313 - Upgrading to io.github.classgraph:classgraph to v4.8.116
- #1296 - Update Demo URLs

### Fixed

- #1316 - Hidden controller exposes Operation annotated method
- #1289 - Fix server address customizing in case of enabled pre-loading mode
- #1274 - Objects properties order is not preserved with
  springdoc.api-docs.resolve-schema-properties = true.
- #1287 - Fix typo of PageableConverter Javadoc

## [1.5.11] - 2021-10-03

### Added

- #1232 - Get CSRF token from local storage
- #1256 - Added parent pom.xml as BOM
- #1258 - Add support for HandlerTypePredicate in spring-web.
- #1265 - adding spring support with the official jetbrains compiler plugin
- #1268 - Added I18n support.
- #38 - Derive documentation from javadoc.

### Changed

- Upgrade swagger-ui to 3.52.3
- Upgrade swagger-api to 2.1.11
- Upgrade spring-boot to 2.5.5
- #1233 - Operation ids for actuator endpoints.

### Fixed

- #1230 - Feign clients that uses SpringMVCContract appears in OpenApi like controllers
  exposed if spring-boot-starter-actuator is in classpath.
- #1215 - Bad Pageable description in Page<DumbBuzModel> description.
- #1215 - Change default behavior of pageable reoslution, outside of spring data rest.
- #1252 - Schemas for subclasses not rendered in GraalVM native image builds.
- #1261 - Removal of SpringdocRouterFunctionDsl.
- #1188 - Cannot override servers in api-docs.yaml using OpenApiCustomiser.
- #1208 - Response schema for overridden methods of ResponseEntityExceptionHandler is not
  generated.
- #1276 - Cannot referenced example in RequestBody.
- #1277 - Inconsistent casing of swagger-config causes fetch error.

## [1.5.10] - 2021-07-27

### Added

- #1197 - Add default values for parameter references.
- #1188 - Ability to override servers in api-docs.yaml using OpenApiCustomiser.
- #1163 - Add a way for knowing the springdoc-openapi made the request or not.
- #1225 - Support for spring-native 0.10.1.

### Changed

- Upgrade swagger-ui version to 3.51.1.
- Upgrade swagger-core to 2.1.10.
- Upgrade spring-boot to 2.5.2.

### Fixed

- #1171 - NullPointerException loading Swagger UI with SpringDoc 1.5.6.
- #1182 - Parameter 3 of method swaggerWelcome in org.springdoc.webflux.ui.SwaggerConfig
  required a bean of type '
  org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping' that
  could not be found.
- #1185 - multpart/form-data single paramter cannot resolve in ui.
- #1196 - RequestPart Integer Param Is shown in the Parameters parameter.

## [1.5.9] - 2021-05-23

### Added

- #1164 - Provide GraalVM native image support.
- #1155 - Add Kotlin DSL for functional routes.
- #1153 - Modify ResponseSupportConverter to resolve inner type.
- #1156 - Pick up ExceptionHandlers from any parent of a class marked as @ControllerAdvice
- #1159 - Added support for @JsonView on ExceptionHandlers methods
- #1146 - Expose @Id fields for entity with spring-data-rest/springdoc-openapi-data-rest

### Changed

- Upgrade swagger-ui version to 3.49.0
- Upgrade spring-boot to 2.5.0

## [1.5.8] - 2021-04-25

### Added

- #1142 - Prevent wrong usage of @ParameterObject for simpleTypes.
- #1142 - Enhance @RequestBody Management

### Changed

- Upgrade swagger-ui version to 3.47.1
- Upgrade swagger-core to 2.1.9
- Upgrade spring-boot to 2.4.5

### Fixed

- #1031 - Incorrect generated Server URL (always returns cached url based on first
  incoming request after app startup)

## [1.5.7] - 2021-04-09

### Added

- #1110 - Add configuration flag for tryItOutEnabled
- #1128 - Add support for Generic fields in ParameterObject are not resolved

### Changed

- Upgrade swagger-ui version to 3.46.0

### Fixed

- #1109 - webflux: contextPath not used while behind a load balancer/reverse proxy.
- #1122 - StackOverflowError when using Kotlin companion object's field
- #1125 - Multiple @SecurityScheme inside a @SecuritySchemes are not present in the
  openapi spec
- #1134 - NoClassDefFoundError: WebFluxProperties$Format.

## [1.5.6] - 2021-03-16

### Added

- #1107 - use HTTP 302 status for UI redirect
- #1104 - switch from 'redirect:' prefix to ResponseEntity for redirecting UI index
- #1085 - enable recalculating oauth2 redirect url while cache is disable

### Changed

- Upgrade swagger-ui version to: 3.45.0
- Upgrade swagger-core to v2.1.7

### Fixed

- #1081 - Spring Data Rest: RequestBody for POST, PUT, PATCH should be required.
- #1082 - Spring Data Rest: Wrong schema for request body in association controllers.
- #1083 - Spring Data Rest: No model for Paged Collection.
- #1082 - Spring Data Rest: Wrong schema for request body in association controllers.
- #1084 - Spring Data Rest: Relations to exported Repositories are handled as if they were
  embedded.
- #1095 - Spring Data Rest: Exception while generating api doc with inheritance
- #1098 - NullPointerException: Cannot invoke "java.util.Map.forEach(
  java.util.function.BiConsumer)" because "properties" is null.
- #1097 - ConversionFailedException: When accessing v3/api-docs.
- #1105 - Collection methods are added to the supported entity methods.
- #1106 - Required `multipart/form-data` parameters not reflected in generated schema.
- #1096 - Fix certain `WebConversionServiceProvider#convert` calls by supplying contextual
  information

## [1.5.5] - 2021-02-28

### Added

- #1050 - Add support for addPathPrefix with Webflux swagger-ui.
- #1059 - Support per method security requirement in RepositoryRestResource.

### Changed

- Upgrade versions: spring-boot to 2.4.3 and swagger-ui to 3.44

### Fixed

- #1068 - Spring Data Rest: Wrong response schema after POST,PUT,PATCH operations.
- #1073 - Springdocs doesn't detect header declarations in class level `@RequestMapping`s.
- #1069 - Spring Data Rest: Wrong response schema for collection relations.
- #1070 - Spring Data Rest: Collection models generated with non-HAL content.
- #1075 - QuerydslBinderCustomizer alias being removed when using
  excludeUnlistedProperties.

## [1.5.4] - 2021-02-10

### Added

- #1053 - Add @RepositoryRestController documentation generation

### Changed

- Upgrade versions: swagger-ui to 3.42.0

### Fixed

- #1051 - IllegalStateException in
  org.springdoc.core.SpringDocConfiguration$OpenApiResourceAdvice.handleNoHandlerFound
- #1047 - swaggerWelcome `WebFluxProperties' that could not be found

## [1.5.3] - 2021-01-26

### Added

- Added Support for spring.webflux.base-path
- #965,#1034 - Improve compatibility with new PathMatcher / PathPatternParser
- #1012 - Forward queryParameters to swagger-ui when redirecting
- #1020 - Change handler methods scanning strategy
- #1026 - Support for @Operation in @RepositoryRestResource Spring Data Repositories.
- #1027 - Detect automatically @Controller with @Operation.

### Changed

- Upgrade versions: spring-boot to 2.4.2 and swagger-ui to 3.40

### Fixed

- #996 - Fix NPE when using management port and spring.application.name is set
- #1004 - NPE in DataRestResponseService.findSearchReturnType
- #1015 - Default value for List/Array RequestParameter has wrong type.
- #1010 - Wrong Parameter Name with Default Sort.
- #1014 - HATOEAS Links produces a circular reference.
- #1035 - oauth2 redirect url calculated incorrectly when springdoc.swagger-ui.path=/

## [1.5.2] - 2020-12-16

### Added

- #978 - Add explode support in combination with arrayShema annotation

### Changed

- Update swagger-ui to 3.38.0

### Fixed

- #985 - StackOverflowError when using ObjectId with @ParameterObject
- #980, #968 - Functional DSL incorrect path mapping

## [1.5.1] - 2020-12-07

### Added

- #923 - Exposing swagger-ui on the management port.
- #938 - Swagger-UI Property persistAuthorization.

### Changed

- Update swagger-core to v2.1.6
- Update swagger-ui to 3.37.2

### Fixed

- #973 - Nullable meta annotations are ignored
- #971 - WebFlux functional DSL does not recognise query parameters.
- #962 - NPE within AbstractRequestService
- #948 - Swagger ui syntax highlighting configuration properties do not autocomplete.

## [1.5.0] - 2020-11-14

### Added

- #891 - Provide a WebMvc.fn / WebFlux.fn functional DSL
- #904 - Add support for placeholders for default value in @RequestParam Annotation.
- Added property for deterministic and alphabetical orderding:
  springdoc.writer-with-order-by-keys
- Removal of deprecated method: GroupedOpenApi.setGroup

### Changed

- Upgrade to Swagger-core 2.1.5
- Upgrade swagger-ui version to 3.36.2
- Upgrade to spring-boot v2.4.0

### Fixed

- #889 - fix for flaky operationIds
- #893 - Generate empty `scopes` object
- #925 - Always add all properties to include if excludeUnlistedProperties=true
- #920 - Define explicitly metadata for springdoc.swagger-ui.enabled
- #907 - Hidden controller showing up in swagger UI when springdoc.show-actuator is
  enabled
- #885 - Flaky operationIds for controller-methods with same name
- #688, #349, #545 - swagger-config not being loaded twice out of the box, thanks to the
  swagger-ui v3.36.0.

## [1.4.8] - 2020-09-27

### Added

- #878, #449 - Add options to filter on the GroupedOpenApi by consumes/produces mediaTypes
  or by header
- #866 - QuerydslPredicateOperationCustomizer exclude static fields and support
  QuerydslBindings.excludeUnlistedProperties
- #877 - Add support for disabling Syntax highlighting in Swagger-ui

### Fixed

- #876 - @ParameterObject causes NPE when combined with spring-data-rest
- #879 - Empty API docs with nested routes

## [1.4.7] - 2020-09-21

### Added

- #854 - Add pre-loading setting to load OpenAPI early

### Changed

- #852 - API component schema description incorrectly overwritten by API parameter
  description
- Upgrade supported spring-boot version to 2.3.4.RELEASE
- Upgrade swagger-ui version to v3.34.0

### Fixed

- #864 - NPE with QueryDSL configuration - SpringBoot < 2.3.2
- #861 - Wrong @ManyToOne relationship naming
- #870 - HAL representation (_embedded and _links) in definitions when application
  returning plain json
- #869 - Incorrect schema addition on header if used in Spring mapping
- #847 - spring-rest-docs api-docs endpoint 500 error using Spring Boot: 2.4.0-M2
- #872 - Javascript error occurs when I set springdoc.swagger-ui.csrf.enabled=true
- #873 - lazy initialization bug

## [1.4.6] - 2020-08-31

### Fixed

- #844 - Cannot disable Try it out button
- #845 - ClassNotFoundException: javax.servlet.Filter when using
  springdoc-openapi-security module in webflux application

### Changed

- Improve compatibility with native images.

## [1.4.5] - 2020-08-29

### Added

- #826 - Support for @Hidden on response class
- #831 - Add support for @PageableDefault
- #814 - Support of denyList, allowList, instead of black and whitelist, for Spring Data
  Commons. Change since SpringBoot 2.3.2
- #837 - Add support of swagger annotations @SecurityRequirement and @Tag on @Repository
  interface
- #827 - Make Spring Security login-endpoint automatically visible in SwaggerUI
- #798 - Support for Extending EntityModel - Spring Hateoas
- Support for spring.data.web properties and spring.data.rest.default... properties

### Changed

- upgrade swagger-ui version v3.32.5
- #829 - Improve management of abstract generic types

### Fixed

- #822 - Operation having method for each accept header generate does not always generate
  the same api-docs
- #836 - Springdoc is unable to redirect to swagger-ui/index.html with Spring Boot
  2.4.0-M2
- #792 - issues with spring data rest and @ManyToOne relationships

## [1.4.4] - 2020-08-06

### Added

- #815 - Allow swagger-ui property filter to be an empty string
- #804 - Add Support for DeferredResult
- #800 - Add support oauth2 pre-selected scope
- #786 - Generate api-docs for Custom Actuator Endpoint
- #776 - Enable CSRF support for swagger-ui: Introduce new properties under
  springdoc.swagger-ui.csrf
- Added the ability to use fully qualified name with new property: springdoc.use-fqn

### Changed

- upgrade swagger-ui version v3.31.1
- upgrade swagger-api to v2.1.4
- upgrade spring-boot version to 2.3.2.RELEASE

### Fixed

- #802 - Fix comment on api-docs.enabled in README.MD
- #791 - Unable to generate correct definition for request parameter containing JSON
- #775 - Fix spring-boot actuator endpoint parameters
- #774 - Fix Weird YAML tag in section OpenAPI spec file
- #771 - MultipleOpenApiResource issue

## [1.4.3] - 2020-07-01

### Added

- #747 - Added property to override @Deprecated Model converter
- #748 - Add support for @ExceptionHandler inside @RestController

### Changed

- upgrade swagger-ui version 3.28.0
- upgrade swagger-api to v2.1.3
- #745 - Change SwaggerIndexTransformer and SwaggerConfig design to ease customization

### Fixed

- #758 - APIResponses constructed programmatically are not correctly analyzed
- #725 - Unexpected fields in request body definition for RepresentationModel DTO

## [1.4.2] - 2020-06-25

### Changed

- Upgrade swagger-ui to 3.27.0
- Migration from maven to gradle

### Fixed

- #729 - ClassNotFoundException: DefaultedPageable
- #736 - Failed load UI behind reverse proxy (Failed to load API definition)
- #728 - Maven enforcer error
- #744 - Impossible to configure swaggerurl programmatically

## [1.4.1] - 2020-06-09

### Added

- #714 - Ability to disable swagger-ui default petstore url
- #713 - Add Support of Actuator endpoints using webflux
- #703 - Pretty print OpenApi spec

### Changed

- Upgrade swagger-ui to 3.26.0

### Fixed

- #678 - Multiple file upload Flux<FilePart> error on the swagger-ui
- #711 - Wrong ApiResponse Schema picked up in ExceptionHandlers returning void
- #688, #349, #545 - Prevent swagger-config from being loaded twice in case of no groups

## [1.4.0] - 2020-05-29

### Added

- #644 - Support for @RepositoryRestResource
- #668 - Process @Parameter annotations in method parameters as MergedAnnotations
- #674 - Support @Parameter annotation attached to @RequestPart, for several @RequestParts
- #658 - Added support for GroupedOpenApi OperationCustomizer
- #654 - Use oneOf schema for polymorphic types
- #693 - Add support for @ParameterObject with POST endpoints
- Added separate module for spring-hateoas
- Added SpringDocUtils.addHiddenRestControllers(String ...)
- Added support for wrapper types on request
- Mark GroupedOpenApi.setGroup as deprecated. Use GroupedOpenApi.group instead.

### Changed

- Upgrade swagger-ui to 3.25.4
- Upgrade to spring-boot 2.3.0.RELEASE

### Fixed

- #267 - @RequestAttribute parameter appears in the UI
- #695 - Servers OpenAPI block resets after customizing with GroupedOpenApi
- #689 - Spring-boot 1, warning about bean creation when cache disabled
- #566 - Wrong hateoas relation
- #671 - Demo URL link fixed

## [1.3.9] - 2020-05-05

### Changed

- Improved support of spring-boot-1 and older spring versions
- #647 - Register model converters only if they are not registered already

### Fixed

- #646 - Handle NPE on SpringDocAnnotationsUtils.mergeSchema

## [1.3.8] - 2020-05-04

### Added

- Introduce support of Webflux and Webmvc.fn with Functional Endpoints

### Changed

- #624 - Revert changes on MediaType.APPLICATION_JSON_VALUE

### Fixed

- #630, #641, #643, #637 - JsonMappingException on UI render

## [1.3.7] - 2020-04-30

### Added

- Make use of @Deprecated annotations for parameters and model fields

### Changed

- #624 - Revert changes on MediaType.APPLICATION_JSON_VALUE
- #568 - Improve visibility of configuration classes

### Fixed

- #626 - Springdoc with Actuator server in different port that embedded tomcat server
- #625 - Add access to ApiResponses for OperationCustomizer.

## [1.3.6] - 2020-04-28

### Changed

- Revert @ParameterObject annotation from Pageable

## [1.3.5] - 2020-04-28

### Added

- Get fields of superclass for parameter objects
- #606 - Added MonetaryAmount support, out of the box.
- #605 - Support nested parameter objects.
- #603 - Request Body can be configured as optional.
- #588 - Support of assignableTypes attribute in @ControllerAdvice.

### Changed

- Upgrade swagger-ui to 3.25.1
- Improve Pageable support
- #608 - Improve override OpenApiResource
- #591 - Changing oauth2RedirectUrl to respect relaxed binding.

### Fixed

- #624 - Change Return type of api-docs to
- #622 - Missing extension to schema property.
- #609 - ( self ref) for HAL hypermedia types.
- #610, #611 - ApiResponse DEFAULT_DESCRIPTION Improvement.
- #601 - components schema not generated.
- #592 - springdoc.cache is not part of additional-spring-configuration-metadata.json.
- #597 - Request Body for Maps not available in Swagger-UI.

## [1.3.4] - 2020-04-19

### Fixed

- #583 - Crash on startup with 1.3.3 for kotlin app not using kotlinx-coroutines-reactor.

## [1.3.3] - 2020-04-18

### Added

- #322 - Make default type of Resource as binary.
- #560 - Add support kotlin Flow as response type.
- #572 - Add support for swagger-ui-property 'urls.primaryName'.

### Changed

- #561 - remove pageable required.
- #567 - springdoc.swagger-ui.oauth.additionalQueryStringParams as map instead of list.

### Fixed

- #570 - Spring HATEOAS fixing _embedded.
- #563 - Spring 4 incompatibility using springdoc-openapi 1.2.30+. Fixes.
- #562 - Lazy Initialisation breaks WebFlux support.

## [1.3.2] - 2020-04-12

### Added

- #541 - Add support for none required fields on @ParameterObject
- Support headers without value (headers = "X-API-VERSION")

### Changed

- #544 - Improve @Parameter annotation support for header
- #156 - Added test for required param object is not marked as required
- #553 - Ignore Map type with @RequestParam(required = false) in method
- #549 - Imrpove Spring HATEOAS support

### Fixed

- #543 - Description for Pageable parameters is not displayed in swagger-ui using
  @ParameterObject.
- #555 - The response scheme is erased with the generic void type
- #545 - Random null pointer exception in 1.3.1

## [1.3.1] - 2020-04-05

### Added

- #344 - Support for dynamic groups from application.yml
- #509 - Support for Groovy metaclass/metadata
- #120 #268 #162 #119 - Support to extract parameters from parameter object using
  Springdoc annotation @ParameterObject
- #502 - Support for Subtypes to be also ignored from Controller class

### Changed

- #461 - Rename some packages to improve Java 9 Modules support
- #536 #424 - Workaround for swagger-ui, to enable layout and filter properties
- #531 - Added encoding section iswagger-apin multipart request
- Upgrade to spring-boot to 2.2.6
- Upgrade swagger-api to 2.1.2

### Fixed

- #489 - Schema mapping with inheritance
- #537 - Improve support of Generics inheritance on complex return types
- #517 - Kotlin Coroutines support corrected

## [1.3.0] - 2020-03-21

### Added

- Feature to support for property resolver on @Schema (name, title and description)
- #501 - Introduced new property springdoc.api-docs.resolve-schema-properties for property
  resolver on @Schema
- #453 - Being able to see the class within an EntityModel as a Schema.
- Introduce new property springdoc.remove-broken-reference-definitions
- #498 - Property resolver on @ApiResponse.description
- Support of @QuerydslPredicate in Rest controllers

### Changed

- Change the interface of ParameterCustomizer
- Improve isAnnotationToIgnore and isParamToIgnore
- Use MethodParameter#isOptional() instead of checking for Optional type
- #496 - Improve the support of Pageable.
- Improve compatibility with spring-boot 1, even there is EOL
  announced: https://spring.io/blog/2018/07/30/spring-boot-1-x-eol-aug-1st-2019

## [1.2.34] - 2020-03-17

### Added

- Property resolver on @operation.summary, @parameter.description and @parameter.name
- #468 - Use required attribute from spring RequestBody annotation
- #481 - Review configuration via springdoc.swagger-ui.urls
- #333 - Support for auto fill clientId and clientSecret for webflux
- Support to ignore annotations on parameter level

### Changed

- #469 - Update spring-boot-starter-parent to 2.2.5.RELEASE
- #477 - Improve compatibility of springdoc-openapi-data-rest with Spring Webflux
- #480 - Improve support of Swagger UI to use provided spec.yml
- Delegate primitive type generation to swagger-core

### Fixed

- #489 - Wrong schema mapping with inheritance. Fixes

## [1.2.33] - 2020-03-01

### Added

- #451 - Support for OpenApiBuilderCustomiser to allow users to customize the
  OpenApiBuilder
- Support for generic controller types parameters
- #454 - Support to handle prefix for webflux-ui

### Changed

- #459 - Allow @Schema annotation to set an attribute as not required even if it's
  annotated with @NotNull
- #458 - Make SpringDocAnnotationsUtils public
- #455 - Make GenericResponseBuilder.calculateSchema method public

## [1.2.32] - 2020-02-19

### Added

- #429 - Support to override generic responses from controller advice with ApiResponses
- #443 - Support for non-nullable types in Kotlin
- #442 - Support for setting default produces/consumes mediaTypes

### Changed

- #447 - Force classgraph version

### Fixed

- #444 - Payload/Request Examples no longer generated
- #441 - Return value of PropertyCustomizer is ignored

## [1.2.31] - 2020-02-18

### Added

- Allow Request paremeters types to be ignored programatically using SpringDocUtils
- #420 - Support to disable autotagging of @RestController Classes
- #404 - Support for properties to exclude packages and paths from documentation.
- #414 - Support for kotlin Deprecated
- #423 - Support for Hiding org.springframework.security.core.Authentication on
  ServerHttpResponse

### Changed

- Upgrade swagger-ui to 3.25.0
- #428 - Allow ModelAndView to be detected
- #435 - Improve Reverse proxy compatibily for webflux
- #418 - Adjusting the output for Links

### Fixed

- #426 - Discovery of MediaType producers inconsistent with Spring MVC behaviour
- #408 - Multiple Parameter Refs throws IllegalStateException (Duplicate key)
- #401 - Solves Hateoas fields names mismatch
- #419 - @CookieValue parameter indents request body

## [1.2.30] - 2020-02-03

### Added

- #378- Support REST Controllers with default empty @RequestMapping
- #398 - Support for Sort GroupedOpenApi by name

### Changed

- #396 - Improve support of @Parameter Schema, in case of @RequestBody
- #393 - Prevent swagger-ui default validation
- #384 - @SecurityScheme `paramName` not accounted for the `name`
- #437 - Warning on referenced example

### Fixed

- #399 - Same operationId for overloaded methods using Groups, breaks swagger-ui
  collapsibles
- #436 - Duplicate tags if they are defined on @Operation annotation on swagger-ui.
- #440 - When using RestControllerAdvice, one of the responses description is missing and
  replaced with a default

## [1.2.29] - 2020-01-27

### Added

- #368 - Support for extensions on @ApiResponse
- #370 - Support for CompletionStage as endpoint return type
- #368 - Support for extensions on @ApiResponse
- #375 - Support for java 8 Optional for @RequestParam
- #377 - Support for @RequestParam for file upload
- #259 - Added ability to disable security for one operation using @SecurityRequirements
- #376 - Support to configure packages-to-scan as list using YAML Syntax

## [1.2.28] - 2020-01-22

### Changed

- Upgrade to spring-boot to 2.2.4.RELEASE
- Upgrade swagger-api to 2.1.1
- Upgrade swagger-ui to 3.24.3
- #359 - Make spring-security-oauth2 as optional for springdoc-openapi-security
- #354 - Improve support of response of responses overloading

## [1.2.27] - 2020-01-21

#### Added

- #338 - Support for pathsToMatch and packagesToScan to work in spinal-case as well
- #327 - Support support for spring-security-oauth2 authorization server
- #333 - Support for custom OAuth 2.0 configuration, of the swagger-ui
- #280 - Support of HttpEntity on webflux
- #352 - Support: Example value can be specified without having to specify the schema
- #346 - Support for serving the swagger-ui from outside of the spring-boot application /
  exploed jar
- #353 - Support spring property resolver in all @Info

### Changed

- Improve actuator documentation
- #349 - Make oauth2RedirectUrl dynamically calculated

### Fixed

- #339 - Fixes springdoc.swagger-ui.url property
- #351 - Allow webFlux handlers using kotlin coroutines to produce response documentation

## [1.2.26] - 2020-01-10

#### Added

- #331 - Make springdoc cache configurable

#### Fixed

- #334 - Multiple paths in controller and DeleteMapping generates incorrect Request Body

## [1.2.25] - 2020-01-08

#### Fixed

- #324 - Fix double registration of model converters when grouped api is used

#### Added

- #329 - Added support for multiple OpenAPI definitions in spring webflux

## [1.2.24] - 2020-01-05

#### Changed

- #321 - Improve beans loading, if projects uses the UI only

## [1.2.23] - 2020-01-05

#### Added

- #292 - Added migration guide from springfox
- #315 - Added support for @JsonViews with spring @requestbody annotation
- #320 - Provide oauth2RedirectUrl, if not declared

#### Fixed

- #312 - Wrong server url with when grouped api name contains special charater

## [1.2.22] - 2020-01-01

#### Added

- New annotation @PageableAsQueryParam for better Pageable support
- #309 - Feature to serve from the same swagger-ui both, REST APIs that are deployed
  within the same application and external REST APIs
- #293 - Customizing operations, parameters and properties available through an SPI

#### Changed

- #299 - Autoconfigurations will not be loaded if classpath does not contain mvc/reactive
  dependencies or application is not web application

#### Fixed

- #311 - Corrected the support of oauth2-redirect.html and oauth2RedirectUrl

## [1.2.21] - 2019-12-25

#### Added

- #297 - Springdoc, Callable support

### Removed

- #296 - Property `springdoc.api-docs.groups.enabled` removed, as not needed any more for
  enabling multiple OpenAPI definitions support

#### Changed

- #290 #294 - Improve springdoc-openapi beans loading

## [1.2.20] - 2019-12-23

#### Changed

- #289 - Preserve order of @Parameters for spring-boot 2.2

### Removed

- #236 #150 - Remove @EnableWebMvc from SwaggerConfig (not needed for Spring Boot)

## [1.2.19] - 2019-12-22

#### Added

- #213 - Support for Multiple OpenAPI definitions in one Spring Boot
- #262 - Support for spring-context-indexer
- #231 - Added support using properties from application.yml for description field in
  swagger-annotations
- #281 - Make validatorUrl configurable and fix overwrites
- #280 - Improve support of HttpEntity

#### Changed

- #270 - Ignore ServerHttpRequest and ServerHttpResponse Webflux
- #274 - Preserve order of parameters in @Parameters annotation
- #275 - Changed the default value if consumes is missing, to
  MediaType.APPLICATION_JSON_VALUE
- #286 - Renamed actuator property to springdoc.show-actuator

#### Fixed

- #246 - Static content no longer delivered

## [1.2.18] - 2019-12-14

#### Added

- #228 - Globally exclude params for webflux
- #255 - Added ability to ignore param with @hidden annotation at class level
- #238 - Support of spring.mvc.servlet.path
- #245 - ignore Authentication in controller params
- #240 - Support for oauth2RedirectUrl
- #260 - Support of @Hidden at class level.
- #231 - Ability for using properties from application.yml to declare security urls: (
  openIdConnectUrl - authorizationUrl - refreshUrl - tokenUrl)
- #241 - Added support of annotation @Parameters (without @Operation)

#### Changed

- #239 - Downgrade swagger-ui to 3.24.0

#### Fixed

- #248 - Fixes error with JDK 11 + Kotlin

## [1.2.17] - 2019-12-05

#### Added

- #219 - Handle multiple endpoints on @GetParam

#### Changed

- Upgrade Springboot to 2.2.1
- Upgrade Swagger UI to 3.24.3
- Upgrade webjars locator to 0.38

## [1.2.16] - 2019-12-04

#### Added

- #208 - Add Spring Boot metadata for config properties.
- #210 - Explicitly set which packages/ paths to scan
- #214 - Disable the try it out button

#### Changed

- #209 - Changed getOpenApi in AbstractOpenApiResource to synchronized

#### Fixed

- #212 - Missing Response Content

## [1.2.15] - 2019-11-30

#### Added

- #198 - Ignore @AuthenticationPrincipal from spring-security

## [1.2.14] - 2019-11-30

#### Added

- Add operationsSorter and tagsSorter configuration.

#### Changed

- #195 - Flux<> implies array structure
- #202 - Improve support of ArraySchema in @Parameter

### Removed

- #207 - Removed default description on RequestBody annotation

#### Fixed

- #206 - Duplicated mapping key. Random ConcurrentModificationException error

## [1.2.13] - 2019-11-28

#### Added

- #192 - Add Ability to Configure Swagger UI - The support of the swagger official
  properties
- #185 - Support of @Parameters on controller or interface level

## [1.2.12] - 2019-11-27

#### Added

- #191 - Disable/enable Swagger-UI generation based on env variable

#### Changed

- #189 - Update README.md

## [1.2.11] - 2019-11-24

#### Added

- #180 - Use `@Deprecated` annotation to mark API operation as deprecated
- #177 - Support for mappring Pageable of spring-data to correct URL-Parameter in Swagger
  UI

## [1.2.10] - 2019-11-22

#### Added

- Make webjars prefix configurable
- Generate a default tag name if no tag specified
- #172 - Support for Kotlin Coroutines added

#### Changed

- #167 - update swagger api version to 2.0.10

## [1.2.9] - 2019-11-19

#### Changed

- project refactoring

## [1.2.8] - 2019-11-18

#### Added

- #160 - Support custom annotations of @RequestMapping
- #161 - Add @NotEmpty @NotBlank @PositiveOrZero @NegativeOrZero support

#### Changed

- #163 - Improve handling of @RequestBody Mono<Tweet>

## [1.2.7] - 2019-11-18

#### Added

- #148 - Support @Schema(hidden = true) on @Parameter

## [1.2.6] - 2019-11-10

#### Changed

- project refactoring

## [1.2.5] - 2019-11-10

#### Added

- #145 - Support of RequestMapping with Regex
- Open base classes for subclassing outside of package

#### Fixed

- #147 - Improve handling interface methods

## [1.2.4] - 2019-11-07

#### Changed

- #144 - Improve Media Type management, on ControllerAdvice method with explicit
  ApiResponse annotation
- #143 - Using @Content on @ApiResponse, will ensure that no content will be generated on
  the OpenAPI yml/json description.

#### Fixed

- #142 - Error on calling GET /v3/api-docs

## [1.2.3] - 2019-11-03

#### Added

- #138 - Support for @Controller class with @ResponseBody annotation

## [1.2.2] - 2019-11-02

#### Changed

- project refactoring

## [1.2.1] - 2019-11-02

#### Added

- upgrade to spring-boot 2.2.0.RELEASE
- #136 - Open up for customisation: Change signatures on some methods in
  AbstractParameterBuilder
- #107 - Add support for @JsonView annotations in Spring MVC APIs

## [1.2.0] - 2019-10-27

#### Added

- rename module springdoc-openapi-core to springdoc-openapi-webmvc-core
- #127 - Missing Header parameters from @Operation annotation
- #55 - Ui with multiple file @RequestPart only shows last part

## [1.1.49] - 2019-10-22

#### Changed

- Upgrade swagger-api from 2.0.9 to 2.0.10
- Upgrade swagger-ui from 3.23.5 to 3.24.0

## [1.1.48] - 2019-10-15

#### Changed

- #115- Enable strict JSON output checking in tests

## [1.1.47] - 2019-10-15

#### Added

- Make ignoring parameters in AbstractRequestBuilder easier to extend
- Do not ignore PathVariable parameters, they are all time mandatory
- Extend search for @ApiResponse annotations
- #114 - Ability to generate operation responses that reference a global reusable response
  component

## [1.1.46] - 2019-10-11

#### Changed

- #106 - Imporove support of `oneOf` Response schemas: merge will be based on content
  element inside @ApiResponse annotation only

#### Added

- @SecurityRequirement at Operation and class level

## [1.1.45] - 2019-10-03

#### Changed

- Project refactoring

## [1.1.44] - 2019-09-29

#### Added

- #99 - Added sample tests for Swagger UI
- #95 - Support of Kotlin List of MultipartFile

#### Changed

- #96 - Imporive inconsistency of generated operationId in /v3/api-docs
- #98 - Change behaviour to not overwrite an existing common schema.

## [1.1.43] - 2019-09-24

#### Added

- New Feature: OpenAPICustomiser
- #92 - Added Custom converter to handle IllegalArgumentException at
  com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder.getSetter() #94

## [1.1.42] - 2019-09-23

#### Fixed

- #90 - Error in AbstractResponseBuilder.Schema calculateSchema(Components components,
  ParameterizedType parameterizedType)

## [1.1.41] - 2019-09-21

#### Changed

- Project refactoring

## [1.1.40] - 2019-09-21

#### Changed

- Imporove support of overloaded methods in the same Rest Controller

## [1.1.39] - 2019-09-15

#### Added

- Added specefic tag for spring-boot-actuator endpoints

## [1.1.38] - 2019-09-15

#### Added

- #88 - Support of spring-boot-actuator endpoints to swagger-ui

## [1.1.37] - 2019-09-06

#### Changed

- Update README.md

#### Fixed

- #84 - Regression between 1.1.33 and 1.1.34

## [1.1.36] - 2019-09-04

#### Added

- #81 - Make @Hidden work on classlevel of @RestControllerAdvice

#### Fixed

- #76 - ClassCastException in org.springdoc.core.AbstractResponseBuilder#calculateSchema

## [1.1.35] - 2019-09-04

#### Changed

- Project refactoring

## [1.1.34] - 2019-09-04

#### Added

- #72 - Query parameter with defaultValue specified will not be marked as required
- #74 - Added Support for callbacks
- WebFlux Multipart File Upload
- #75 - Support for FilePart

#### Fixed

- #79 - Error in version 1.1.27
- #80 - requestBody content is empty when using @RequestMapping annotation but is
  populated for @PostMapping

## [1.1.33] - 2019-09-01

#### Changed

- Project refactoring

## [1.1.32] - 2019-08-30

#### Fixed

- #70 - Regression between 1.1.25 and 1.1.26

## [1.1.31] - 2019-08-28

#### Changed

- Added more tests
- #68 - Spring ResponseEntity shoudl not return empty MediaType for no-body responses

## [1.1.30] - 2019-08-27

#### Fixed

- #62 - Operation.requestBody.content[0].mediaType is ignored

## [1.1.29] - 2019-08-26

#### Added

- #61 - Support of schema.example for string/date-time

## [1.1.28] - 2019-08-26

#### Changed

- project refactoring

## [1.1.27] - 2019-08-26

#### Added

- #55 - View on the Swagger-ui multiple file @RequestPart

## [1.1.26] - 2019-08-25

#### Added

- #12 - Support beans as parameter in @GetMapping / components empty

#### Changed

- #53 - Improve Generic (error) responses built from `ControllerAdvice`
- #59 - Parameter documentation overwritten by schema calculation based on type

## [1.1.25] - 2019-08-23

#### Added

- #57 - Ignore HttpServletRequest and HttpServletResponse params
- #46 - HTTP status codes in responses not according to spec
- better support for global parameters
- Support of @Hidden annotation for ControllerAdvice exception handlers

#### Changed

- #51 - Do not override parameter.schema

## [1.1.24] - 2019-08-15

#### Changed

- project refactoring

## [1.1.23] - 2019-08-15

#### Changed

- project refactoring

## [1.1.22] - 2019-08-15

#### Added

- #40 - A Controller method that does not return a response body will not document a
  schema
- Make sure the swagger-ui.path of the initial html page is the same for other swagger-ui
  requests

## [1.1.21] - 2019-08-15

#### Added

- #35 - Allow to overwrite default API response

#### Fixed

- #34 - Exception in case of parametrized types inside ReponseEntity

## [1.1.20] - 2019-08-14

#### Changed

- project refactoring

## [1.1.19] - 2019-08-14

#### Fixed

- #36 - Attempting to add @SecurityScheme to annotation results in a NPE.

## [1.1.18] - 2019-08-14

#### Added

- #33 - Support the io.swagger.v3.oas.annotations.security.SecurityScheme annotation
- #32 - Support the io.swagger.v3.oas.annotations.Hidden annotation to exclude from
  swagger docs

#### Changed

- update README

## [1.1.17] - 2019-08-12

#### Changed

- project refactoring

## [1.1.16] - 2019-08-12

#### Added

- #16 - Support hiding of Schema and Example Value

## [1.1.15] - 2019-08-12

#### Added

- Add property that helps disable springdoc-openapi endpoints.

## [1.1.14] - 2019-08-11

#### Changed

- project refactoring

## [1.1.13] - 2019-08-10

#### Added

- Add server url on webflux

## [1.1.12] - 2019-08-10

#### Changed

- project refactoring

## [1.1.11] - 2019-08-09

#### Changed

- project refactoring

## [1.1.10] - 2019-08-09

#### Changed

- project refactoring

## [1.1.9] - 2019-08-09

#### Added

- #28 - Load components from OpenAPI bean config
- Support handling @requestbody annotation directly at parameter level

## [1.1.8] - 2019-08-08

#### Added

- #20 - Detect context-path on standalone webservers

#### Changed

- #23 - Parameter will not be missing, if @parameter is used without name.

## [1.1.7] - 2019-08-07

#### Added

- #21 - Support of @javax.validation.Size specs with (maximum instead of maxLength)
- Any @GetMapping parameters should be marked as required, even if @RequestParam missing
  #14
- #17 - Handling @parameter in @operation with proper schema

## [1.1.6] - 2019-08-02

#### Changed

- project refactoring

## [1.1.5] - 2019-08-01

#### Added

- #1 - Support MultipartFile schema in UI

## [1.1.4] - 2019-08-01

#### Changed

- project refactoring

## [1.1.3] - 2019-07-31

#### Added

- #8 - Support Annotations from interfaces
- #10 - oneOf response implementation
- #3 - Support Spring Boot WebFlux Netty

#### Changed

- #9 - Complete parameter types list to be excluded

## [1.1.2] - 2019-07-30

#### Added

- #4 - Allow to customize OpenAPI object programmatically

## [1.1.1] - 2019-07-27

#### Fixed

- #2 - context-path is not respected when using Swagger UI

## [1.1.0] - 2019-07-25

#### Changed

- update README.md

## [1.0.1] - 2019-07-24

#### Added

- Added demo applications, sample code

## [1.0.0] - 2019-07-23

#### Added

- First release of springdoc-openapi, that supports OpenAPI 3

## [0.0.14] - 2019-07-21

#### Added

- Experimental release

