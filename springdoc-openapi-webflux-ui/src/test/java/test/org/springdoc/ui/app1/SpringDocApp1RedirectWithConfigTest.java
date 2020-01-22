package test.org.springdoc.ui.app1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.org.springdoc.ui.AbstractSpringDocTest;

@TestPropertySource(properties = {
        "springdoc.swagger-ui.validatorUrl=/foo/validate",
        "springdoc.api-docs.path=/baf/batz"
})
public class SpringDocApp1RedirectWithConfigTest  extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Test
    public void shouldRedirectWithConfiguredParams() throws Exception {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isTemporaryRedirect();

        responseSpec.expectHeader()
                .value("Location", Matchers.is("/webjars/swagger-ui/index.html?configUrl=/baf/batz/swagger-config"));

        webTestClient.get().uri("/baf/batz/swagger-config").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.validatorUrl", "/foo/validate");
    }

}