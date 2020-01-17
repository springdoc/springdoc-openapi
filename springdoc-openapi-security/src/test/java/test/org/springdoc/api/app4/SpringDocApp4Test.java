package test.org.springdoc.api.app4;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringDocApp4Test extends AbstractSpringDocTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void configurations_successfully_loaded() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("springSecurityOAuth2Provider")
                );
    }

    @SpringBootApplication(scanBasePackages = {"test.org.springdoc.api.configuration,test.org.springdoc.api.app4"})
    @EnableAuthorizationServer
    static class TestApp {
    }

}
