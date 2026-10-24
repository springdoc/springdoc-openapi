# swagger-core 3.0 (Jackson 3) — springdoc-openapi integration review

This report records what porting springdoc-openapi to swagger-core 3.0 (Jackson 3, `tools.jackson`) required, the
swagger-core bugs found while running the springdoc-openapi test suite, and the swagger-core enhancements that would
make the integration simpler for downstream projects.

It's a working document for the `springdoc-openapi-jackson3` branch. Nothing has been proposed to swagger-core yet.

## Context

| | |
|---|---|
| swagger-core | branch [`3.0.0`](https://github.com/swagger-api/swagger-core/tree/3.0.0), commit `21cde346d` (2026-10-23), built locally as `3.0.0-SNAPSHOT` |
| References | [#4991](https://github.com/swagger-api/swagger-core/issues/4991), [MIGRATION.md](https://github.com/swagger-api/swagger-core/blob/3.0.0/MIGRATION.md), [Swagger 3.X Getting started](https://github.com/swagger-api/swagger-core/wiki/Swagger-3.X---Getting-started) |
| springdoc-openapi | `main` (3.2.1-SNAPSHOT), Spring Boot 4.1.2, Spring Framework 7 |
| Jackson | 3.2.3 and `jackson-annotations` 2.22, the versions of swagger-core 3.0.0 (springdoc overrides `jackson-bom.version` and `jackson-2-bom.version`; Spring Boot 4.1.2 manages 3.1.7 and 2.21) |
| JDK | 21 |

**Test results** (`./mvnw test -Pci`, Jackson 3.2.3): 1847 tests in 26 modules, **1842 pass, 5 fail**. The result
is the same on Jackson 3.1.7. All 5 failures come from
swagger-core behaviour: [B1](#b1-jsonunwrapped-on-a-record-component-leaks-a-synthetic-jsonunwrapped0-property)
(1 test) and [B2](#b2-implicit-constructor-parameters-become-extra-writeonly-schema-properties) (4 tests). Their
golden files are left unchanged on purpose.

## What springdoc-openapi had to change

1. Dependencies:
   - `swagger-core-jakarta` → `swagger-core` (the `-jakarta` artifacts are gone in 3.0);
   - `jackson-dataformat-yaml` and `jackson-module-kotlin` → `tools.jackson.*`;
   - **explicit `commons-lang3`**, which swagger-core no longer brings (see [E6](#e6-document-the-removal-of-commons-lang3)).
2. Imports across 97 main and test files: `com.fasterxml.jackson.{core,databind,dataformat,module}` → `tools.jackson.*`.
   `com.fasterxml.jackson.annotation` stays.
3. Jackson 3 API changes:
   - `JacksonException` (unchecked) instead of `JsonProcessingException`/`IOException`;
   - `ValueDeserializer`;
   - `serializationConfig()`;
   - `JsonNode.properties()` / `propertyNames()` / `asString()`;
   - `MapperBuilder.changeDefaultPropertyInclusion`;
   - `PropertyNamingStrategies.UPPER_SNAKE_CASE.nameForField(...)`, since `NamingBase.translate` is now protected;
   - `JacksonJsonHttpMessageConverter` in tests.
4. Introspector overrides (`SpringDocRequiredModule`, `SpringDocSealedClassModule`) gained the `MapperConfig<?>`
   parameter of `SwaggerAnnotationIntrospector` (see [E5](#e5-document-the-swaggerannotationintrospector-signature-changes)).
5. Bean introspection now uses Jackson internals, as swagger-core itself does (see [E3](#e3-a-public-bean-introspection-helper)).
6. `ObjectMapperProvider` rewritten. On `main`, springdoc **mutated** `Json.mapper()`/`Json31.mapper()`: it registered
   modules and mixins and toggled sort features at write time. With immutable mappers:
   - Modules that must affect schema introspection (required-mode, sealed classes, Kotlin, HAL) are registered once,
     globally, through `ObjectMapperFactory.addCustomizer`, keyed by `JacksonModule#getRegistrationId()`, and can
     be unregistered (see [E1](#e1-keyed-idempotent-and-removable-module-registration)).
   - Output-only settings are applied to **derived** mappers (`mapper.rebuild()...build()`):
     - the 3.1 `SchemaTypeMixin`;
     - `writer-with-order-by-keys` sorting;
     - YAML `USE_NATIVE_TYPE_ID` off.
   - The derived mappers are cached and rebuilt when `ObjectMapperFactory.generation()` moves.
7. Cloning the user's `OpenAPI` bean switched from `new ObjectMapper()` to
   `ObjectMapperFactory.buildStrictGenericObjectMapper()`. A plain Jackson 3 mapper silently drops `$ref` (see
   [B3](#b3-a-plain-jackson-3-mapper-silently-drops-ref-and-the-other--properties-of-swagger-models)).
8. Tests that removed Spring HATEOAS mixins from `Json.mapper()` by reflection now unregister and restore the HAL module.
9. `writer-with-order-by-keys` now sorts every property alphabetically, including `Server` (`description` before
   `url`). The `app138` golden files were updated.

Overall the `ObjectMapperFactory` customizer API made the port possible without forking any swagger-core class. The
points below are what still costs downstream projects time, or produces wrong output.

## Bugs

### B1. `@JsonUnwrapped` on a record component leaks a synthetic `@JsonUnwrapped/0` property

Jackson 3 exposes an unwrapped creator parameter as an extra, synthetic property named `@JsonUnwrapped/<index>`.
`ModelResolver` treats it as a regular bean property. The schema gets an empty `@JsonUnwrapped/0` property, which is
also added to `required` when the component carries `@Schema(requiredMode = REQUIRED)`. The problem shows with and
without `setJackson2Compatibility(true)`.

Reproducer (swagger-core only, no springdoc):

```java
public record Example(
		@JsonUnwrapped @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Wrapped unwrapped,
		@Schema(requiredMode = Schema.RequiredMode.REQUIRED) Integer number) {
	public record Wrapped(@Schema(requiredMode = Schema.RequiredMode.REQUIRED) String value) {}
}

System.out.println(Json31.pretty(ModelConverters.getInstance(true).readAll(Example.class)));
```

```json
"Example" : {
  "type" : "object",
  "properties" : {
    "value" : { "type" : "string" },
    "unwrapped" : { },
    "number" : { "type" : "integer", "format" : "int32" },
    "@JsonUnwrapped/0" : { }
  },
  "required" : [ "@JsonUnwrapped/0", "number", "unwrapped", "value" ]
}
```

- **Expected:** no `@JsonUnwrapped/0` entry in `properties` or `required`. `ModelResolver` should skip Jackson's
  synthetic unwrapped creator properties, the same way it already handles the unwrapped accessor through
  `jsonUnwrappedHandler`.
- **Pre-existing, also on swagger-core 2.x:** the empty `"unwrapped" : { }` property, and `unwrapped` in `required`,
  should not appear either, since the property is unwrapped into `value`.
- **Root cause, OpenAPI 3.1 only:** once the `jsonUnwrappedHandler` has merged the inner properties,
  `getSchemaFromAnnotation` rebuilds an empty schema from the remaining `@Schema(requiredMode = REQUIRED)`. The
  OpenAPI 3.0 output is correct. The fix is in PR 1 (see [Proposed upstream](#proposed-upstream)). With it,
  springdoc's `app237` expected file must also drop the empty `unwrapped` property it had recorded.
- **springdoc test:** `v31.app237.SpringDocApp237Test`.

### B2. Implicit constructor parameters become extra `writeOnly` schema properties

With the Jackson 3 default `DETECT_PARAMETER_NAMES=true`, a class with a single public constructor is now
deserialized through that constructor. Its parameter names show up as additional `writeOnly` properties, even when no
getter or field has that name.

```java
public class ErrorMessage {
	private String id;
	private String message;
	public ErrorMessage(String id, String message2) { this.id = id; this.message = message2; }
	public String getId() { return id; }
	public String getMessage() { return message; }
}
```

| | properties of `ErrorMessage` |
|---|---|
| swagger-core 2.x | `id`, `message` |
| swagger-core 3.0 (default) | `id`, `message`, **`message2` (`writeOnly: true`)** |
| swagger-core 3.0 with `setJackson2Compatibility(true)` | `id`, `message` |

This is consistent with how a Jackson 3 mapper reads such a class, so it may be intended. However:

- `MIGRATION.md` lists `DETECT_PARAMETER_NAMES` among the mapper defaults, but not its effect on **generated
  schemas**.
- The Getting-started wiki page says 3.0 "does not change how Swagger Core works". For code-first users, the
  generated schemas do change (this case, B1, acronym accessors, `Month`).

Requested:

- Add a "generated schema changes" section to `MIGRATION.md`, listing the cases where a model produces a different
  schema in 3.0, with this example.
- Consider letting the schema mapper's parameter-name detection be configured independently from the
  (de)serialization mappers.

springdoc tests: `v30/v31.app1.SpringDocApp1Test`, in both `springdoc-openapi-starter-webmvc-api` and
`springdoc-openapi-javadoc-tests`. The golden files are unchanged until springdoc decides whether to adopt Jackson 3
semantics or opt into Jackson 2 compatibility.

### B3. A plain Jackson 3 mapper silently drops `$ref` and the other `$` properties of swagger models

Jackson 3 no longer detects accessors whose name doesn't start with an upper-case letter after `get`/`set`, such as
`get$ref()`, `get$id()`, `get$schema()`, `get$anchor()` or `get$comment()`. swagger-core enables them on its own mappers
(`DefaultAccessorNamingStrategy.Provider().withFirstCharAcceptance(true, true)`). Any other mapper used to serialize
or copy swagger models silently loses them, without an error: for example a Spring `JsonMapper`, or `new ObjectMapper()`
in user code. On Jackson 2 the same code worked.

Found through springdoc's HATEOAS test `app9`: a user-provided `OpenAPI` bean with
`new Schema<>().$ref("#/components/schemas/Link")` came out without `$ref`, and the referenced schemas disappeared.

Requested (any of):

- Annotate the `$`-prefixed accessors of `swagger-models` with `@JsonProperty("$ref")` and so on, so that the models
  (de)serialize correctly with any Jackson 3 mapper.
- Document in `MIGRATION.md` that swagger models must be (de)serialized with a mapper from `ObjectMapperFactory`, and
  that `buildStrictGenericObjectMapper()` is the generic option.

### B4. `ModelConverters.getInstance(boolean)` lazy initialization is not thread-safe

`getInstance(boolean openapi31)` initializes the non-volatile static `SINGLETON`/`SINGLETON31` without
synchronization. The `getInstance(boolean, SchemaResolution)` overload synchronizes on `ModelConverters.class`.
Concurrent first calls can build two instances, run `init(...)` twice (service-loaded converters), and publish a
partially initialized instance.

This isn't new in 3.0, but 3.0 adds mutable state to the instance (`mapperGeneration`, the refreshed default
resolver), which raises the stakes. springdoc builds documentation lazily on the first HTTP request, often from
several threads. Requested: synchronize this overload as well, or use a holder or a volatile field.

### B5. `Json.mapper(ObjectMapper)` and `Json.configure(...)` don't move `generation()`

`MapperHolder.set` and `MapperHolder.configure` install an explicit mapper without incrementing
`ObjectMapperFactory.generation()`. Components that derive their own mappers from `Json.mapper()` and use
`generation()` to know when to rebuild can't detect this change. That is the pattern `MIGRATION.md` recommends ("implement a refresh mechanism"). Examples are springdoc's
`ObjectMapperProvider` and `ModelConverters` itself.

Also, once a mapper is installed explicitly, customizers registered later through `addCustomizer` are silently
ignored for it.

Requested:

- Increment the generation, or a per-holder version exposed publicly, on `set`/`configure`/`reset`.
- Log a debug message, or document on `addCustomizer`, that explicit mappers aren't customized.

## Enhancements

### E1. Keyed, idempotent and removable module registration

`ObjectMapperFactory.addModule(module)` wraps the module in an anonymous customizer and doesn't return it. Calling it
twice registers the module twice, and the module can never be removed.

Frameworks register modules once per application context. Spring test suites cache several contexts in one JVM, and
contexts are restarted, for example by devtools. springdoc had to keep its own `registrationId → MapperCustomizer`
map so it could register idempotently and unregister. Its HATEOAS tests need a context where HAL is disabled after
another context enabled it.

Proposed:

- Deduplicate `addModule` by `JacksonModule#getRegistrationId()`, as Jackson's `MapperBuilder.addModule` already does.
- Make `addModule`/`addCustomizer` return a handle (`AutoCloseable` or the `MapperCustomizer`).
- Add `removeModule(JacksonModule)`, matching by registration id.

### E2. Instance-scoped mapper and model-converter configuration

All customization is global and static: `ObjectMapperFactory` customizers, the Jackson 2 compatibility switch,
`Json`/`Json31` holders, `ModelConverters` singletons, and system properties such as `USE_ARBITRARY_SCHEMA_PROPERTY`.
Two applications, or two Spring contexts, in the same JVM can't have different mapper setups. One context's modules
(HAL, Kotlin, required-mode) leak into the other's generated schemas.

Proposed: let a `ModelConverters` instance be created with its own mapper configuration, for example
`ModelConverters.builder().openapi31(true).customizer(...).build()`, used by the default `ModelResolver`. Keep the
static API as the default instance. springdoc could then hold one configuration per application context and drop all
global state.

### E3. A public bean-introspection helper

Jackson 3 removed `SerializationConfig.introspect(JavaType)`. swagger-core itself calls the underscore-prefixed
internals `_serializationContext().introspectBeanDescription(...)` and
`_deserializationContext().introspectBeanDescription(...)` (`ModelResolver`, `AbstractModelConverter`). Converters
written by downstream projects need the same (springdoc's `PolymorphicModelConverter`, `JsonNullableSupportConverter`
and `JavaNullablePropertyCustomizer`).

Proposed: a public, supported helper, for example `ModelResolver#introspect(JavaType, boolean forSerialization)` or
a static utility. Downstream code would then not depend on Jackson internals that may change in a minor release.

### E4. Declare and test the minimum supported Jackson version

swagger-core 3.0 builds against Jackson 3.2.3 and `jackson-annotations` 2.22. MIGRATION.md and the wiki give 3.2.3 as
"the" version. The current Spring Boot release line (4.1.x) manages Jackson 3.1.7 and annotations 2.21, so most
Spring users will run swagger-core on an older Jackson than it was built with. swagger-core's own test suite passes
with `-Djackson-version=3.1.7 -Djackson-annotations-version=2.21`, and so does springdoc's, so nothing breaks today.
But nothing guarantees it either: a call to an API added in 3.2 would fail with `NoSuchMethodError` at runtime only.

Proposed:

- State a minimum supported Jackson 3 version in MIGRATION.md and the wiki.
- Add a CI job running the swagger-core tests against that version, or against the Jackson version of the current
  Spring Boot release.

### E5. Document the `SwaggerAnnotationIntrospector` signature changes

`hasRequiredMarker`, `findPropertyDescription`, `findSubtypes` and `findTypeName` now take a `MapperConfig<?>` first
parameter, following Jackson 3's `AnnotationIntrospector`. Subclasses that override them with `@Override` fail to
compile. Worse, an override without `@Override` still compiles, as a new overload, and is silently never called.

Proposed: add these methods to the "Method signature changes" section of `MIGRATION.md`, next to the serializer and
deserializer changes, and recommend `@Override`.

### E6. Document the removal of `commons-lang3`

swagger-core no longer depends on `commons-lang3` (#5323). It used to bring it transitively, so downstream projects
that relied on it fail to compile after the upgrade (about 90 errors in springdoc). Proposed: mention it under
"Dependency changes" in `MIGRATION.md`.

### E7. `Month` schema and serialized value disagree

As documented in `MIGRATION.md`, swagger-core 3.0 writes `Month.JANUARY` as `1` by default, while the generated
schema still describes `Month` as a string enum (`JANUARY`…). An API documented with swagger-core then publishes
examples and defaults that don't match its schema. Proposed: either keep the Jackson 2 behaviour (text) for the
swagger mappers by default, or have `ModelResolver` describe `Month` according to the mapper's
`DateTimeFeature.ONE_BASED_MONTHS` / `WRITE_ENUMS_USING_INDEX` settings.

### E8. Cached mappers inside swagger-core itself

`MIGRATION.md` points out that `DefaultParameterExtension` and `OpenAPI31SpecFilter` (through
`OpenAPISchema2JsonSchema`) cache mappers at class initialization, so they miss customizers registered later. Within
swagger-core these could follow the same `generation()` check as `Json.mapper()`, so the documented "register early"
constraint doesn't leak to users.

## Summary

| # | Type | Title | springdoc impact |
|---|---|---|---|
| B1 | Bug | `@JsonUnwrapped/0` synthetic property in record schemas | wrong schema, 1 failing test |
| B2 | Behaviour change | implicit constructor parameters become `writeOnly` properties | schema change, 4 failing tests |
| B3 | Bug | plain Jackson 3 mappers drop `$ref`/`$id`/`$schema`… of swagger models | silent data loss, worked around |
| B4 | Bug | unsynchronized `ModelConverters.getInstance(boolean)` | possible race on first request |
| B5 | Bug | `Json.mapper(...)`/`configure(...)` invisible to `generation()` | stale derived mappers |
| E1 | Enhancement | keyed, idempotent, removable `addModule` | own registry needed |
| E2 | Enhancement | instance-scoped mapper and `ModelConverters` configuration | cross-context leaks |
| E3 | Enhancement | public bean-introspection helper | uses Jackson internals |
| E4 | Enhancement | minimum Jackson version and CI against it | runtime risk on Spring Boot 4.1 |
| E5 | Docs | `SwaggerAnnotationIntrospector` signature changes | compile errors |
| E6 | Docs | `commons-lang3` no longer transitive | ~90 compile errors |
| E7 | Enhancement | `Month` schema vs. serialized value | inconsistent documentation |
| E8 | Enhancement | refresh mappers cached inside swagger-core | stale mappers |

## Proposed upstream

The swagger-core backlog is large, so the findings are bundled into 3 small PRs plus one feedback comment on
[#4991](https://github.com/swagger-api/swagger-core/issues/4991). They're prepared locally in the swagger-core clone,
on top of `3.0.0` `21cde346d`, and haven't been submitted yet.

| PR | Branch | Covers | Size |
|---|---|---|---|
| 1 | `fix/jackson3-model-resolution` | B1 (plus the pre-existing empty unwrapped property) | 7 lines in `ModelResolver`, plus tests |
| 2 | `fix/jackson3-mapper-lifecycle` | B4, B5, E8 | ~30 lines, plus tests |
| 3 | `docs/jackson3-migration-notes` | E6, E5, B2, B3 | MIGRATION.md only |

E1, E2, E3, E7 and the schema-only part of B2 are raised as proposals in the #4991 comment. E4 (minimum Jackson version) isn't proposed upstream.

The 3 branches merge without conflicts. With all of them, swagger-core and swagger-rest pass (1592 + 246 tests), and
springdoc's `app237` passes once its expected file is corrected. The 4 `app1` failures remain: they are the B2
behaviour change, a decision for springdoc.

