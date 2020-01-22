package test.org.springdoc.ui.app2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "springdoc.swagger-ui.enabled=false")
public class SpringDocApp2Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }

    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isNotFound());
    }

}