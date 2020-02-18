# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.31] - 2020-02-18
## Added
- Allow Request paremeters types to be ignored programatically
- Added support to disable autotagging of @RestController Classes. Fixes #420.
- Multiple Parameter Refs throws IllegalStateException (Duplicate key). Fixes #408
- Support for properties to exclude packages and paths from documentation. Fixes #404
- Support for kotlin. Deprecated. Fixes #414
- Add Suppot for Hiding org.springframework.security.core.Authentication on ServerHttpResponse. Fixes #423.
## Changed
- @CookieValue parameter indents request body. Fixes #419
- Solves Hateoas fields names mismatch. Fixes #401
- Fixes #418 by adjusting the output for Links.
- Upgrade swagger-ui to 3.25.0
- Allow ModelAndView to be detected. Fixes #428.
## Fixed
- Discovery of MediaType producers inconsistent with Spring MVC behaviour. Fixes #426
- Improve Reverse proxy compatibily for webflux. Fixes #435
## [1.2.30] - 2020-02-03
## Added
- Support REST Controllers with default empty @RequestMapping #378
- Support for Sort GroupedOpenApi by name #398
## Changed
- Improve support of @Parameter Schema, in case of @RequestBody #396
- Prevent swagger-ui default validation #393
- @SecurityScheme `paramName` not accounted for the `name` #384
- Warning on referenced example. Fixes #437
## Fixed
- Same operationId for overloaded methods using Groups, breaks swagger-ui collapsibles #399
- Duplicate tags if they are defined on @Operation annotation on swagger-ui. Fixes #436.
- When using RestControllerAdvice, one of the responses description is missing and replaced with a default. Fixes #440
## [1.2.29] - 2020-01-27
## Added
- Support for extensions on @ApiResponse #368
- Support for CompletionStage as endpoint return type #370
- Support for extensions on @ApiResponse #368
- Support for java 8 Optional for  @RequestParam #375
- Support for @RequestParam for file upload #377
- Added ability to disable security for one operation using  @SecurityRequirements #259
- Support to configure packages-to-scan as list using YAML Syntax #376

## [1.2.28] - 2020-01-22
## Changed
- Upgrade to spring-boot to 2.2.4.RELEASE
- Upgrade swagger-api to 2.1.1
- Upgrade swagger-ui to 3.24.3
- Make spring-security-oauth2 as optional for springdoc-openapi-security #359
- Improve support of response of responses overloading #354

## [1.2.27] - 2020-01-21
### Added
- Support for pathsToMatch and packagesToScan to work in spinal-case as well #338
- Support support for spring-security-oauth2 authorization server #327
- support for custom OAuth 2.0 configuration, of the swagger-ui #333
- Support of HttpEntity on webflux #280
- Support: Example value can be specified without having to specify the schema #352
- Support for serving the swagger-ui from outside of the spring-boot application / exploed jar #346
- Support spring property resolver in all @Info #353
## Changed
- Improve actuator documentation
- Make oauth2RedirectUrl dynamically calculated #349
## Fixed
- Fixes springdoc.swagger-ui.url property #339
- Allow webFlux handlers using kotlin coroutines to produce response documentation #351

## [1.2.26] - 2020-01-10
### Added
- Make springdoc cache configurable #331
### Fixed
- Multiple paths in controller and DeleteMapping generates incorrect Request Body #334

## [1.2.25] - 2020-01-08
### Fixed
- Fix double registration of model converters when grouped api is used #324
### Added
- Added support for multiple OpenAPI definitions in spring webflux #329

## [1.2.24] - 2020-01-05
### Changed
- Improve beans loading, if projects uses the UI only #321

## [1.2.23] - 2020-01-05
### Added
- Added migration guide from springfox #292
- Added support for @JsonViews with spring @requestbody annotation #315
- Provide oauth2RedirectUrl, if not declared #320
### Fixed
- Wrong server url with when grouped api name contains special charater #312

