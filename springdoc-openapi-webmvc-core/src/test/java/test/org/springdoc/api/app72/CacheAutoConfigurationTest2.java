package test.org.springdoc.api.app72;

import org.junit.Test;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheAutoConfigurationTest2 {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void cache_configuration_loaded_when_not_disabled_explicitly() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .doesNotHaveBean("beanFactoryPostProcessor2")
                );
    }

    @Test
    public void cache_configuration_loaded_when_disabled_explicitly() {
        contextRunner
                .withPropertyValues("springdoc.cache.disabled=false")
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .doesNotHaveBean("beanFactoryPostProcessor2")
                );
    }

    @Test
    public void cache_configurations_successfully_disabled() {
        contextRunner
                .withPropertyValues("springdoc.cache.disabled=true")
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .hasBean("beanFactoryPostProcessor1")
                        .doesNotHaveBean("beanFactoryPostProcessor2")
                );
    }



    @EnableAutoConfiguration
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