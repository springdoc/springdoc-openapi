# Springdoc OpenAPI MCP Integration

This module bridges your REST API with AI Agents using Spring AI and the Model Context Protocol (MCP).

## Features

- **Auto-Discovery**: Automatically turns your documented `@RestController` endpoints into AI Tools.
- **Semantic Mapping**: Maps OpenAPI descriptions to AI tool metadata so LLMs understand context.
- **MCP Dashboard**: Built-in web UI for discovering and testing MCP tools, with tools grouped by OpenAPI tags.
- **Agent Ready**: Compatible with Claude Desktop, Cursor, and other MCP-enabled clients.
- **Customizable**: Extension points to rename, redescribe, filter, or exclude tools.
- **AI-Optimized Descriptions**: `@McpToolDescription` annotation for writing descriptions specifically for AI agents.
- **Tool Grouping**: Tools are automatically organized by their `@Tag` annotation, matching how OpenAPI operations are grouped in Swagger UI.
- **Return Type Descriptions**: Tool descriptions are enriched with human-readable return type information for better AI comprehension.
- **MCP Guardrails**: Safe/mutating tool classification and Human-in-the-Loop (HITL) support — mutating operations require human approval before execution by default.
- **Latency Attribution**: Split latency tracking isolates REST API time from server overhead, displayed as a stacked bar in the audit dashboard.
- **Payload Size & Token Estimation**: Captures byte sizes of MCP arguments, request bodies, and response bodies with approximate token counts to help manage context window usage.

## Modules

| Module | Description |
|--------|-------------|
| `springdoc-openapi-starter-common-mcp` | Core engine: properties, tool callbacks, schema converter, customizers, annotations, MCP dashboard controller, and frontend assets. |
| `springdoc-openapi-starter-webmvc-mcp` | WebMvc integration: static resource configurer, servlet API version strategy, `@McpToolDescription` scanning, and dashboard initializer. |
| `springdoc-openapi-starter-webflux-mcp` | WebFlux integration: reactive resource configurer, reactive API version strategy, `@McpToolDescription` scanning, and dashboard initializer. |

## Usage

Add the dependency matching your web stack:

**For WebMvc (Spring MVC / Servlet):**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-mcp</artifactId>
</dependency>
```

**For WebFlux (Reactive):**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-mcp</artifactId>
</dependency>
```

Enable the integration in your `application.properties`:

```properties
springdoc.ai.mcp.enabled=true
```

That's it. Your `@RestController` endpoints are now available as MCP tools. AI agents connecting to your MCP server will automatically discover them.

## Configuration

All properties are under the `springdoc.ai.mcp` prefix:

| Property | Default | Description |
|----------|---------|-------------|
| `springdoc.ai.mcp.enabled` | `true` | Enable the MCP tool integration. |
| `springdoc.ai.mcp.base-url` | `http://localhost:8080` | Base URL for tool execution HTTP calls. |
| `springdoc.ai.mcp.init-timeout-seconds` | `30` | Timeout in seconds waiting for the OpenAPI spec at startup. |
| `springdoc.ai.mcp.paths-to-exclude` | none | List of ant patterns for paths to exclude from MCP tool generation. |
| `springdoc.ai.mcp.mcp-endpoint` | `/mcp` | The MCP server endpoint path. |
| `springdoc.ai.mcp.dashboard-enabled` | `false` | Enable the MCP Developer Dashboard UI. |
| `springdoc.ai.mcp.dashboard-path` | `/mcp-ui` | The dashboard UI mount path. |
| `springdoc.ai.mcp.guardrails.require-approval-for-mutating-tools` | `true` | When `true`, calling a mutating tool (POST/PUT/DELETE/PATCH) via MCP returns an approval-required response instead of executing the HTTP call. |
| `springdoc.ai.mcp.guardrails.safe-methods` | `GET,HEAD,OPTIONS` | Comma-separated list of HTTP methods considered safe (read-only). All other methods are treated as mutating. |

> **Note**: When MCP is enabled, `springdoc.pre-loading-enabled` is automatically forced to `true` by an environment post-processor, ensuring the OpenAPI specification is available at startup for tool registration.

## MCP Guardrails

Guardrails protect your API from unintended mutations triggered by AI agents. Two complementary safety features are built in:

### Safe vs. Mutating Classification