## [1.2.22] - 2020-01-01
### Added
- New annotation @PageableAsQueryParam for better Pageable support
- Feature to serve from the same swagger-ui both, REST APIs that are deployed within the same application and external REST APIs  #309
- Customizing operations, parameters and properties available through an SPI #293
### Changed
-  Autoconfigurations will not be loaded if classpath does not contain mvc/reactive dependencies or application is not web application #299
### Fixed
- Corrected the support of oauth2-redirect.html and oauth2RedirectUrl #311

  
## [1.2.21] - 2019-12-25
### Added
- Springdoc, Callable support #297
### Removed
- Property `springdoc.api-docs.groups.enabled` removed, as not needed any more for enabling multiple OpenAPI definitions support #296
### Changed 
- Improve springdoc-openapi beans loading #290 #294

## [1.2.20] - 2019-12-23
### Changed 
- Preserve order of @Parameters for spring-boot 2.2 #289
### Removed
- Remove @EnableWebMvc from SwaggerConfig (not needed for Spring Boot) #236 #150

## [1.2.19] - 2019-12-22
### Added
- Support for Multiple OpenAPI definitions in one Spring Boot #213
- Support for spring-context-indexer #262
- Added support using properties from application.yml for description field in swagger-annotations #231
- Make validatorUrl configurable and fix overwrites #281
- Improve support of HttpEntity. fixes #280
### Changed 
- Ignore ServerHttpRequest and ServerHttpResponse Webflux #270
- Preserve order of parameters in @Parameters annotation #274
- Changed the default value if consumes is missing, to MediaType.APPLICATION_JSON_VALUE #275
- Renamed actuator property to springdoc.show-actuator #286
### Fixed
- Static content no longer delivered #246

## [1.2.18] - 2019-12-14
### Added
- Globally exclude params for webflux #228
- Added ability to ignore param with @hidden annotation at class level #255
- Support of spring.mvc.servlet.path #238
- ignore Authentication in controller params #245
- Support for oauth2RedirectUrl  #240
- Support of @Hidden at class level. #260
- Ability for using properties from application.yml to declare security urls: (openIdConnectUrl - authorizationUrl - refreshUrl - tokenUrl) #231
- Added support of annotation @Parameters (without @Operation) #241
### Changed 
- Downgrade swagger-ui to 3.24.0, fixes #239
### Fixed
- Fixes error with JDK 11 + Kotlin #248

## [1.2.17] - 2019-12-05
### Added
- Handle multiple endpoints on @GetParam #219
### Changed
- Upgrade Springboot to 2.2.1
- Upgrade Swagger UI to 3.24.3
- Upgrade webjars locator to 0.38

## [1.2.16] - 2019-12-04
### Added
- Add Spring Boot metadata for config properties. #208
- Explicitly set which packages/ paths to scan #210
- Disable the try it out button #214
### Changed
- Changed getOpenApi in AbstractOpenApiResource to synchronized #209
### Fixed
- Missing Response Content - #212

## [1.2.15] - 2019-11-30
### Added
- Ignore @AuthenticationPrincipal from spring-security #198

## [1.2.14] - 2019-11-30
### Added
- Add operationsSorter and tagsSorter configuration.
### Changed
- Flux<> implies array structure #195
- Improve support of ArraySchema in @Parameter #202
### Removed
- Removed default description on RequestBody annotation #207
### Fixed
- Duplicated mapping key. Random ConcurrentModificationException error #206

## [1.2.13] - 2019-11-28
### Added
- Add Ability to Configure Swagger UI - The support of the swagger official properties #192
- Support of @Parameters on controller or interface level #185

## [1.2.12] - 2019-11-27
### Added
- Disable/enable Swagger-UI generation based on env variable #191
### Changed
- Update README.md #189

## [1.2.11] - 2019-11-24
### Added
- Use `@Deprecated` annotation to mark API operation as deprecated #180
- Support for mappring Pageable of spring-data to correct URL-Parameter in Swagger UI #177

## [1.2.10] - 2019-11-22
### Added
- Make webjars prefix configurable
- Generate a default tag name if no tag specified
- Support for Kotlin Coroutines added #172
### Changed
- update swagger api version to 2.0.10 #167

## [1.2.9] - 2019-11-19
### Changed
- project refactoring

## [1.2.8] - 2019-11-18
### Added
- Support custom annotations of @RequestMapping #160
- Add @NotEmpty @NotBlank @PositiveOrZero @NegativeOrZero support #161

### Changed
- Improve handling of @RequestBody Mono<Tweet>  #163

## [1.2.7] - 2019-11-18
### Added
- Support @Schema(hidden = true) on @Parameter #148

