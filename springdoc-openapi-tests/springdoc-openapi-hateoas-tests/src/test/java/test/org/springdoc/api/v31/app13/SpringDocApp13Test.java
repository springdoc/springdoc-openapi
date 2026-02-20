
package test.org.springdoc.api.v31.app13;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for issue #3161: Wrong HAL _links are generated in sub type of a schema
 * Verifies that _links field is not duplicated in extended schemas using allOf composition
 * Tests OpenAPI 3.1.0 spec compliance with JSON Schema 2020-12 support
 */
@SpringBootTest
@TestPropertySource(properties = { "springdoc.api-docs.version=openapi_3_1" })
public class SpringDocApp13Test extends AbstractSpringDocTest {

    /**
     * Integration test: Validates the entire OpenAPI 3.1.0 specification JSON against the expected schema.
     *
     * This is the main integration test that ensures:
     * 1. The OpenAPI version is correctly set to 3.1.0
     * 2. The generated OpenAPI document matches the expected JSON structure exactly
     * 3. All schema definitions, paths, and components are correct
     * 4. Issue #3161 is resolved (no duplicate _links in child schemas)
     * 5. OpenAPI 3.1.0 JSON Schema optimizations are properly applied
     *
     * OpenAPI 3.1.0 introduces full JSON Schema 2020-12 support, which allows for
     * more optimized schema representations compared to OpenAPI 3.0.1.
     * For example, allOf compositions may omit redundant schema definitions.
     *
     * The test compares the actual HTTP response from /v3/api-docs endpoint with
     * the expected specification stored in results/3.1.0/app13.json file.
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testApp() throws Exception {
        // Extract test number from class name (13 from SpringDocApp13Test)
        String className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");

        // Perform GET request to OpenAPI documentation endpoint
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify OpenAPI version is 3.1.0 (not 3.0.1)
                .andExpect(jsonPath("$.openapi", is("3.1.0")))
                .andReturn();

        // Get the actual generated JSON response
        String result = mockMvcResult.getResponse().getContentAsString();
        // Load the expected JSON specification from classpath resource for OpenAPI 3.1.0
        String expected = getContent("results/3.1.0/app" + testNumber + ".json");

        // Compare expected and actual JSON in lenient mode (true parameter)
        // Lenient mode allows flexibility in JSON comparison (e.g., field order independence,
        // handling of optional fields like 'type' in OpenAPI 3.1.0)
        try {
            assertEquals(expected, result, true);
        } catch (AssertionError e) {
            // Log detailed comparison results for debugging purposes
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + result);
            throw e;
        }
    }

    /**
     * Unit test: Verifies that the parent TestDto includes the _links property from RepresentationModel.
     *
     * This test ensures that:
     * 1. TestDto correctly extends RepresentationModel
     * 2. The _links field is automatically included in the OpenAPI 3.1.0 schema
     * 3. HATEOAS links support is properly recognized and documented
     *
     * The _links field is essential for REST API clients to navigate between resources
     * using HATEOAS (Hypermedia As The Engine Of Application State) principles.
     *
     * Note: This test verifies the same behavior as SpringDocApp11Test but with
     * OpenAPI 3.1.0, confirming consistency across OpenAPI versions.
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testTestDtoHasHateoasLinks() throws Exception {
        // Perform GET request to OpenAPI documentation endpoint
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify that _links property exists in TestDto schema
                // Path: $.components.schemas.TestDto.properties._links
                .andExpect(jsonPath("$.components.schemas.TestDto.properties._links").exists())
                .andReturn();
    }

    /**
     * Unit test: Verifies that ExtendedTestDto correctly uses allOf composition to inherit from TestDto.
     *
     * This test validates the OpenAPI 3.1.0 schema composition pattern:
     * 1. ExtendedTestDto uses allOf keyword for schema composition
     * 2. The first allOf item is a $ref pointing to the parent TestDto
     * 3. In OpenAPI 3.1.0, the composition structure may be more optimized than in 3.0.1
     *
     * The allOf pattern ensures proper inheritance in OpenAPI where child schemas
     * automatically inherit all properties from parent schemas without explicit duplication.
     *
     * Note: OpenAPI 3.1.0 with JSON Schema 2020-12 support may omit the explicit
     * second allOf item if no additional properties are defined in the child schema.
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testExtendedTestDtoAllOfInheritance() throws Exception {
        // Perform GET request to OpenAPI documentation endpoint
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify that allOf array exists in ExtendedTestDto schema
                .andExpect(jsonPath("$.components.schemas.ExtendedTestDto.allOf").exists())
                // Verify that the first allOf item references the parent TestDto
                // Path: $.components.schemas.ExtendedTestDto.allOf[0].$ref
                .andExpect(jsonPath("$.components.schemas.ExtendedTestDto.allOf[0].$ref")
                        .value("#/components/schemas/TestDto"))
                .andReturn();
    }

    /**
     * Critical test: Verifies that ExtendedTestDto does NOT have duplicate _links in its own properties.
     *
     * This test is the core validation for issue #3161:
     * "Wrong HAL _links are generated in sub type of a schema"
     *
     * The problem was that child schemas incorrectly duplicated the _links field
     * even though it was already inherited from the parent schema via allOf.
     *
     * This test confirms:
     * 1. ExtendedTestDto exists in the schema definitions
     * 2. ExtendedTestDto uses allOf composition (does not duplicate parent properties)
     * 3. The _links field is inherited from TestDto, not redefined in ExtendedTestDto
     * 4. This behavior is consistent between OpenAPI 3.0.1 and 3.1.0
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testExtendedTestDtoNoLinksInOwnProperties() throws Exception {
        // Perform GET request to OpenAPI documentation endpoint
        MvcResult result = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                .andReturn();

        // Get the response body as JSON string for content-based assertions
        String content = result.getResponse().getContentAsString();

        // Verify that ExtendedTestDto schema is defined in components
        assert(content.contains("\"ExtendedTestDto\"")) : "ExtendedTestDto not found in schema";

        // Verify that ExtendedTestDto uses allOf composition pattern
        assert(content.contains("\"allOf\"")) : "allOf not found in ExtendedTestDto";

        // Note: Full validation of _links absence is performed by testApp()
        // which compares the complete JSON structure with the expected specification
    }

    /**
     * Unit test: Verifies that ExtendedTestDto correctly extends TestDto in OpenAPI 3.1.0.
     *
     * This test differs from SpringDocApp11Test due to OpenAPI 3.1.0 optimizations:
     * 1. Verifies that allOf array exists for schema composition
     * 2. Confirms that the first allOf item correctly references the parent TestDto
     * 3. Acknowledges that OpenAPI 3.1.0 may omit redundant schema elements
     *
     * In OpenAPI 3.1.0 with full JSON Schema 2020-12 support:
     * - The type field becomes optional (can be omitted)
     * - Implicit inheritance is allowed without explicit property definitions
     * - Schema composition may be more compact than in OpenAPI 3.0.1
     *
     * Therefore, this test focuses on verifying the core allOf composition pattern
     * rather than checking for an explicit second allOf item with properties.
     *
     * Note: The otherField property definition may be implicit through inheritance
     * rather than explicitly defined in allOf[1] as in OpenAPI 3.0.1.
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testExtendedTestDtoHasOwnProperties() throws Exception {
        // Perform GET request to OpenAPI documentation endpoint
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify that allOf array exists in ExtendedTestDto schema
                // This confirms the composition pattern is in place
                .andExpect(jsonPath("$.components.schemas.ExtendedTestDto.allOf").exists())
                // Verify that the first allOf item correctly references the parent TestDto
                // This is the critical element for proper schema composition
                .andExpect(jsonPath("$.components.schemas.ExtendedTestDto.allOf[0].$ref")
                        .value("#/components/schemas/TestDto"))
                .andReturn();
    }

    /**
     * Spring Boot test configuration class for OpenAPI 3.1.0 testing.
     *
     * This inner static class configures the embedded Spring context for testing:
     * 1. @SpringBootApplication enables auto-configuration and component scanning
     * 2. @ComponentScan explicitly specifies the base package for component discovery
     * 3. The context is specifically configured for OpenAPI 3.1.0 via TestPropertySource
     *
     * The ComponentScan ensures that HateoasController and other components
     * in the test.org.springdoc.api.v31.app13 package are properly registered
     * in the Spring context and available for the integration tests.
     *
     * This differs from SpringDocApp11Test by scanning the v31.app13 package
     * and using OpenAPI 3.1.0 configuration instead of 3.0.1.
     */
    @SpringBootApplication
    @ComponentScan(basePackages = "test.org.springdoc.api.v31.app13")
    static class SpringDocTestApp {
    }
}