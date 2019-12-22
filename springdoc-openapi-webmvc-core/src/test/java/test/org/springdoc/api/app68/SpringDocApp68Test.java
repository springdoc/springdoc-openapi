package test.org.springdoc.api.app68;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import test.org.springdoc.api.AbstractSpringDocTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "springdoc.api-docs.groups.enabled=true")
public class SpringDocApp68Test {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringDocTest.class);

    public static String className;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testApp1() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/stores")).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app681.json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);
        assertEquals(expected, result, true);
    }

    @Test
    public void testApp2() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app682.json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);
        assertEquals(expected, result, true);
    }

    @Test
    public void testApp3() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/pets")).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app683.json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);
        assertEquals(expected, result, true);
    }

    @Test
    public void testApp4() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/groups")).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app684.json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);
        assertEquals(expected, result, true);
    }
}