## [1.2.6] - 2019-11-10
### Changed
- project refactoring

## [1.2.5] - 2019-11-10
### Added
- Support of RequestMapping with Regex #145
- Open base classes for subclassing outside of package
### Fixed
- Improve handling interface methods #147

## [1.2.4] - 2019-11-07
### Changed
- Improve Media Type management, on ControllerAdvice method with explicit ApiResponse annotation #144
- Using @Content on @ApiResponse, will ensure that no content will be generated on the OpenAPI yml/json description. #143

### Fixed
- Error on calling GET /v3/api-docs #142

## [1.2.3] - 2019-11-03
### Added
- Support for @Controller class with @ResponseBody annotation #138

## [1.2.2] - 2019-11-02
### Changed
- project refactoring

## [1.2.1] - 2019-11-02
### Added
- upgrade to spring-boot 2.2.0.RELEASE
- Open up for customisation #136: Change signatures on some methods in AbstractParameterBuilder 
- Add support for @JsonView annotations in Spring MVC APIs #107

## [1.2.0] - 2019-10-27
### Added
- rename module springdoc-openapi-core to springdoc-openapi-webmvc-core
- Missing Header parameters from @Operation annotation #127
- Ui with multiple file @RequestPart only shows last part #55

## [1.1.49] - 2019-10-22
### Changed
- Upgrade swagger-api from 2.0.9 to 2.0.10
- Upgrade swagger-ui from 3.23.5 to 3.24.0

## [1.1.48] - 2019-10-15
### Changed
- Enable strict JSON output checking in tests #115

## [1.1.47] - 2019-10-15
### Added
- Make ignoring parameters in AbstractRequestBuilder easier to extend
- Do not ignore PathVariable parameters, they are all time mandatory
- Extend search for @ApiResponse annotations
- Ability to generate operation responses that reference a global reusable response component #114

## [1.1.46] - 2019-10-11
### Changed
- Imporove support of `oneOf` Response schemas: merge will be based on content element inside @ApiResponse annotation only  #106
### Added
- @SecurityRequirement at Operation and class level

## [1.1.45] - 2019-10-03
### Changed
- Project refactoring

## [1.1.44] - 2019-09-29
### Added
- Added sample tests for Swagger UI #99
- Support of Kotlin List of MultipartFile #95
### Changed
- Imporive inconsistency of generated operationId in /v3/api-docs #96
- Change behaviour to not overwrite an existing common schema. #98

## [1.1.43] - 2019-09-24
### Added
- New Feature: OpenAPICustomiser #92
- Added Custom converter to handle IllegalArgumentException at com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder.getSetter() #94

## [1.1.42] - 2019-09-23
### Fixed
- Error in AbstractResponseBuilder.Schema calculateSchema(Components components, ParameterizedType parameterizedType) #90

## [1.1.41] - 2019-09-21
### Changed
- Project refactoring

## [1.1.40] - 2019-09-21
### Changed
-  Imporove support of overloaded methods in the same Rest Controller

## [1.1.39] - 2019-09-15
### Added
- Added specefic tag for spring-boot-actuator endpoints

## [1.1.38] - 2019-09-15
### Added
- Support of spring-boot-actuator endpoints to swagger-ui #88

## [1.1.37] - 2019-09-06
### Changed
- Update README.md

### Fixed
- Regression between 1.1.33 and 1.1.34 #84

## [1.1.36] - 2019-09-04
### Added
- Make @Hidden work on classlevel of @RestControllerAdvice #81
### Fixed
- ClassCastException in org.springdoc.core.AbstractResponseBuilder#calculateSchema #76

## [1.1.35] - 2019-09-04
### Changed
- Project refactoring

## [1.1.34] - 2019-09-04
### Added
- Query parameter with defaultValue specified will not be marked as required #72
- Added Support for callbacks #74
- WebFlux Multipart File Upload - Support for FilePart #75
### Fixed
- Error in version 1.1.27 #79
- requestBody content is empty when using @RequestMapping annotation but is populated for @PostMapping #80

## [1.1.33] - 2019-09-01
### Changed
- Project refactoring

## [1.1.32] - 2019-08-30
### Fixed
- Regression between 1.1.25 and 1.1.26 #70

## [1.1.31] - 2019-08-28
### Changed
- Added more tests
- Spring ResponseEntity shoudl not return empty MediaType for no-body responses #68

