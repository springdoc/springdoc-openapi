package test.org.springdoc.api.app27;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SpringDocApp27Test {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andExpect(jsonPath("$.paths./test.get.responses.500.content.['*/*'].schema.oneOf").isArray()).andExpect(jsonPath("$.paths./test.get.responses.500.content.['*/*'].schema.oneOf[*].$ref", containsInAnyOrder("#/components/schemas/Bar",
                "#/components/schemas/Foo")));
    }
}