package test.org.springdoc.ui.app5;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.swagger-ui.oauth2RedirectUrl=http://localhost:8080/toto/test/swagger-ui/oauth2-redirect.html")
public class SpringDocOauthPathsWithPropertyTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void oauth2_redirect_url_calculated() throws Exception {
        mockMvc.perform(get("/v3/api-docs/swagger-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost:8080/toto/test/swagger-ui/oauth2-redirect.html")));
    }

}