package test.org.springdoc.api.app36;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource("classpath:/properties/36.properties")
public class SpringDocApp36Test {


    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andExpect(jsonPath("$.paths./actuator/info.get.operationId", containsString("handle"))).andExpect(jsonPath("$.paths./actuator/health.get.operationId", containsString("handle")));
    }

}