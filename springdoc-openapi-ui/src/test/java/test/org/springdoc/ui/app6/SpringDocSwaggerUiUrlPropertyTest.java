package test.org.springdoc.ui.app6;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "springdoc.swagger-ui.url=http://myserver:8080/v3/api-docs")
public class SpringDocSwaggerUiUrlPropertyTest extends AbstractSpringDocTest {

    @Test
    public void with_swagger_ui_property() throws Exception {
        mockMvc.perform(get("/v3/api-docs/swagger-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("url", equalTo("http://myserver:8080/v3/api-docs")));
    }
}