package test.org.springdoc.api.app72;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheAutoConfigurationTest1 {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    public void cache_configuration_loaded_when_not_disabled_explicitly() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .doesNotHaveBean("beanFactoryPostProcessor1")
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
                        .doesNotHaveBean("beanFactoryPostProcessor1")
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
                        .hasBean("beanFactoryPostProcessor2")
                        .doesNotHaveBean("beanFactoryPostProcessor1")
                );
    }



    @EnableAutoConfiguration
    static class TestApp {

    }
}