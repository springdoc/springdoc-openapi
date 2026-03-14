/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.ai.mcp;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * Utility that converts OpenAPI {@link Operation} + {@link Components} into a JSON Schema
 * string suitable for AI tool input schema.
 *
 * @author bnasslahsen
 */
public final class OpenApiSchemaConverter {

	/**
	 * Pattern to match path template variables like {id} or {version}.
	 */
	private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}]+)}");

	/**
	 * Private constructor to prevent instantiation.
	 */
	private OpenApiSchemaConverter() {
	}

	/**
	 * Builds a JSON Schema string combining parameters and requestBody from the given
	 * operation.
	 * @param path the URL path template (e.g. /api/v{version}/users/{id})
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components for $ref resolution
	 * @return the JSON Schema string
	 */
	public static String buildInputSchema(String path, Operation operation, Components components) {
		ObjectNode schema = JsonNodeFactory.instance.objectNode();
		schema.put("type", "object");
		ObjectNode properties = schema.putObject("properties");
		ArrayNode required = schema.putArray("required");

		Set<String> declaredParams = new HashSet<>();
		if (operation.getParameters() != null) {
			for (Parameter param : operation.getParameters()) {
				addParameterToSchema(param, properties, required, components);
				if (param.getName() != null) {
					declaredParams.add(param.getName());
				}
			}
		}

		addUndeclaredPathVariables(path, declaredParams, properties, required);

		if (operation.getRequestBody() != null) {
			addRequestBodyToSchema(operation.getRequestBody(), properties, required, components);
		}

		return schema.toString();
	}

	/**
	 * Adds a parameter to the JSON Schema properties.
	 * @param param the OpenAPI parameter
	 * @param properties the properties node to add to
	 * @param required the required array node
	 * @param components the OpenAPI components for $ref resolution
	 */
	private static void addParameterToSchema(Parameter param, ObjectNode properties, ArrayNode required,
			Components components) {
		String name = param.getName();
		if (name == null) {
			return;
		}

		ObjectNode prop = properties.putObject(name);
		Schema<?> paramSchema = param.getSchema();
		if (paramSchema != null) {
			paramSchema = resolveSchema(paramSchema, components);
			applySchemaToNode(paramSchema, prop, components, newVisitedSet());
		}
		else {
			prop.put("type", "string");
		}

		if (param.getDescription() != null) {
			prop.put("description", param.getDescription());
		}

		if (Boolean.TRUE.equals(param.getRequired())) {
			required.add(name);
		}
	}

	/**
	 * Detects path template variables not covered by declared parameters and adds them as
	 * required string properties.
	 * @param path the URL path template
	 * @param declaredParams set of already-declared parameter names
	 * @param properties the properties node to add to
	 * @param required the required array node
	 */
	private static void addUndeclaredPathVariables(String path, Set<String> declaredParams, ObjectNode properties,
			ArrayNode required) {
		if (path == null) {
			return;
		}
		Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);
		while (matcher.find()) {
			String varName = matcher.group(1);
			if (!declaredParams.contains(varName)) {
				ObjectNode prop = properties.putObject(varName);
				prop.put("type", "string");
				prop.put("description", "Path variable: " + varName);
				required.add(varName);
			}
		}
	}

	/**
	 * Adds request body to the JSON Schema properties as a "body" property.
	 * @param requestBody the OpenAPI request body
	 * @param properties the properties node to add to
	 * @param required the required array node
	 * @param components the OpenAPI components for $ref resolution
	 */
	private static void addRequestBodyToSchema(RequestBody requestBody, ObjectNode properties, ArrayNode required,
			Components components) {
		if (requestBody.get$ref() != null && components != null && components.getRequestBodies() != null) {
			String ref = requestBody.get$ref();
			String refName = ref.substring(ref.lastIndexOf('/') + 1);
			requestBody = components.getRequestBodies().get(refName);
			if (requestBody == null) {
				return;
			}
		}

		Content content = requestBody.getContent();
		if (content == null) {
			return;
		}

		MediaType mediaType = content.get("application/json");
		if (mediaType == null) {
			// Try first available media type
			mediaType = content.values().stream().findFirst().orElse(null);
		}

		if (mediaType != null && mediaType.getSchema() != null) {
			Schema<?> bodySchema = resolveSchema(mediaType.getSchema(), components);
			ObjectNode bodyProp = properties.putObject("body");
			applySchemaToNode(bodySchema, bodyProp, components, newVisitedSet());
			if (bodySchema.getDescription() != null) {
				bodyProp.put("description", bodySchema.getDescription());
			}
			else {
				bodyProp.put("description", "Request body");
			}

			if (Boolean.TRUE.equals(requestBody.getRequired())) {
				required.add("body");
			}
		}
	}

	/**
	 * Resolves $ref schemas against Components.
	 * @param schema the schema that may contain a $ref
	 * @param components the OpenAPI components
	 * @return the resolved schema
	 */
	@SuppressWarnings("rawtypes")
	static Schema<?> resolveSchema(Schema<?> schema, Components components) {
		if (schema.get$ref() != null && components != null && components.getSchemas() != null) {
			String ref = schema.get$ref();
			String refName = ref.substring(ref.lastIndexOf('/') + 1);
			Schema resolved = components.getSchemas().get(refName);
			if (resolved != null) {
				return resolved;
			}
		}
		return schema;
	}

	/**
	 * Extracts the schema type, checking both {@code getType()} (OAS 3.0) and
	 * {@code getTypes()} (OAS 3.1) for compatibility.
	 * @param schema the OpenAPI schema
	 * @return the type string, or null if not set
	 */
	private static String getSchemaType(Schema<?> schema) {
		if (schema.getType() != null) {
			return schema.getType();
		}
		Set<String> types = schema.getTypes();
		if (types != null && !types.isEmpty()) {
			return types.iterator().next();
		}
		return null;
	}

	/**
	 * Creates a new identity-based visited set for circular reference detection.
	 * @return a new empty identity hash set
	 */
	private static Set<Schema<?>> newVisitedSet() {
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}

	/**
	 * Applies schema properties to a JSON node with full recursive conversion, $ref
	 * resolution, circular reference detection, composed schema support, and validation
	 * constraints.
	 * @param schema the OpenAPI schema
	 * @param node the JSON node to populate
	 * @param components the OpenAPI components for $ref resolution
	 * @param visited identity set of already-visited schemas for cycle detection
	 */
	@SuppressWarnings("unchecked")
	private static void applySchemaToNode(Schema<?> schema, ObjectNode node, Components components,
			Set<Schema<?>> visited) {
		// Circular reference detection
		if (!visited.add(schema)) {
			node.put("type", "object");
			return;
		}

		// Handle allOf: merge all sub-schemas into this node
		if (schema.getAllOf() != null && !schema.getAllOf().isEmpty()) {
			applyAllOf(schema, node, components, visited);
			return;
		}

		// Handle anyOf/oneOf: emit JSON Schema composition keyword
		if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
			applyComposedSchemas("oneOf", schema.getOneOf(), node, components, visited);
			return;
		}
		if (schema.getAnyOf() != null && !schema.getAnyOf().isEmpty()) {
			applyComposedSchemas("anyOf", schema.getAnyOf(), node, components, visited);
			return;
		}

		// Type and nullable
		String type = getSchemaType(schema);
		if (Boolean.TRUE.equals(schema.getNullable())) {
			ArrayNode typeArray = node.putArray("type");
			typeArray.add(type != null ? type : "object");
			typeArray.add("null");
		}
		else {
			node.put("type", type != null ? type : "object");
		}

		if (schema.getDescription() != null) {
			node.put("description", schema.getDescription());
		}

		if (schema.getFormat() != null) {
			node.put("format", schema.getFormat());
		}

		if (schema.getEnum() != null && !schema.getEnum().isEmpty()) {
			ArrayNode enumArray = node.putArray("enum");
			for (Object value : schema.getEnum()) {
				enumArray.add(String.valueOf(value));
			}
		}

		if (schema.getDefault() != null) {
			node.put("default", String.valueOf(schema.getDefault()));
		}

		// Validation constraints
		applyValidationConstraints(schema, node);

		// Nested object properties (recursive)
		if (schema.getProperties() != null) {
			ObjectNode nestedProps = node.putObject("properties");
			Map<String, Schema> props = schema.getProperties();
			for (Map.Entry<String, Schema> entry : props.entrySet()) {
				ObjectNode nestedProp = nestedProps.putObject(entry.getKey());
				Schema<?> propSchema = resolveSchema(entry.getValue(), components);
				applySchemaToNode(propSchema, nestedProp, components, visited);
			}
			// Preserve required array from the object schema
			if (schema.getRequired() != null && !schema.getRequired().isEmpty()) {
				ArrayNode reqArray = node.putArray("required");
				for (String reqName : schema.getRequired()) {
					reqArray.add(reqName);
				}
			}
		}

		// Additional properties (e.g. Map<String, String>)
		if (schema.getAdditionalProperties() instanceof Schema<?> additionalPropsSchema) {
			ObjectNode additionalPropsNode = node.putObject("additionalProperties");
			Schema<?> resolved = resolveSchema(additionalPropsSchema, components);
			applySchemaToNode(resolved, additionalPropsNode, components, visited);
		}
		else if (Boolean.TRUE.equals(schema.getAdditionalProperties())) {
			node.put("additionalProperties", true);
		}

		// Array items (recursive)
		if (schema.getItems() != null) {
			ObjectNode itemsNode = node.putObject("items");
			Schema<?> items = resolveSchema(schema.getItems(), components);
			applySchemaToNode(items, itemsNode, components, visited);
		}
	}

	/**
	 * Handles allOf by merging all sub-schema properties into the target node.
	 * @param schema the parent schema containing allOf
	 * @param node the JSON node to populate
	 * @param components the OpenAPI components for $ref resolution
	 * @param visited identity set for cycle detection
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void applyAllOf(Schema<?> schema, ObjectNode node, Components components, Set<Schema<?>> visited) {
		node.put("type", "object");
		ObjectNode mergedProperties = node.putObject("properties");
		ArrayNode mergedRequired = node.putArray("required");

		for (Schema subSchema : schema.getAllOf()) {
			Schema<?> resolved = resolveSchema(subSchema, components);
			if (resolved.getProperties() != null) {
				Map<String, Schema> props = resolved.getProperties();
				for (Map.Entry<String, Schema> entry : props.entrySet()) {
					ObjectNode propNode = mergedProperties.putObject(entry.getKey());
					Schema<?> propSchema = resolveSchema(entry.getValue(), components);
					applySchemaToNode(propSchema, propNode, components, visited);
				}
			}
			if (resolved.getRequired() != null) {
				for (String reqName : resolved.getRequired()) {
					mergedRequired.add(reqName);
				}
			}
		}

		if (schema.getDescription() != null) {
			node.put("description", schema.getDescription());
		}
	}

	/**
	 * Handles oneOf or anyOf composed schemas by emitting the corresponding JSON Schema
	 * composition keyword.
	 * @param keyword the composition keyword ("oneOf" or "anyOf")
	 * @param subSchemas the list of sub-schemas
	 * @param node the JSON node to populate
	 * @param components the OpenAPI components for $ref resolution
	 * @param visited identity set for cycle detection
	 */
	@SuppressWarnings("rawtypes")
	private static void applyComposedSchemas(String keyword, List<Schema> subSchemas, ObjectNode node,
			Components components, Set<Schema<?>> visited) {
		ArrayNode composedArray = node.putArray(keyword);
		for (Schema subSchema : subSchemas) {
			ObjectNode subNode = composedArray.addObject();
			Schema<?> resolved = resolveSchema(subSchema, components);
			applySchemaToNode(resolved, subNode, components, visited);
		}
	}

	/**
	 * Applies validation constraints from the OpenAPI schema to the JSON Schema node.
	 * Includes: pattern, minimum, maximum, exclusiveMinimum, exclusiveMaximum, minLength,
	 * maxLength, minItems, maxItems, multipleOf.
	 * @param schema the OpenAPI schema
	 * @param node the JSON node to populate
	 */
	private static void applyValidationConstraints(Schema<?> schema, ObjectNode node) {
		if (schema.getPattern() != null) {
			node.put("pattern", schema.getPattern());
		}
		if (schema.getMinimum() != null) {
			node.put("minimum", schema.getMinimum());
		}
		if (schema.getMaximum() != null) {
			node.put("maximum", schema.getMaximum());
		}
		if (Boolean.TRUE.equals(schema.getExclusiveMinimum())) {
			node.put("exclusiveMinimum", true);
		}
		else if (schema.getExclusiveMinimumValue() != null) {
			node.put("exclusiveMinimum", schema.getExclusiveMinimumValue());
		}
		if (Boolean.TRUE.equals(schema.getExclusiveMaximum())) {
			node.put("exclusiveMaximum", true);
		}
		else if (schema.getExclusiveMaximumValue() != null) {
			node.put("exclusiveMaximum", schema.getExclusiveMaximumValue());
		}
		if (schema.getMinLength() != null) {
			node.put("minLength", schema.getMinLength());
		}
		if (schema.getMaxLength() != null) {
			node.put("maxLength", schema.getMaxLength());
		}
		if (schema.getMinItems() != null) {
			node.put("minItems", schema.getMinItems());
		}
		if (schema.getMaxItems() != null) {
			node.put("maxItems", schema.getMaxItems());
		}
		if (schema.getMultipleOf() != null) {
			node.put("multipleOf", schema.getMultipleOf());
		}
	}

	/**
	 * Builds a human-readable response description from the operation's success response
	 * schema.
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components for $ref resolution
	 * @return the response description, or empty string if unavailable
	 */
	@SuppressWarnings("unchecked")
	public static String buildResponseDescription(Operation operation, Components components) {
		ApiResponses responses = operation.getResponses();
		if (responses == null) {
			return "";
		}
		ApiResponse response = responses.get("200");
		if (response == null) {
			response = responses.entrySet()
				.stream()
				.filter(e -> e.getKey().startsWith("2"))
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse(null);
		}
		if (response == null || response.getContent() == null) {
			return "";
		}
		Content content = response.getContent();
		MediaType mediaType = content.get("application/json");
		if (mediaType == null) {
			mediaType = content.values().stream().findFirst().orElse(null);
		}
		if (mediaType == null || mediaType.getSchema() == null) {
			return "";
		}
		return "Returns " + describeSchema(mediaType.getSchema(), components);
	}

	/**
	 * Describes a single schema as a human-readable string.
	 * @param schema the schema to describe
	 * @param components the OpenAPI components for $ref resolution
	 * @return the description string
	 */
	@SuppressWarnings("rawtypes")
	private static String describeSchema(Schema<?> schema, Components components) {
		if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
			StringJoiner joiner = new StringJoiner(" or ");
			for (Schema oneOf : schema.getOneOf()) {
				joiner.add(describeSchema(oneOf, components));
			}
			return joiner.toString();
		}
		String type = getSchemaType(schema);
		if ("array".equals(type) && schema.getItems() != null) {
			return "an array of " + describeSchema(schema.getItems(), components);
		}
		if (schema.get$ref() != null) {
			String refName = schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);
			String props = describeProperties(refName, components);
			return props.isEmpty() ? refName : refName + " (" + props + ")";
		}
		Schema<?> resolved = resolveSchema(schema, components);
		if (resolved != schema && resolved.getProperties() != null) {
			String props = String.join(", ", resolved.getProperties().keySet());
			String resolvedType = getSchemaType(resolved);
			String typeName = resolvedType != null ? resolvedType : "object";
			return props.isEmpty() ? typeName : typeName + " (" + props + ")";
		}
		return type != null ? type : "object";
	}

	/**
	 * Describes the properties of a named component schema.
	 * @param schemaName the schema name in components
	 * @param components the OpenAPI components
	 * @return comma-separated property names, or empty string
	 */
	@SuppressWarnings("unchecked")
	private static String describeProperties(String schemaName, Components components) {
		if (components == null || components.getSchemas() == null) {
			return "";
		}
		Schema<?> schema = components.getSchemas().get(schemaName);
		if (schema == null || schema.getProperties() == null) {
			return "";
		}
		return String.join(", ", schema.getProperties().keySet());
	}

}
