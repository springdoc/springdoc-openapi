package test.org.springdoc.ui.app2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

@TestPropertySource(properties = "springdoc.swagger-ui.enabled=false")
public class SpringDocApp2Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isNotFound();
    }


}