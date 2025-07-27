package test.org.springdoc.api.v31.app245;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "springdoc.version=v1",
    "git.build.time=2025-07-08T12:00:00Z"
})
@AutoConfigureMockMvc
class SpringDocApp245Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void SpringDocTestApp() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.info.version", is("v1")))
            .andExpect(jsonPath("$.info.x-build-time", is("2025-07-08T12:00:00Z")));
    }
}
