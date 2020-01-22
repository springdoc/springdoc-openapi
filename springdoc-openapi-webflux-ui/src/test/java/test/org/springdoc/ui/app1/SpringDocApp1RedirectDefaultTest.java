package test.org.springdoc.ui.app1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.org.springdoc.ui.AbstractSpringDocTest;


public class SpringDocApp1RedirectDefaultTest  extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Test
    public void shouldRedirectWithDefaultQueryParams() throws Exception {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isTemporaryRedirect();
        responseSpec.expectHeader()
                .value("Location", Matchers.is("/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config"));

    }

}