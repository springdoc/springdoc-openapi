package test.org.springdoc.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = {"test.org.springdoc.aop"})
@AutoConfigureMockMvc
public abstract class AbstractSpringDocTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringDocTest.class);

    public static String className;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testApp() throws Exception {
        className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");
        MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
        String result = mockMvcResult.getResponse().getContentAsString();
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app" + testNumber + ".json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);
        assertEquals(expected, result, true);
    }

}
