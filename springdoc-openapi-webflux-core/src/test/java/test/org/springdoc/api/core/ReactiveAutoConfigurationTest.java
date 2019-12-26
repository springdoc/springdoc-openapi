package test.org.springdoc.api.core;

import org.junit.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveAutoConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void configurations_successfully_loaded() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource"));
    }

    @Test
    public void configurations_not_loaded_when_application_is_not_web() {
        new ApplicationContextRunner()
                .withUserConfiguration(TestApp.class)
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource"));
    }

    @Test
    public void configurations_not_loaded_when_disabled() {
        contextRunner
                .withPropertyValues("springdoc.api-docs.enabled=false")
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource"));
    }

    @Test
    public void configurations_not_loaded_when_reactor_is_not_on_class_path() {
        contextRunner
                .withClassLoader(new FilteredClassLoader("reactor.core"))
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource"));

    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    protected static class TestApp {
    }
}