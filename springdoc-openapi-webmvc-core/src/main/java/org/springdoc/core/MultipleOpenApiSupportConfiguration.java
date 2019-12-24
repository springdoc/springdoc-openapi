package org.springdoc.core;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.ActuatorProvider;
import org.springdoc.api.MultipleOpenApiResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.List;
import java.util.Optional;


@Configuration
@ConditionalOnBean(GroupedOpenApi.class)
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

    @Bean
    public MultipleOpenApiResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
                                                           ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder, AbstractRequestBuilder requestBuilder,
                                                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                                                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
                                                           Optional<ActuatorProvider> servletContextProvider) {
        return new MultipleOpenApiResource(groupedOpenApis,
                defaultOpenAPIBuilder, requestBuilder,
                responseBuilder, operationParser,
                requestMappingHandlerMapping, servletContextProvider);
    }
}