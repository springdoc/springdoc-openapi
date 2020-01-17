package test.org.springdoc.api.app72;

import org.junit.Test;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockingAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void configurations_successfully_loaded() {
        contextRunner
                .withPropertyValues("springdoc.show-actuator=true")
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .hasBean("actuatorProvider")
                        .hasBean("multipleOpenApiResource")
                );
    }

    @Test
    public void configurations_not_loaded_when_application_is_not_web() {
        new ApplicationContextRunner()
                .withUserConfiguration(TestApp.class)
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource")
                        .doesNotHaveBean("actuatorProvider")
                        .doesNotHaveBean("multipleOpenApiResource")
                );
    }

    @Test
    public void actuator_configuration_not_loaded_when_not_enabled_explicitly() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .doesNotHaveBean("actuatorPprrovider")
                        .hasBean("multipleOpenApiResource")
                );
    }

    @Test
    public void configurations_not_loaded_when_disabled() {
        contextRunner
                .withPropertyValues("springdoc.api-docs.enabled=false")
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource")
                        .doesNotHaveBean("actuatorProvider")
                        .doesNotHaveBean("multipleOpenApiResource")
                );
    }

    @Test
    public void configurations_not_loaded_when_mvc_is_not_on_class_path() {
        contextRunner
                .withClassLoader(new FilteredClassLoader("org.springframework.web.context.support.GenericWebApplicationContext"))
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .doesNotHaveBean("openApiResource")
                        .doesNotHaveBean("actuatorProvider")
                        .doesNotHaveBean("multipleOpenApiResource")
                );

    }

    @SpringBootApplication
    static class TestApp {
        @Bean
        GroupedOpenApi testGroupedOpenApi() {
            return GroupedOpenApi.builder()
                    .setGroup("test-group")
                    .packagesToScan("org.test")
                    .build();
        }
    }
}