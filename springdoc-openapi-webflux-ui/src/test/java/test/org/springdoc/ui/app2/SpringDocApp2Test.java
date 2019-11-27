package test.org.springdoc.ui.app2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.SwaggerWelcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest(properties = "springdoc.swagger-ui.enabled=false")
@ActiveProfiles("test")
@ContextConfiguration(classes = {SwaggerWelcome.class, SwaggerUiConfigProperties.class})
public class SpringDocApp2Test  {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isNotFound();
    }


}