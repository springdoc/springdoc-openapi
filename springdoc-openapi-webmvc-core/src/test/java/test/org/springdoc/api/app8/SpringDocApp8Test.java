package test.org.springdoc.api.app8;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SpringDocApp8Test extends AbstractSpringDocTest {

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get("/myapp" + Constants.DEFAULT_API_DOCS_URL).contextPath("/myapp"))
                .andExpect(status().isOk());
    }
    @SpringBootApplication
    static class SpringDocTestApp { }
}
