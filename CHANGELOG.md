# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.5.7] - 2021-04-09
### Added
- #1110 - Add configuration flag for tryItOutEnabled
- #1128 - Add support for Generic fields in ParameterObject are not resolved 
### Changed
- Upgrade swagger-ui version to 3.46.0
### Fixed
- #1109 - webflux: contextPath not used while behind a load balancer/reverse proxy.
- #1122 - StackOverflowError when using Kotlin companion object's field
- #1125 - Multiple @SecurityScheme inside a @SecuritySchemes are not present in the openapi spec
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
- #1084 - Spring Data Rest: Relations to exported Repositories are handled as if they were embedded. 
- #1095 - Spring Data Rest: Exception while generating api doc with inheritance
- #1098 - NullPointerException: Cannot invoke "java.util.Map.forEach(java.util.function.BiConsumer)" because "properties" is null. 
- #1097 - ConversionFailedException: When accessing v3/api-docs. 
- #1105 - Collection methods are added to the supported entity methods.  
- #1106 - Required `multipart/form-data` parameters not reflected in generated schema. 
- #1096 - Fix certain `WebConversionServiceProvider#convert` calls by supplying contextual information
 
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
- #1075 - QuerydslBinderCustomizer alias being removed when using excludeUnlistedProperties. 

## [1.5.4] - 2021-02-10
### Added
- #1053 - Add @RepositoryRestController documentation generation
### Changed
- Upgrade versions: swagger-ui to 3.42.0
### Fixed
- #1051 - IllegalStateException in org.springdoc.core.SpringDocConfiguration$OpenApiResourceAdvice.handleNoHandlerFound
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
- Added property for deterministic and alphabetical orderding: springdoc.writer-with-order-by-keys
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
- #907 - Hidden controller showing up in swagger UI when springdoc.show-actuator is enabled
- #885 - Flaky operationIds for controller-methods with same name 
- #688, #349, #545 - swagger-config not being loaded twice out of the box, thanks to the swagger-ui v3.36.0.

## [1.4.8] - 2020-09-27
### Added
- #878, #449 - Add options to filter on the GroupedOpenApi by consumes/produces mediaTypes or by header
- #866 - QuerydslPredicateOperationCustomizer exclude static fields and support QuerydslBindings.excludeUnlistedProperties
- #877 - Add support for disabling Syntax highlighting in Swagger-ui
### Fixed
- #876 - @ParameterObject causes NPE when combined with spring-data-rest
- #879 - Empty API docs with nested routes

## [1.4.7] - 2020-09-21
### Added
- #854 - Add pre-loading setting to load OpenAPI early
### Changed
- #852 - API component schema description incorrectly overwritten by API parameter description
- Upgrade supported spring-boot version to 2.3.4.RELEASE
- Upgrade swagger-ui version to v3.34.0
### Fixed
- #864 - NPE with QueryDSL configuration - SpringBoot < 2.3.2
- #861 - Wrong @ManyToOne relationship naming
- #870 - HAL representation (_embedded and _links) in definitions when application returning plain json
- #869 - Incorrect schema addition on header if used in Spring mapping
- #847 - spring-rest-docs api-docs endpoint 500 error using Spring Boot: 2.4.0-M2
- #872 - Javascript error occurs when I set springdoc.swagger-ui.csrf.enabled=true
- #873 - lazy initialization bug

## [1.4.6] - 2020-08-31
### Fixed
- #844 - Cannot disable Try it out button
- #845 - ClassNotFoundException: javax.servlet.Filter when using springdoc-openapi-security module in webflux application
### Changed
- Improve compatibility with native images.

## [1.4.5] - 2020-08-29
### Added
- #826 - Support for @Hidden on response class
- #831 - Add support for @PageableDefault
- #814 - Support of denyList, allowList, instead of black and whitelist, for Spring Data Commons. Change since SpringBoot 2.3.2
- #837 - Add support of swagger annotations @SecurityRequirement and @Tag on @Repository interface
- #827 - Make Spring Security login-endpoint automatically visible in SwaggerUI
- #798 - Support for Extending EntityModel - Spring Hateoas
- Support for spring.data.web properties and spring.data.rest.default... properties
### Changed
- upgrade swagger-ui version v3.32.5
- #829 - Improve management of abstract generic types
### Fixed
- #822 - Operation having method for each accept header generate does not always generate the same api-docs
- #836 - Springdoc is unable to redirect to swagger-ui/index.html with Spring Boot 2.4.0-M2
- #792 - issues with spring data rest and @ManyToOne relationships

## [1.4.4] - 2020-08-06
### Added
- #815 - Allow swagger-ui property filter to be an empty string
- #804 - Add Support for DeferredResult
- #800 - Add support oauth2 pre-selected scope
- #786 - Generate api-docs for Custom Actuator Endpoint
- #776 - Enable CSRF support for swagger-ui: Introduce new properties under springdoc.swagger-ui.csrf
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
- #624 -  Change Return type of api-docs to 
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
- #543 - Description for Pageable parameters is not displayed in swagger-ui using @ParameterObject.
- #555 - The response scheme is erased with the generic void type
- #545 - Random null pointer exception in 1.3.1

