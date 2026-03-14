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

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OpenApiSchemaConverter}.
 *
 * @author bnasslahsen
 */
class OpenApiSchemaConverterTest {

	/**
	 * The object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Test that path and query parameters are correctly converted to JSON Schema.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testParametersConvertedToSchema() throws Exception {
		Operation operation = new Operation();
		Parameter pathParam = new Parameter().name("id").in("path").required(true).schema(new StringSchema());
		Parameter queryParam = new Parameter().name("filter")
			.in("query")
			.required(false)
			.description("Filter criteria")
			.schema(new StringSchema());
		operation.setParameters(List.of(pathParam, queryParam));

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/users/{id}", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		assertThat(schema.get("type").asText()).isEqualTo("object");
		assertThat(schema.get("properties").has("id")).isTrue();
		assertThat(schema.get("properties").has("filter")).isTrue();
		assertThat(schema.get("properties").get("id").get("type").asText()).isEqualTo("string");
		assertThat(schema.get("properties").get("filter").get("description").asText()).isEqualTo("Filter criteria");

		// Check required
		boolean idRequired = false;
		for (JsonNode req : schema.get("required")) {
			if ("id".equals(req.asText())) {
				idRequired = true;
			}
		}
		assertThat(idRequired).isTrue();
	}

	/**
	 * Test that a request body is correctly added to the schema.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testRequestBodyConvertedToSchema() throws Exception {
		Operation operation = new Operation();
		Schema<Object> bodySchema = new Schema<>();
		bodySchema.setType("object");
		bodySchema.setProperties(Map.of("name", new StringSchema().description("User name"), "email",
				new StringSchema().description("User email")));
		MediaType mediaType = new MediaType().schema(bodySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		assertThat(schema.get("properties").has("body")).isTrue();
		assertThat(schema.get("properties").get("body").get("type").asText()).isEqualTo("object");
		assertThat(schema.get("properties").get("body").has("properties")).isTrue();

		// Check required contains "body"
		boolean bodyRequired = false;
		for (JsonNode req : schema.get("required")) {
			if ("body".equals(req.asText())) {
				bodyRequired = true;
			}
		}
		assertThat(bodyRequired).isTrue();
	}

	/**
	 * Test that $ref schemas are resolved against Components.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testRefSchemaResolution() throws Exception {
		Components components = new Components();
		Schema<Object> userSchema = new Schema<>();
		userSchema.setType("object");
		userSchema.setProperties(Map.of("name", new StringSchema()));
		components.addSchemas("User", userSchema);

		Schema<?> refSchema = new Schema<>();
		refSchema.set$ref("#/components/schemas/User");

		Schema<?> resolved = OpenApiSchemaConverter.resolveSchema(refSchema, components);
		assertThat(resolved.getType()).isEqualTo("object");
		assertThat(resolved.getProperties()).containsKey("name");
	}

	/**
	 * Test that an operation with no parameters produces an empty schema.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testEmptyOperationProducesEmptySchema() throws Exception {
		Operation operation = new Operation();

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		assertThat(schema.get("type").asText()).isEqualTo("object");
		assertThat(schema.get("properties").isEmpty()).isTrue();
		assertThat(schema.get("required").isEmpty()).isTrue();
	}

	/**
	 * Test that undeclared path template variables are added as required string
	 * properties.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testUndeclaredPathVariablesAddedToSchema() throws Exception {
		Operation operation = new Operation();

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/api/v{api}/users", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		assertThat(schema.get("type").asText()).isEqualTo("object");
		assertThat(schema.get("properties").has("api")).isTrue();
		assertThat(schema.get("properties").get("api").get("type").asText()).isEqualTo("string");

		boolean apiRequired = false;
		for (JsonNode req : schema.get("required")) {
			if ("api".equals(req.asText())) {
				apiRequired = true;
			}
		}
		assertThat(apiRequired).isTrue();
	}

	/**
	 * Test that enum values and default are correctly propagated to the JSON Schema.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testEnumAndDefaultConvertedToSchema() throws Exception {
		Operation operation = new Operation();
		StringSchema versionSchema = new StringSchema();
		versionSchema.setDefault("1.0");
		versionSchema.setEnum(List.of("1.0", "v2"));
		Parameter versionParam = new Parameter().name("version").in("query").required(true).schema(versionSchema);
		operation.setParameters(List.of(versionParam));

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/api/users/list", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode versionNode = schema.get("properties").get("version");
		assertThat(versionNode.get("type").asText()).isEqualTo("string");
		assertThat(versionNode.get("default").asText()).isEqualTo("1.0");
		assertThat(versionNode.has("enum")).isTrue();
		assertThat(versionNode.get("enum")).hasSize(2);
		assertThat(versionNode.get("enum").get(0).asText()).isEqualTo("1.0");
		assertThat(versionNode.get("enum").get(1).asText()).isEqualTo("v2");
	}

	/**
	 * Test that declared path parameters are not duplicated by undeclared path variable
	 * detection.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testDeclaredPathParamsNotDuplicated() throws Exception {
		Operation operation = new Operation();
		Parameter pathParam = new Parameter().name("id").in("path").required(true).schema(new StringSchema());
		operation.setParameters(List.of(pathParam));

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/users/{id}", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		assertThat(schema.get("properties").has("id")).isTrue();
		int idCount = 0;
		for (JsonNode req : schema.get("required")) {
			if ("id".equals(req.asText())) {
				idCount++;
			}
		}
		assertThat(idCount).isEqualTo(1);
	}

	/**
	 * Test that response description is built from an array of $ref schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testResponseDescriptionArrayOfRef() {
		Components components = new Components();
		Schema<Object> userSchema = new Schema<>();
		userSchema.setType("object");
		userSchema.setProperties(
				Map.of("id", new Schema<>().type("integer"), "name", new StringSchema(), "email", new StringSchema()));
		components.addSchemas("UserDTOv1", userSchema);

		Schema<?> itemsRef = new Schema<>();
		itemsRef.set$ref("#/components/schemas/UserDTOv1");
		Schema<Object> arraySchema = new Schema<>();
		arraySchema.setType("array");
		arraySchema.setItems(itemsRef);

		MediaType mediaType = new MediaType().schema(arraySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		ApiResponse apiResponse = new ApiResponse().description("OK").content(content);
		ApiResponses responses = new ApiResponses();
		responses.addApiResponse("200", apiResponse);

		Operation operation = new Operation();
		operation.setResponses(responses);

		String description = OpenApiSchemaConverter.buildResponseDescription(operation, components);
		assertThat(description).startsWith("Returns an array of UserDTOv1");
		assertThat(description).contains("id");
		assertThat(description).contains("name");
		assertThat(description).contains("email");
	}

	/**
	 * Test that response description handles oneOf schemas.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testResponseDescriptionOneOf() {
		Components components = new Components();
		Schema<Object> v1Schema = new Schema<>();
		v1Schema.setType("object");
		v1Schema.setProperties(Map.of("id", new Schema<>().type("integer"), "name", new StringSchema()));
		components.addSchemas("V1", v1Schema);

		Schema<Object> v2Schema = new Schema<>();
		v2Schema.setType("object");
		v2Schema.setProperties(Map.of("id", new Schema<>().type("integer"), "firstName", new StringSchema()));
		components.addSchemas("V2", v2Schema);

		Schema<?> ref1 = new Schema<>();
		ref1.set$ref("#/components/schemas/V1");
		Schema<?> ref2 = new Schema<>();
		ref2.set$ref("#/components/schemas/V2");
		Schema<Object> oneOfSchema = new Schema<>();
		oneOfSchema.setOneOf(List.of(ref1, ref2));

		MediaType mediaType = new MediaType().schema(oneOfSchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		ApiResponse apiResponse = new ApiResponse().description("OK").content(content);
		ApiResponses responses = new ApiResponses();
		responses.addApiResponse("200", apiResponse);

		Operation operation = new Operation();
		operation.setResponses(responses);

		String description = OpenApiSchemaConverter.buildResponseDescription(operation, components);
		assertThat(description).startsWith("Returns ");
		assertThat(description).contains("V1");
		assertThat(description).contains("V2");
		assertThat(description).contains(" or ");
	}

	/**
	 * Test that response description returns empty string when no responses.
	 */
	@Test
	void testResponseDescriptionEmptyWhenNoResponses() {
		Operation operation = new Operation();
		String description = OpenApiSchemaConverter.buildResponseDescription(operation, null);
		assertThat(description).isEmpty();
	}

