package test.org.springdoc.api.app5.sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.api-docs.path=/api-docs")
public class OpenApiResourceNoConfigurationTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    /**
     * givenNoConfiguration_whenGetApiJson_returnsDefaultEmptyDocs - should return
     * {"openapi":"3.0.1","info":{"title":"OpenAPI definition","version":"v0"},"paths":{},"components":{}}
     */
    @Test
    public void testApp() throws Exception {
        mockMvc
                .perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1")))
                .andExpect(jsonPath("$.info.title", is("OpenAPI definition")))
                .andExpect(jsonPath("$.info.version", is("v0")))
                .andExpect(jsonPath("$.paths").isEmpty())
                .andExpect(jsonPath("$.components").isEmpty());
    }
}
