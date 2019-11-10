package test.org.springdoc.api.app44;

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
public class SpringDocApp44Test {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andExpect(jsonPath("$.paths./helloworld.post.responses.200.content.['application/json'].schema.oneOf").isArray()).andExpect(jsonPath("$.paths./helloworld.post.responses.200.content.['application/json'].schema.oneOf[*].$ref", containsInAnyOrder("#/components/schemas/HelloDTO2",
                "#/components/schemas/HelloDTO1")))
                .andExpect(jsonPath("$.paths./helloworld.post.requestBody.content.['application/vnd.v1+json'].schema.$ref", is("#/components/schemas/RequestV1")))
                .andExpect(jsonPath("$.paths./helloworld.post.requestBody.content.['application/vnd.v2+json'].schema.$ref", is("#/components/schemas/RequestV2")))
                .andExpect(jsonPath("$.paths./helloworld.post.responses.400.content.['application/json'].schema.$ref", is("#/components/schemas/ErrorDTO")));

    }

}