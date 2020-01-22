package test.org.springdoc.ui.app1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.org.springdoc.ui.AbstractSpringDocTest;


@TestPropertySource(properties = {
        "springdoc.swagger-ui.configUrl=/foo/bar",
        "springdoc.swagger-ui.url=/batz" // ignored since configUrl is configured
})
public class SpringDocApp1RedirectConfigUrlTest  extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp {}

    @Test
    public void shouldRedirectWithConfigUrlIgnoringQueryParams() throws Exception {

        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/swagger-ui.html").exchange()
                .expectStatus().isTemporaryRedirect();
        responseSpec.expectHeader()
                .value("Location", Matchers.is("/webjars/swagger-ui/index.html?configUrl=/foo/bar"));

    }

}