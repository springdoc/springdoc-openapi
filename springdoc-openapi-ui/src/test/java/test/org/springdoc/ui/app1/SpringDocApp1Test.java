package test.org.springdoc.ui.app1;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocApp1Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(contentAsString.contains("Swagger UI"));
    }

}