package test.org.springdoc.ui.app5;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties ="server.servlet.context-path=/context-path")
public class SpringDocOauthContextPathTest extends AbstractSpringDocTest {

    @Test
    public void oauth2_redirect_url_calculated_with_context_path() throws Exception {
        mockMvc.perform(get("/context-path/v3/api-docs/swagger-config").contextPath("/context-path"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/context-path/swagger-ui/oauth2-redirect.html")));
    }
}