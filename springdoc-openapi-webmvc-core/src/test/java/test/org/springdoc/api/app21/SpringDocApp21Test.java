package test.org.springdoc.api.app21;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.AbstractSpringDocTest;

@TestPropertySource(properties = "springdoc.oAuthFlow.authorizationUrl=http://personstore.swagger.io/oauth/dialog")
public class SpringDocApp21Test extends AbstractSpringDocTest {

    @SpringBootApplication
    static class SpringDocTestApp { }
}