## [1.3.1] - 2020-04-05
### Added
- #344 - Support for dynamic groups from application.yml 
- #509 - Support for Groovy metaclass/metadata 
- #120 #268 #162 #119 - Support to extract parameters from parameter object using Springdoc annotation @ParameterObject 
- #502 - Support for Subtypes to be also ignored from Controller class 
### Changed
- #461 - Rename some packages to improve Java 9 Modules support 
- #536 #424 - Workaround for swagger-ui, to enable layout and filter properties 
- #531  - Added encoding section iswagger-apin multipart request 
- Upgrade to spring-boot to 2.2.6
- Upgrade swagger-api to 2.1.2
### Fixed
- #489 - Schema mapping with inheritance 
- #537 - Improve support of Generics inheritance on complex return types 
- #517 - Kotlin Coroutines support corrected 

## [1.3.0] - 2020-03-21
### Added
- Feature to support for property resolver on @Schema (name, title and description)
- #501 - Introduced new property springdoc.api-docs.resolve-schema-properties for property resolver on @Schema 
- #453 - Being able to see the class within an EntityModel as a Schema.  
- Introduce new property springdoc.remove-broken-reference-definitions
- #498 - Property resolver on @ApiResponse.description 
- Support of @QuerydslPredicate in Rest controllers
### Changed
- Change the interface of ParameterCustomizer
- Improve isAnnotationToIgnore and isParamToIgnore
- Use MethodParameter#isOptional() instead of checking for Optional type
- #496 - Improve the support of Pageable. 
- Improve compatibility with spring-boot 1, even there is EOL announced: https://spring.io/blog/2018/07/30/spring-boot-1-x-eol-aug-1st-2019

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
- #480 - Improve support of  Swagger UI to use provided spec.yml 
- Delegate primitive type generation to swagger-core
### Fixed
- #489 - Wrong schema mapping with inheritance. Fixes 

## [1.2.33] - 2020-03-01
### Added
- #451 - Support for OpenApiBuilderCustomiser to allow users to customize the OpenApiBuilder 
- Support for generic controller types parameters
- #454 - Support to handle prefix for webflux-ui 
### Changed
- #459 - Allow @Schema annotation to set an attribute as not required even if it's annotated with @NotNull 
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
- #423 - Support for Hiding org.springframework.security.core.Authentication on ServerHttpResponse 
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
- #399 - Same operationId for overloaded methods using Groups, breaks swagger-ui collapsibles 
- #436 - Duplicate tags if they are defined on @Operation annotation on swagger-ui.
- #440 - When using RestControllerAdvice, one of the responses description is missing and replaced with a default 
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
- #346 - Support for serving the swagger-ui from outside of the spring-boot application / exploed jar 
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
- #309 - Feature to serve from the same swagger-ui both, REST APIs that are deployed within the same application and external REST APIs 
- #293 - Customizing operations, parameters and properties available through an SPI 
#### Changed
- #299 - Autoconfigurations will not be loaded if classpath does not contain mvc/reactive dependencies or application is not web application 
#### Fixed
- #311 - Corrected the support of oauth2-redirect.html and oauth2RedirectUrl 

 
## [1.2.21] - 2019-12-25
#### Added
- #297 - Springdoc, Callable support 
### Removed
- #296 - Property `springdoc.api-docs.groups.enabled` removed, as not needed any more for enabling multiple OpenAPI definitions support 
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
- #231 - Added support using properties from application.yml for description field in swagger-annotations 
- #281 - Make validatorUrl configurable and fix overwrites 
- #280 - Improve support of HttpEntity 
#### Changed 
- #270 - Ignore ServerHttpRequest and ServerHttpResponse Webflux 
- #274 - Preserve order of parameters in @Parameters annotation 
- #275 - Changed the default value if consumes is missing, to MediaType.APPLICATION_JSON_VALUE 
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
- #231 - Ability for using properties from application.yml to declare security urls: (openIdConnectUrl - authorizationUrl - refreshUrl - tokenUrl)
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
- #192 - Add Ability to Configure Swagger UI - The support of the swagger official properties 
- #185 - Support of @Parameters on controller or interface level 

## [1.2.12] - 2019-11-27
#### Added
- #191 - Disable/enable Swagger-UI generation based on env variable 
#### Changed
- #189 - Update README.md 

## [1.2.11] - 2019-11-24
#### Added
- #180 - Use `@Deprecated` annotation to mark API operation as deprecated 
- #177 - Support for mappring Pageable of spring-data to correct URL-Parameter in Swagger UI 

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
- #144 - Improve Media Type management, on ControllerAdvice method with explicit ApiResponse annotation 
- #143 - Using @Content on @ApiResponse, will ensure that no content will be generated on the OpenAPI yml/json description. 

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
- #136 - Open up for customisation: Change signatures on some methods in AbstractParameterBuilder 
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
- #114 - Ability to generate operation responses that reference a global reusable response component 

## [1.1.46] - 2019-10-11
#### Changed
- #106 - Imporove support of `oneOf` Response schemas: merge will be based on content element inside @ApiResponse annotation only 
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
- #92 - Added Custom converter to handle IllegalArgumentException at com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder.getSetter() #94

## [1.1.42] - 2019-09-23
#### Fixed
- #90 - Error in AbstractResponseBuilder.Schema calculateSchema(Components components, ParameterizedType parameterizedType)

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
- #80 - requestBody content is empty when using @RequestMapping annotation but is populated for @PostMapping 

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
- #40 - A Controller method that does not return a response body will not document a schema 
- Make sure the swagger-ui.path of the initial html page is the same for other swagger-ui requests

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
- #32  - Support the io.swagger.v3.oas.annotations.Hidden annotation to exclude from swagger docs 
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
- Any @GetMapping parameters should be marked as required, even if @RequestParam missing #14
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

