package test.org.springdoc.ui.app4;

import org.junit.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocApp4Test extends AbstractSpringDocTest {

    @Test
    public void swagger_config_for_multiple_groups() throws Exception {
        mockMvc.perform(get("/v3/api-docs/swagger-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("configUrl", equalTo("/v3/api-docs/swagger-config")))
                .andExpect(jsonPath("url").doesNotExist())
                .andExpect(jsonPath("urls[0].url", equalTo("/v3/api-docs/stores")))
                .andExpect(jsonPath("urls[0].name", equalTo("stores")))
                .andExpect(jsonPath("urls[1].url", equalTo("/v3/api-docs/pets")))
                .andExpect(jsonPath("urls[1].name", equalTo("pets")));
    }
}