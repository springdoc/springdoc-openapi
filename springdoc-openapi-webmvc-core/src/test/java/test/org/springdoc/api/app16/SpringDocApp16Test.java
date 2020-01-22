package test.org.springdoc.api.app16;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.api-docs.enabled=false")
public class SpringDocApp16Test extends AbstractSpringDocTest {

    @SpringBootConfiguration
    static class SpringDocTestApp { }

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
                .andExpect(status().isNotFound());
    }
}