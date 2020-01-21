package test.org.springdoc.ui;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
