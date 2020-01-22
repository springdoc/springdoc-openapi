package test.org.springdoc.ui.app7;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.swagger-ui.oauth.clientId=myClientId")
public class SpringDocApp7Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void transformed_index_with_oauth() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk()).andReturn();
        String transformedIndex = mvcResult.getResponse().getContentAsString();
        assertTrue(transformedIndex.contains("Swagger UI"));
        assertEquals(this.getExpectedResult(), transformedIndex);
    }

}