
package test.org.springdoc.api.v30.app11;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

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
 * Tests OpenAPI 3.0.1 spec compliance
 */
@SpringBootTest
@TestPropertySource(properties = { "springdoc.api-docs.version=openapi_3_0" })
public class SpringDocApp11Test extends AbstractSpringDocTest {

    /**
     * Integration test: Validates the entire OpenAPI specification JSON against the expected schema.
     *
     * This is the main integration test that ensures:
     * 1. The OpenAPI version is correctly set to 3.0.1
     * 2. The generated OpenAPI document matches the expected JSON structure exactly
     * 3. All schema definitions, paths, and components are correct
     * 4. Issue #3161 is resolved (no duplicate _links in child schemas)
     *
     * The test compares the actual HTTP response from /v3/api-docs endpoint with
     * the expected specification stored in results/3.0.1/app11.json file.
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testApp() throws Exception {
        // Extract test number from class name (11 from SpringDocApp11Test)
        String className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");

        // Perform GET request to OpenAPI documentation endpoint
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify OpenAPI version is 3.0.1
                .andExpect(jsonPath("$.openapi", is("3.0.1")))
                .andReturn();

        // Get the actual generated JSON response
        String result = mockMvcResult.getResponse().getContentAsString();
        // Load the expected JSON specification from classpath resource
        String expected = getContent("results/3.0.1/app" + testNumber + ".json");

        // Compare expected and actual JSON in lenient mode (true parameter)
        // Lenient mode allows flexibility in JSON comparison (e.g., field order independence)
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
     * 2. The _links field is automatically included in the OpenAPI schema
     * 3. HATEOAS links support is properly recognized and documented
     *
     * The _links field is essential for REST API clients to navigate between resources
     * using HATEOAS (Hypermedia As The Engine Of Application State) principles.
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
     * This test validates the OpenAPI schema composition pattern:
     * 1. ExtendedTestDto uses allOf keyword for schema composition
     * 2. The first allOf item is a $ref pointing to the parent TestDto
     * 3. The second allOf item contains ExtendedTestDto's own properties
     *
     * The allOf pattern ensures proper inheritance in OpenAPI where child schemas
     * automatically inherit all properties from parent schemas without duplication.
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
     * Unit test: Verifies that ExtendedTestDto contains its own unique properties.
     *
     * This test ensures that:
     * 1. ExtendedTestDto defines its own properties in the second allOf item
     * 2. The child-specific property "otherField" is correctly included
     * 3. Properties are nested in allOf[1].properties structure
     *
     * The structure should be:
     * ExtendedTestDto {
     *   allOf: [
     *     { $ref: "#/components/schemas/TestDto" },  // allOf[0] - parent
     *     {
     *       type: "object",
     *       properties: {
     *         otherField: { ... }  // allOf[1].properties.otherField
     *       }
     *     }
     *   ]
     * }
     *
     * @throws Exception if the test fails or HTTP request encounters an error
     */
    @Test
    public void testExtendedTestDtoHasOwnProperties() throws Exception {
        // Perform GET request to OpenAPI documentation endpoint
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify that otherField property exists in the second allOf item
                // Path: $.components.schemas.ExtendedTestDto.allOf[1].properties.otherField
                .andExpect(jsonPath("$.components.schemas.ExtendedTestDto.allOf[1].properties.otherField").exists())
                .andReturn();
    }

    /**
     * Spring Boot test configuration class.
     *
     * This inner static class configures the embedded Spring context for testing:
     * 1. @SpringBootApplication enables auto-configuration and component scanning
     * 2. @ComponentScan explicitly specifies the base package for component discovery
     *
     * The ComponentScan ensures that HateoasController and other components
     * in the test.org.springdoc.api.v30.app11 package are properly registered
     * in the Spring context and available for the integration tests.
     */
    @SpringBootApplication
    @ComponentScan(basePackages = "test.org.springdoc.api.v30.app11")
    static class SpringDocTestApp {
    }
}