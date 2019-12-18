package test.org.springdoc.ui.app1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "springdoc.swagger-ui.validatorUrl=/foo/validate",
        "springdoc.api-docs.path=/baf/batz"
})
public class SpringDocApp1RedirectWithConfigTest extends AbstractSpringDocTest {

    @Test
    public void shouldRedirectWithConfiguredParams() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isFound()).andReturn();

        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertEquals("/swagger-ui/index.html?url=/baf/batz&validatorUrl=/foo/validate", locationHeader);
    }

}