## [1.1.30] - 2019-08-27
### Fixed
- Operation.requestBody.content[0].mediaType is ignored #62

## [1.1.29] - 2019-08-26
### Added
- Support of schema.example for string/date-time #61

## [1.1.28] - 2019-08-26
### Changed
- project refactoring

## [1.1.27] - 2019-08-26
### Added
- View on the Swagger-ui multiple file @RequestPart #55

## [1.1.26] - 2019-08-25
### Added
- Support beans as parameter in @GetMapping / components empty #12
### Changed 
- Improve Generic (error) responses built from `ControllerAdvice` #53
- Parameter documentation overwritten by schema calculation based on type #59

## [1.1.25] - 2019-08-23
### Added
- Ignore HttpServletRequest and HttpServletResponse params #57
- HTTP status codes in responses not according to spec #46
- better support for global parameters
- Support of @Hidden annotation for ControllerAdvice exception handlers
### Changed
- Do not override parameter.schema #51

## [1.1.24] - 2019-08-15
### Changed
- project refactoring

## [1.1.23] - 2019-08-15
### Changed
- project refactoring

## [1.1.22] - 2019-08-15
### Added
- A Controller method that does not return a response body will not document a schema #40
- Make sure the swagger-ui.path of the initial html page is the same for other swagger-ui requests

## [1.1.21] - 2019-08-15
### Added
- Allow to overwrite default API response #35
### Fixed
- Exception in case of parametrized types inside ReponseEntity #34

## [1.1.20] - 2019-08-14
### Changed
- project refactoring

## [1.1.19] - 2019-08-14
### Fixed
- Attempting to add @SecurityScheme to annotation results in a NPE. #36

## [1.1.18] - 2019-08-14
### Added
- Support the io.swagger.v3.oas.annotations.security.SecurityScheme annotation #33
- Support the io.swagger.v3.oas.annotations.Hidden annotation to exclude from swagger docs #32 
### Changed
- update README

## [1.1.17] - 2019-08-12
### Changed
- project refactoring

## [1.1.16] - 2019-08-12
### Added
- Support hiding of Schema and Example Value #16

## [1.1.15] - 2019-08-12
### Added
- Add property that helps disable springdoc-openapi endpoints.

## [1.1.14] - 2019-08-11
### Changed
- project refactoring

## [1.1.13] - 2019-08-10
### Added
- Add server url on webflux

## [1.1.12] - 2019-08-10
### Changed
- project refactoring

## [1.1.11] - 2019-08-09
### Changed
- project refactoring

## [1.1.10] - 2019-08-09
### Changed
- project refactoring

## [1.1.9] - 2019-08-09
### Added
- Load components from OpenAPI bean config #28
- Load components from OpenAPI bean config
- Support handling @requestbody annotation directly at parameter level

## [1.1.8] - 2019-08-08
### Added
- Detect context-path on standalone webservers #20
### Changed
- Parameter will not be missing, if @parameter is used without name. #23

## [1.1.7] - 2019-08-07
### Added
- Support of @javax.validation.Size specs with (maximum instead of maxLength) #21
- Any @GetMapping parameters should be marked as required, even if @RequestParam missing #14
- Handling @parameter in @operation with proper schema #17

## [1.1.6] - 2019-08-02
### Changed
- project refactoring

## [1.1.5] - 2019-08-01
### Added
- Support MultipartFile schema in UI #1

## [1.1.4] - 2019-08-01
### Changed
- project refactoring

## [1.1.3] - 2019-07-31
### Added
- support Annotations from interfaces #8
- oneOf response implementation #10
- Support Spring Boot WebFlux Netty #3
### Changed
- Complete parameter types list to be excluded #9

## [1.1.2] - 2019-07-30
### Added
- Allow to customize OpenAPI object programmatically #4

## [1.1.1] - 2019-07-27
### Fixed
- context-path is not respected when using Swagger UI - #2

## [1.1.0] - 2019-07-25
### Changed
- update README.md

## [1.0.1] - 2019-07-24
### Added
- Added demo applications, sample code

## [1.0.0] - 2019-07-23
### Added
- First release of springdoc-openapi, that supports OpenAPI 3

## [0.0.14] - 2019-07-21
### Added
- Experimental release

