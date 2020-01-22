package test.org.springdoc.ui;

import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SpringDocWebFluxConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.SwaggerWelcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("test")
@WebFluxTest
@ContextConfiguration(classes = {SpringDocConfiguration.class, SpringDocWebFluxConfiguration.class, SwaggerUiConfigProperties.class, SwaggerWelcome.class})
public abstract class AbstractSpringDocTest {


    @Autowired
    protected WebTestClient webTestClient;


}
