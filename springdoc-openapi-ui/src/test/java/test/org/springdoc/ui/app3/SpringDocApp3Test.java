package test.org.springdoc.ui.app3;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import test.org.springdoc.ui.AbstractSpringDocTest;

public class SpringDocApp3Test extends AbstractSpringDocTest {

    @Test
    public void appLoads() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.forwardedUrl("index.html"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/index.html"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE));
    }
}
