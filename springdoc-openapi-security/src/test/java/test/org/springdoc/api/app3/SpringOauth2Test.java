package test.org.springdoc.api.app3;

import org.junit.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringOauth2Test {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void configurations_successfully_loaded() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("springSecurityOAuth2Provider")
                );
    }

    @SpringBootConfiguration
    static class TestApp {
    }

}
