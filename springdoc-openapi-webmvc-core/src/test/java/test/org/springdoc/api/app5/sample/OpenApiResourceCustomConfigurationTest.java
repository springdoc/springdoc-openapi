package test.org.springdoc.api.app5.sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;
import test.org.springdoc.api.app5.CustomOpenAPIConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CustomOpenAPIConfig.class)
@TestPropertySource(properties = "springdoc.api-docs.path=/api-docs")
public class OpenApiResourceCustomConfigurationTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    /**
     * givenNoConfiguration_whenGetApiJson_returnsDefaultEmptyDocs -  should return
     * {"openapi":"3.0.1","info":{"title":"Custom API","version":"100"},"paths":{},"components":{}}
     */
    @Test
    public void testApp() throws Exception {
        mockMvc
                .perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1")))
                .andExpect(jsonPath("$.info.title", is("Custom API")))
                .andExpect(jsonPath("$.info.version", is("100")))
                .andExpect(jsonPath("$.paths").isEmpty())
                .andExpect(jsonPath("$.components").isEmpty())
                .andExpect(jsonPath("$.tags").isNotEmpty())
                .andExpect(jsonPath("$.tags[0].name", is("mytag")));
    }
}
