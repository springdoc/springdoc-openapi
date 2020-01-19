package test.org.springdoc.ui.app1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SpringDocWebFluxConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.SwaggerWelcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {SpringDocConfiguration.class, SpringDocWebFluxConfiguration.class, SwaggerUiConfigProperties.class, SwaggerWelcome.class})
public class SpringDocApp1Test {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isTemporaryRedirect();
    }

}