package test.org.springdoc.api;

import nonapi.io.github.classgraph.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebFluxTest
@ActiveProfiles("test")
public abstract class AbstractSpringDocTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void testApp() throws Exception {
        EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
                .expectStatus().isOk().expectBody().returnResult();

        String result = new String(getResult.getResponseBody());
        String className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");

        String expected = getContent("results/app" + testNumber + ".json");
        JSONAssert.assertEquals(expected, result, true);
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