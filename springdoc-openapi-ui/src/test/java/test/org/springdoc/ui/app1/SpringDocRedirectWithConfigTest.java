package test.org.springdoc.ui.app1;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "springdoc.swagger-ui.validatorUrl=/foo/validate",
        "springdoc.api-docs.path=/baf/batz"
})
public class SpringDocRedirectWithConfigTest extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void shouldRedirectWithConfiguredParams() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isFound()).andReturn();

        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertEquals("/swagger-ui/index.html?configUrl=/baf/batz/swagger-config", locationHeader);

        mvcResult = mockMvc.perform(get("/baf/batz/swagger-config"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.validatorUrl", is("/foo/validate"))).andReturn();

    }

}