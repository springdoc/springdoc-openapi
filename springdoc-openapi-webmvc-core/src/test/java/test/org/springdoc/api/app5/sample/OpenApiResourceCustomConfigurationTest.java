package test.org.springdoc.api.app5.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import test.org.springdoc.api.app5.CustomOpenAPIConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(CustomOpenAPIConfig.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "springdoc.api-docs.path=/api-docs")
@AutoConfigureMockMvc
public class OpenApiResourceCustomConfigurationTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Autowired
    private MockMvc mockMvc;

    /**
     * should return
     * {"openapi":"3.0.1","info":{"title":"Custom API","version":"100"},"paths":{},"components":{}}
     */
    @Test
    public void givenNoConfiguration_whenGetApiJson_returnsDefaultEmptyDocs() throws Exception {
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
