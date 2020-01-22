package test.org.springdoc.ui;

import nonapi.io.github.classgraph.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractSpringDocTest {

    @Autowired
    protected MockMvc mockMvc;

    public static String className;

    protected String getExpectedResult() throws Exception {
        className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");
       return  getContent("results/app" + testNumber );
    }

    public static String getContent(String fileName) throws Exception {
        try {
            Path path = Paths.get(FileUtils.class.getClassLoader().getResource(fileName).toURI());
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }
}
