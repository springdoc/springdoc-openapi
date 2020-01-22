package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.swagger-ui.path=/test/swagger.html")
public class SpringDocConfigPathsTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void should_display_swaggerui_page() throws Exception {
        mockMvc.perform(get("/context-path/servlet-path/test/swagger.html").contextPath("/context-path").servletPath("/servlet-path")).andExpect(status().isFound()).andReturn();
        MvcResult mvcResult = mockMvc.perform(get("/context-path/servlet-path/test/swagger-ui/index.html").contextPath("/context-path").servletPath("/servlet-path")).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(contentAsString.contains("Swagger UI"));
    }
}