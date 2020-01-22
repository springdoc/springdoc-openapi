package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocRedirectDefaultTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void shouldRedirectWithDefaultQueryParams() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isFound()).andReturn();

        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertEquals("/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config", locationHeader);
    }

}