package test.org.springdoc.ui.app5;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "springdoc.swagger-ui.path=/test/swagger.html",
        "spring.mvc.servlet.path=/servlet-path"
})
public class SpringDocOauthServletPathsTest extends AbstractSpringDocTest {

    @Test
    public void should_display_oauth2_redirect_page() throws Exception {
        mockMvc.perform(get("/context-path/servlet-path/test/swagger-ui/oauth2-redirect.html").contextPath("/context-path").servletPath("/servlet-path")).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void oauth2_redirect_url_calculated_with_context_path_and_servlet_path() throws Exception {
        mockMvc.perform(get("/context-path/servlet-path/v3/api-docs/swagger-config").contextPath("/context-path").servletPath("/servlet-path"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/context-path/servlet-path/test/swagger-ui/oauth2-redirect.html")));
    }
}