package test.org.springdoc.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@AutoConfigureMockMvc
public abstract class AbstractSpringDocTest {

    @Autowired
    protected MockMvc mockMvc;

    public static String className;

    protected String getExpectedResult() throws Exception {
        className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");
        Path path = Paths.get(getClass().getClassLoader().getResource("results/app" + testNumber).toURI());
        byte[] fileBytes = Files.readAllBytes(path);
       return new String(fileBytes);
    }
}
