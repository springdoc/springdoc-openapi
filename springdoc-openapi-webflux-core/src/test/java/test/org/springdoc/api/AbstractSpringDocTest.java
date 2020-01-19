package test.org.springdoc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.api.OpenApiResource;
import org.springdoc.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = {OpenApiResource.class})
@ActiveProfiles("test")
public abstract class AbstractSpringDocTest {

    protected String groupName = "";
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testApp() throws Exception {
        EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + groupName).exchange()
                .expectStatus().isOk().expectBody().returnResult();

        String result = new String(getResult.getResponseBody());
        String className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");

        Path path = Paths.get(getClass().getClassLoader().getResource("results/app" + testNumber + ".json").toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        String expected = new String(fileBytes);

        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(result));
    }

}
