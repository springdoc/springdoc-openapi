package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.org.springdoc.ui.AbstractSpringDocTest;


public class SpringDocApp1Test  extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isTemporaryRedirect();
    }

}