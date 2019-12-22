package org.springdoc.core;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_GROUPS_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_GROUPS_ENABLED)
public class MultipleOpenApiSupportConfiguration {
    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class)) {
                beanFactory.getBeanDefinition(beanName).setScope("prototype");
            }
            for (String beanName : beanFactory.getBeanNamesForType(OpenAPIBuilder.class)) {
                beanFactory.getBeanDefinition(beanName).setScope("prototype");
            }
        };
    }
}