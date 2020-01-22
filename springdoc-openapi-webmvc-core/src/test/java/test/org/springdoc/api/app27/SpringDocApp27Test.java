package test.org.springdoc.api.app27;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocApp27Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andExpect(jsonPath("$.paths./test.get.responses.500.content.['*/*'].schema.oneOf").isArray()).andExpect(jsonPath("$.paths./test.get.responses.500.content.['*/*'].schema.oneOf[*].$ref", containsInAnyOrder("#/components/schemas/Bar",
                "#/components/schemas/Foo")));
    }
}