Every MCP tool is automatically classified based on its HTTP method:

| Classification | HTTP Methods |
|---------------|-------------|
| **Safe** (read-only) | `GET`, `HEAD`, `OPTIONS` (configurable via `guardrails.safe-methods`) |
| **Mutating** | `POST`, `PUT`, `DELETE`, `PATCH`, and any other method |

The `safe` and `requiresApproval` flags are exposed on the `/api/mcp-admin/tools` endpoint and surfaced in the dashboard UI (mutating tools show an amber warning badge).

Individual endpoints can override this classification by calling `context.setSafeEndpoint(Boolean)` inside a `McpToolCustomizer` bean, which takes precedence over the global `safe-methods` list (see [McpToolCustomizer](#mcptoolcustomizer)).

### Human-in-the-Loop (HITL)

When `require-approval-for-mutating-tools` is `true` (the default), calling a mutating tool through the MCP protocol does **not** execute the underlying HTTP request. Instead, the agent receives a structured JSON response:

```json
{
  "requires_human_approval": true,
  "tool_name": "create_book",
  "http_method": "POST",
  "path": "/books",
  "arguments": { "title": "...", "author": "..." },
  "message": "This mutating operation (POST /books) requires human approval before execution."
}
```

The AI agent is expected to relay this to the user, who can then confirm or reject the action.

From the **MCP Dashboard**, executing a mutating tool while HITL is enabled returns an `Approval Required` response (amber) instead of the normal success/error state.

### Disabling HITL

To allow AI agents to call mutating tools freely (e.g., in a trusted internal environment), set:

```properties
springdoc.ai.mcp.guardrails.require-approval-for-mutating-tools=false
```

### Customizing Safe Methods

To treat additional methods as safe, override the list:

```properties
springdoc.ai.mcp.guardrails.safe-methods=GET,HEAD,OPTIONS,TRACE
```

### Behavior Summary

| Scenario | MCP agent result | Dashboard result |
|----------|-----------------|-----------------|
| GET tool (any guardrail setting) | HTTP response body | HTTP response |
| Mutating tool, HITL disabled | HTTP response body | HTTP response |
| Mutating tool, HITL enabled (default) | `{"requires_human_approval": true, ...}` | `Approval Required` |

## How It Works

1. At startup, springdoc scans your `@RestController` endpoints and builds the OpenAPI specification (pre-loading is forced automatically).
2. Each OpenAPI operation is converted into a `ToolCallback` with:
   - A **name** derived from the `operationId` (converted to `snake_case` for better LLM tokenization, e.g., `getUserById` becomes `get_user_by_id`).
   - A **description** from `summary`/`description`, enriched with return type information (e.g., "Returns User (id, name, email)").
   - A **JSON Schema** input schema from parameters and request body.
   - A **group** from the first `@Tag` annotation on the controller.
3. The customizer chain runs on each tool definition, applying `@McpToolDescription` annotations first, then any user-registered `McpToolCustomizer` beans.
4. AI agents connect to the MCP server, discover the available tools, and can invoke your API endpoints directly.
5. When an MCP server (`McpSyncServer`) is present, tools are registered dynamically after all singletons are instantiated via a deferred registration mechanism to avoid startup deadlocks.

## Extension Points

### McpToolCustomizer

Register a `McpToolCustomizer` bean to modify the tool name, description, or input schema for any operation. Return `null` to exclude a tool entirely. Multiple customizers are applied in order.

```java
@Bean
McpToolCustomizer rewriteDescriptions() {
    return (context, path, method, operation) -> {
        // Rename a tool
        if ("listUsers".equals(context.getName())) {
            context.setName("fetchAllUsers");
        }
        // Rewrite a description
        if ("getUserById".equals(context.getName())) {
            context.setDescription("Look up a single user by their unique ID");
        }
        // Exclude a tool by returning null
        if (path.startsWith("/internal")) {
            return null;
        }
        return context;
    };
}
```

The `McpToolDefinitionContext` passed to each customizer exposes:

| Getter / Setter | Description |
|-----------------|-------------|
| `getName()` / `setName(String)` | The tool name (defaults to the `operationId`). |
| `getDescription()` / `setDescription(String)` | The tool description shown to AI agents. |
| `getInputSchema()` / `setInputSchema(String)` | The JSON Schema string for tool input. |
| `isExclude()` / `setExclude(boolean)` | Set to `true` to drop this endpoint from the MCP tool list entirely. Preferred over returning `null`. |
| `getSafeEndpoint()` / `setSafeEndpoint(Boolean)` | Override the safe/mutating classification for this endpoint. `true` = safe, `false` = mutating, `null` = use global `safe-methods` config (default). |

**Excluding endpoints** via `context.setExclude(true)`:

```java
@Bean
McpToolCustomizer excludeDeleteAndInternal() {
    return (context, path, method, operation) -> {
        if (method == PathItem.HttpMethod.DELETE || path.startsWith("/internal")) {
            context.setExclude(true);
        }
        return context;
    };
}
```

**Per-endpoint safety override** via `context.setSafeEndpoint(Boolean)`:

```java
@Bean
McpToolCustomizer overrideSafety() {
    return (context, path, method, operation) -> {
        // Treat POST /reports/generate as safe (read-only despite POST method)
        if (method == PathItem.HttpMethod.POST && "/reports/generate".equals(path)) {
            context.setSafeEndpoint(true);
        }
        // Treat GET /cache/invalidate as mutating despite GET method
        else if (method == PathItem.HttpMethod.GET && "/cache/invalidate".equals(path)) {
            context.setSafeEndpoint(false);
        }
        return context;
    };
}
```

| `setSafeEndpoint` value | Effect |
|------------------------|--------|
| `true` | Force safe — HITL is skipped even if `require-approval-for-mutating-tools=true` |
| `false` | Force mutating — HITL applies even if the method is in `safe-methods` |
| `null` (default) | Use the global `safe-methods` configuration |

### @McpToolDescription Annotation

Place `@McpToolDescription` on a controller method to provide an AI-optimized description that overrides the OpenAPI `summary`/`description` in the MCP tool definition. Optionally override the tool name.

```java
@GetMapping("/orders/{id}")
@Operation(summary = "Get an order by ID", operationId = "getOrderById")
@McpToolDescription(value = "Look up a single order by its unique identifier",
        name = "findOrder")
public Order getOrderById(@PathVariable String id) { ... }
```

| Attribute | Required | Description |
|-----------|----------|-------------|
| `value` | yes | AI-optimized description for the tool. |
| `name` | no | Tool name override. When empty, the `operationId` is used. |

The built-in `McpToolDescriptionCustomizer` reads these annotations and runs at `Ordered.HIGHEST_PRECEDENCE`, so user-registered `McpToolCustomizer` beans can further modify the annotation-provided values.

### Summary of Extension Points

| Extension Point | Type | Purpose |
|-----------------|------|---------|
| `McpToolCustomizer` | `@FunctionalInterface` | Customize tool name, description, or schema; set `context.setExclude(true)` to exclude; set `context.setSafeEndpoint(Boolean)` to override safe/mutating classification. |
| `@McpToolDescription` | Annotation | Provide AI-optimized descriptions directly on controller methods. |
| `McpDashboardToolSource` | Interface | Provide custom tool discovery and execution sources for the dashboard. |

## Tool Grouping

Tools in the MCP dashboard are automatically organized into groups:

- **OpenAPI-generated tools** are grouped by their first `@Tag` annotation. For example, a controller annotated with `@Tag(name = "Users")` will have all its operations grouped under "Users".
- **Native MCP tools** (from `@McpTool`/`@Tool` annotated methods) are grouped under `"mcp-tools"`.
- **Tools without a tag** appear under "Other" in the dashboard.

```java
@RestController
@Tag(name = "Users")
public class UserController {

    @GetMapping("/users")
    @Operation(operationId = "listUsers")
    public List<User> listUsers() { ... }     // group: "Users"

    @GetMapping("/users/{id}")
    @Operation(operationId = "getUserById")
    public User getUserById(@PathVariable String id) { ... }  // group: "Users"
}

@RestController
@Tag(name = "Products")
public class ProductController {

    @GetMapping("/products")
    @Operation(operationId = "listProducts")
    public List<Product> listProducts() { ... }  // group: "Products"
}
```

In the dashboard, tools are displayed in collapsible sections by group with a tool count badge. All groups are expanded by default. Search filters across all groups, and empty groups after filtering are hidden.

## MCP Dashboard

The built-in MCP Developer Dashboard is a web UI served at the configured `dashboard-path` (default: `/mcp-ui`). Enable it with:

```properties
springdoc.ai.mcp.dashboard-enabled=true
```

It provides:

- **Tool Discovery**: Lists all registered MCP tools grouped by their OpenAPI tags, with names, descriptions, HTTP methods, paths, and input schemas.
- **Tool Execution**: Execute any tool directly from the browser with JSON input, view response body, status code, and execution duration.
- **Guardrail Indicators**: Mutating tools that require human approval are marked with an amber warning badge (⚠) in the tool list and display a banner in the detail panel. Executing a blocked tool shows an `Approval Required` response instead of an error.
- **Header Forwarding**: Authentication headers (e.g., `Authorization`, API keys) are automatically forwarded from the dashboard to the underlying API calls.
- **Security Settings**: Configure authentication credentials (Bearer tokens, Basic auth, API keys) in the dashboard UI.
- **Multi-Source Support**: Discovers tools from both `ToolCallbackProvider` beans (OpenAPI-backed tools) and `McpSyncServer` tool specifications (`@McpTool` annotated methods).
- **cURL Export**: Copy tool invocations as cURL commands for use outside the dashboard.
- **Latency Attribution**: The audit tab displays a stacked latency bar per tool call — green for REST API time and gray for server overhead — with a tooltip showing the full breakdown.
- **Payload Size & Token Impact**: Trace viewer tabs show byte sizes and estimated token counts (`bytes / 4`) for MCP arguments, request bodies, and response bodies. Responses larger than 2 KB trigger an orange warning suggesting lightweight DTOs to reduce context window usage.

### Dashboard Endpoints

The dashboard REST API is served at `/api/mcp-admin`:

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/mcp-admin/tools` | Lists all available MCP tools (includes `group` field). |
| `POST` | `/api/mcp-admin/tools/execute` | Executes a tool by name with JSON arguments. |

### Audit Logging

Every MCP tool execution produces a structured JSON audit event logged at `INFO` level to the `org.springdoc.ai.mcp.audit` logger. When the dashboard is active, events are also stored in-memory for the audit tab.

Each audit event contains:

| Section | Field | Description |
|---------|-------|-------------|
| `execution` | `duration_ms` | Total wall-clock time of the `executeHttp()` call (includes JSON parsing, path resolution, body building, and the HTTP call). |
| `execution` | `rest_duration_ms` | Time spent in the `HTTP_CLIENT.send()` call only. The difference (`duration_ms - rest_duration_ms`) represents server overhead. |
| `translation` | `mcp_arguments_size` | Byte length (UTF-8) of the raw MCP/JSON-RPC arguments from the LLM. |
| `translation` | `request_body_size` | Byte length (UTF-8) of the JSON body sent to the API. |
| `translation` | `response_body_size` | Byte length (UTF-8) of the full response body from the API. |

**Dashboard visualization:**

- **Latency bar** (audit table): A mini stacked bar (green = REST API time, gray = server overhead) with the total duration as a text label. Hovering shows a tooltip with the full breakdown: `Total: Xms | REST API: Yms | Overhead: Zms`.
- **REST Request tab**: Displays timing badges (`REST API: Yms` in green, `Overhead: Zms` in gray) and shows request body size with token estimate (e.g., `64 B (~16 tokens)`).
- **Response tab**: Shows response size with token estimate (e.g., `2.1 KB (~538 tokens)`). When the response exceeds 2 KB, an orange warning banner suggests creating a lightweight DTO to reduce context window usage.

## Schema Conversion

The `OpenApiSchemaConverter` translates OpenAPI operations into JSON Schema suitable for AI tool input. It handles:

- **Parameters**: Path, query, and header parameters with types, descriptions, and required markers.
- **Undeclared Path Variables**: Detects `{var}` in path templates not declared as parameters and adds them as required string properties.
- **Request Body**: Mapped as a `body` property; supports `$ref` resolution and multiple media types (prefers `application/json`).
- **Composed Schemas**: `allOf` (merged), `anyOf`, `oneOf` composition keywords.
- **Nested Objects**: Recursive property processing with circular reference detection.
- **Validation Constraints**: `pattern`, `minimum`/`maximum`, `exclusiveMinimum`/`exclusiveMaximum`, `minLength`/`maxLength`, `minItems`/`maxItems`, `multipleOf`, `enum`, `default`.
- **Response Descriptions**: Appends a human-readable return type description (e.g., "Returns an array of User (id, name, email)") to the tool description for both OpenAPI-backed and native MCP tools.

## API Version Strategy Compatibility

When path-based API versioning is configured (e.g., `usePathSegment(1)`), the version resolver may throw `InvalidApiVersionException` for MCP paths like `/mcp` or `/mcp-ui`. The WebMvc and WebFlux modules automatically wrap the `ApiVersionStrategy` on all `RequestMappingHandlerMapping` and `RouterFunctionMapping` beans so that MCP and dashboard paths are handled gracefully.

Protected paths: `/mcp`, `/mcp-ui`, `/api/mcp-admin`.

## Module Details

### springdoc-openapi-starter-webmvc-mcp

The WebMvc module provides servlet-specific integration:

- **`McpWebMvcAiAutoConfiguration`**: Auto-configuration activated for `SERVLET` web applications. Registers the `McpToolDescriptionCustomizer` by scanning `RequestMappingHandlerMapping` handler methods, sets up the dashboard `WebMvcConfigurer`, and wraps API version strategies.
- **`McpDashboardWebMvcConfigurer`**: Implements `WebMvcConfigurer` to serve static dashboard assets from `classpath:/mcp-ui/` and adds view controller redirects from `/mcp-ui` and `/mcp-ui/` to `/mcp-ui/index.html`.
- **`McpApiVersionStrategy`**: Wraps the existing servlet `ApiVersionStrategy` to gracefully handle MCP paths that don't conform to path-based versioning patterns.

**Dependencies**: `springdoc-openapi-starter-common-mcp`, `springdoc-openapi-starter-webmvc-api`, `jakarta.servlet-api`.

### springdoc-openapi-starter-webflux-mcp

The WebFlux module provides reactive-specific integration:

- **`McpWebFluxAiAutoConfiguration`**: Auto-configuration activated for `REACTIVE` web applications. Registers the `McpToolDescriptionCustomizer` by scanning reactive `RequestMappingHandlerMapping` handler methods, sets up the dashboard `WebFluxConfigurer`, creates redirect `RouterFunction` beans, and wraps API version strategies.
- **`McpDashboardWebFluxConfigurer`**: Implements `WebFluxConfigurer` to serve static dashboard assets from `classpath:/mcp-ui/`.
- **`McpReactiveApiVersionStrategy`**: Wraps the existing reactive `ApiVersionStrategy` to gracefully handle MCP paths. Supports both synchronous (`resolveParseAndValidateVersion`) and reactive (`resolveParseAndValidateApiVersion`) resolution.
- **Dashboard Redirects**: Registered as a `RouterFunction<ServerResponse>` bean that redirects `/mcp-ui` and `/mcp-ui/` to `/mcp-ui/index.html` with HTTP 302.

**Dependencies**: `springdoc-openapi-starter-common-mcp`, `springdoc-openapi-starter-webflux-api`.

### springdoc-openapi-starter-common-mcp

The core module contains all shared logic:

| Package | Responsibility |
|---------|----------------|
| `org.springdoc.ai.configuration` | Auto-configuration for tool callback provider and deferred MCP server registration. |
| `org.springdoc.ai.properties` | `SpringDocAiProperties` configuration properties (`springdoc.ai.mcp.*`). |
| `org.springdoc.ai.mcp` | OpenAPI-to-MCP conversion: `OpenApiMcpToolCallbackProvider`, `OpenApiToolCallback`, `OpenApiSchemaConverter`. |
| `org.springdoc.ai.customizers` | Extension interfaces: `McpToolCustomizer`, `McpToolDescriptionCustomizer`, `McpToolDefinitionContext`. |
| `org.springdoc.ai.annotations` | `@McpToolDescription` annotation. |
| `org.springdoc.ai.dashboard` | Dashboard REST controller, tool sources (`ToolCallbackDashboardToolSource`, `McpSyncServerDashboardToolSource`), and DTOs. |
| `org.springdoc.ai.environment` | `SpringDocAiEnvironmentPostProcessor` — forces `springdoc.pre-loading-enabled=true`. |

**Dependencies**: `springdoc-openapi-starter-common`, `spring-ai-model`, optional `mcp-core`, optional `spring-ai-mcp`.