	/**
	 * Test that nested object properties are recursively converted with full type info.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testRecursiveNestedObjectConversion() throws Exception {
		Operation operation = new Operation();
		Schema<Object> addressSchema = new Schema<>();
		addressSchema.setType("object");
		addressSchema
			.setProperties(Map.of("street", new StringSchema().description("Street name"), "zip", new StringSchema()));

		Schema<Object> bodySchema = new Schema<>();
		bodySchema.setType("object");
		bodySchema.setProperties(Map.of("name", new StringSchema(), "address", addressSchema));

		MediaType mediaType = new MediaType().schema(bodySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode addressNode = schema.get("properties").get("body").get("properties").get("address");
		assertThat(addressNode.get("type").asText()).isEqualTo("object");
		assertThat(addressNode.has("properties")).isTrue();
		assertThat(addressNode.get("properties").has("street")).isTrue();
		assertThat(addressNode.get("properties").get("street").get("type").asText()).isEqualTo("string");
		assertThat(addressNode.get("properties").get("street").get("description").asText()).isEqualTo("Street name");
		assertThat(addressNode.get("properties").has("zip")).isTrue();
	}

	/**
	 * Test that $ref schemas in nested properties are resolved and recursively converted.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testRefInNestedPropertiesResolved() throws Exception {
		Components components = new Components();
		Schema<Object> addressSchema = new Schema<>();
		addressSchema.setType("object");
		addressSchema.setProperties(Map.of("city", new StringSchema(), "country", new StringSchema()));
		components.addSchemas("Address", addressSchema);

		Schema<?> addressRef = new Schema<>();
		addressRef.set$ref("#/components/schemas/Address");

		Schema<Object> bodySchema = new Schema<>();
		bodySchema.setType("object");
		bodySchema.setProperties(Map.of("name", new StringSchema(), "address", addressRef));

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(bodySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode addressNode = schema.get("properties").get("body").get("properties").get("address");
		assertThat(addressNode.get("type").asText()).isEqualTo("object");
		assertThat(addressNode.has("properties")).isTrue();
		assertThat(addressNode.get("properties").has("city")).isTrue();
		assertThat(addressNode.get("properties").has("country")).isTrue();
	}

	/**
	 * Test that array items with $ref are resolved and recursively converted.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testArrayItemsRefResolved() throws Exception {
		Components components = new Components();
		Schema<Object> itemSchema = new Schema<>();
		itemSchema.setType("object");
		itemSchema.setProperties(Map.of("id", new IntegerSchema(), "label", new StringSchema()));
		components.addSchemas("Item", itemSchema);

		Schema<?> itemRef = new Schema<>();
		itemRef.set$ref("#/components/schemas/Item");
		Schema<Object> arraySchema = new Schema<>();
		arraySchema.setType("array");
		arraySchema.setItems(itemRef);

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(arraySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyItems = schema.get("properties").get("body").get("items");
		assertThat(bodyItems.get("type").asText()).isEqualTo("object");
		assertThat(bodyItems.has("properties")).isTrue();
		assertThat(bodyItems.get("properties").has("id")).isTrue();
		assertThat(bodyItems.get("properties").has("label")).isTrue();
	}

	/**
	 * Test that the required array from nested object schemas is preserved.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testNestedRequiredArrayPreserved() throws Exception {
		Schema<Object> bodySchema = new Schema<>();
		bodySchema.setType("object");
		bodySchema.setProperties(Map.of("name", new StringSchema(), "email", new StringSchema()));
		bodySchema.setRequired(List.of("name", "email"));

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(bodySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.has("required")).isTrue();
		assertThat(bodyNode.get("required")).hasSize(2);

		boolean nameRequired = false;
		boolean emailRequired = false;
		for (JsonNode req : bodyNode.get("required")) {
			if ("name".equals(req.asText())) {
				nameRequired = true;
			}
			if ("email".equals(req.asText())) {
				emailRequired = true;
			}
		}
		assertThat(nameRequired).isTrue();
		assertThat(emailRequired).isTrue();
	}

	/**
	 * Test that allOf schemas are merged into a single object with combined properties.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testAllOfMergedIntoSingleObject() throws Exception {
		Components components = new Components();
		Schema<Object> baseSchema = new Schema<>();
		baseSchema.setType("object");
		baseSchema.setProperties(Map.of("id", new IntegerSchema()));
		baseSchema.setRequired(List.of("id"));
		components.addSchemas("Base", baseSchema);

		Schema<Object> extSchema = new Schema<>();
		extSchema.setType("object");
		extSchema.setProperties(Map.of("name", new StringSchema()));
		extSchema.setRequired(List.of("name"));
		components.addSchemas("Extension", extSchema);

		Schema<?> ref1 = new Schema<>();
		ref1.set$ref("#/components/schemas/Base");
		Schema<?> ref2 = new Schema<>();
		ref2.set$ref("#/components/schemas/Extension");
		Schema<Object> allOfSchema = new Schema<>();
		allOfSchema.setAllOf(List.of(ref1, ref2));

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(allOfSchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.get("type").asText()).isEqualTo("object");
		assertThat(bodyNode.get("properties").has("id")).isTrue();
		assertThat(bodyNode.get("properties").has("name")).isTrue();

		// Check merged required
		boolean idRequired = false;
		boolean nameRequired = false;
		for (JsonNode req : bodyNode.get("required")) {
			if ("id".equals(req.asText())) {
				idRequired = true;
			}
			if ("name".equals(req.asText())) {
				nameRequired = true;
			}
		}
		assertThat(idRequired).isTrue();
		assertThat(nameRequired).isTrue();
	}

	/**
	 * Test that oneOf schemas produce a JSON Schema oneOf array in input schema.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testOneOfInInputSchema() throws Exception {
		Components components = new Components();
		Schema<Object> catSchema = new Schema<>();
		catSchema.setType("object");
		catSchema.setProperties(Map.of("meow", new StringSchema()));
		components.addSchemas("Cat", catSchema);

		Schema<Object> dogSchema = new Schema<>();
		dogSchema.setType("object");
		dogSchema.setProperties(Map.of("bark", new StringSchema()));
		components.addSchemas("Dog", dogSchema);

		Schema<?> ref1 = new Schema<>();
		ref1.set$ref("#/components/schemas/Cat");
		Schema<?> ref2 = new Schema<>();
		ref2.set$ref("#/components/schemas/Dog");
		Schema<Object> oneOfSchema = new Schema<>();
		oneOfSchema.setOneOf(List.of(ref1, ref2));

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(oneOfSchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.has("oneOf")).isTrue();
		assertThat(bodyNode.get("oneOf")).hasSize(2);
		assertThat(bodyNode.get("oneOf").get(0).get("properties").has("meow")).isTrue();
		assertThat(bodyNode.get("oneOf").get(1).get("properties").has("bark")).isTrue();
	}

	/**
	 * Test that circular references are detected and broken with a simple object type.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testCircularReferenceDetection() throws Exception {
		Components components = new Components();

		// Create a self-referencing schema: Node { name, child -> Node }
		Schema<Object> nodeSchema = new Schema<>();
		nodeSchema.setType("object");
		Schema<?> selfRef = new Schema<>();
		selfRef.set$ref("#/components/schemas/Node");
		Map<String, Schema> nodeProps = new LinkedHashMap<>();
		nodeProps.put("name", new StringSchema());
		nodeProps.put("child", selfRef);
		nodeSchema.setProperties(nodeProps);
		components.addSchemas("Node", nodeSchema);

		Schema<?> bodyRef = new Schema<>();
		bodyRef.set$ref("#/components/schemas/Node");

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(bodyRef);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		// First level should have properties
		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.get("type").asText()).isEqualTo("object");
		assertThat(bodyNode.get("properties").has("name")).isTrue();
		assertThat(bodyNode.get("properties").has("child")).isTrue();

		// The child (circular) should just be { "type": "object" }
		JsonNode childNode = bodyNode.get("properties").get("child");
		assertThat(childNode.get("type").asText()).isEqualTo("object");
		assertThat(childNode.has("properties")).isFalse();
	}

	/**
	 * Test that validation constraints are correctly propagated to the JSON Schema.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testValidationConstraints() throws Exception {
		Operation operation = new Operation();
		IntegerSchema ageSchema = new IntegerSchema();
		ageSchema.setMinimum(BigDecimal.ZERO);
		ageSchema.setMaximum(BigDecimal.valueOf(150));
		ageSchema.setExclusiveMinimum(true);

		StringSchema nameSchema = new StringSchema();
		nameSchema.setMinLength(1);
		nameSchema.setMaxLength(100);
		nameSchema.setPattern("^[a-zA-Z]+$");

		Parameter ageParam = new Parameter().name("age").in("query").required(true).schema(ageSchema);
		Parameter nameParam = new Parameter().name("name").in("query").required(true).schema(nameSchema);
		operation.setParameters(List.of(ageParam, nameParam));

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode ageNode = schema.get("properties").get("age");
		assertThat(ageNode.get("type").asText()).isEqualTo("integer");
		assertThat(ageNode.get("minimum").intValue()).isEqualTo(0);
		assertThat(ageNode.get("maximum").intValue()).isEqualTo(150);
		assertThat(ageNode.get("exclusiveMinimum").asBoolean()).isTrue();

		JsonNode nameNode = schema.get("properties").get("name");
		assertThat(nameNode.get("type").asText()).isEqualTo("string");
		assertThat(nameNode.get("minLength").intValue()).isEqualTo(1);
		assertThat(nameNode.get("maxLength").intValue()).isEqualTo(100);
		assertThat(nameNode.get("pattern").asText()).isEqualTo("^[a-zA-Z]+$");
	}

	/**
	 * Test that nullable schemas produce a type array with "null".
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testNullableHandling() throws Exception {
		Operation operation = new Operation();
		StringSchema nullableSchema = new StringSchema();
		nullableSchema.setNullable(true);
		Parameter param = new Parameter().name("nickname").in("query").required(false).schema(nullableSchema);
		operation.setParameters(List.of(param));

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode nicknameNode = schema.get("properties").get("nickname");
		assertThat(nicknameNode.get("type").isArray()).isTrue();
		assertThat(nicknameNode.get("type").get(0).asText()).isEqualTo("string");
		assertThat(nicknameNode.get("type").get(1).asText()).isEqualTo("null");
	}

	/**
	 * Test that anyOf schemas produce a JSON Schema anyOf array in input schema.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testAnyOfInInputSchema() throws Exception {
		Components components = new Components();
		Schema<Object> s1 = new Schema<>();
		s1.setType("object");
		s1.setProperties(Map.of("x", new IntegerSchema()));
		components.addSchemas("S1", s1);

		Schema<Object> s2 = new Schema<>();
		s2.setType("object");
		s2.setProperties(Map.of("y", new IntegerSchema()));
		components.addSchemas("S2", s2);

		Schema<?> ref1 = new Schema<>();
		ref1.set$ref("#/components/schemas/S1");
		Schema<?> ref2 = new Schema<>();
		ref2.set$ref("#/components/schemas/S2");
		Schema<Object> anyOfSchema = new Schema<>();
		anyOfSchema.setAnyOf(List.of(ref1, ref2));

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(anyOfSchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, components);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.has("anyOf")).isTrue();
		assertThat(bodyNode.get("anyOf")).hasSize(2);
		assertThat(bodyNode.get("anyOf").get(0).get("properties").has("x")).isTrue();
		assertThat(bodyNode.get("anyOf").get(1).get("properties").has("y")).isTrue();
	}

	/**
	 * Test that minItems and maxItems constraints are applied to array schemas.
	 * @throws Exception if JSON parsing fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testArrayValidationConstraints() throws Exception {
		Schema<Object> arraySchema = new Schema<>();
		arraySchema.setType("array");
		arraySchema.setItems(new StringSchema());
		arraySchema.setMinItems(1);
		arraySchema.setMaxItems(10);

		Operation operation = new Operation();
		MediaType mediaType = new MediaType().schema(arraySchema);
		Content content = new Content().addMediaType("application/json", mediaType);
		RequestBody requestBody = new RequestBody().content(content).required(true);
		operation.setRequestBody(requestBody);

		String schemaJson = OpenApiSchemaConverter.buildInputSchema("/test", operation, null);
		JsonNode schema = objectMapper.readTree(schemaJson);

		JsonNode bodyNode = schema.get("properties").get("body");
		assertThat(bodyNode.get("type").asText()).isEqualTo("array");
		assertThat(bodyNode.get("minItems").intValue()).isEqualTo(1);
		assertThat(bodyNode.get("maxItems").intValue()).isEqualTo(10);
		assertThat(bodyNode.get("items").get("type").asText()).isEqualTo("string");
	}

}
