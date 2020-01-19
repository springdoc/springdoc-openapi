package org.springdoc.core;

import org.springdoc.api.MultipleOpenApiResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import java.util.List;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;


@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnBean(GroupedOpenApi.class)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class MultipleOpenApiWebFluxConfiguration {

    @Bean
    public MultipleOpenApiResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
                                                           ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder, AbstractRequestBuilder requestBuilder,
                                                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                                                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping) {
        return new MultipleOpenApiResource(groupedOpenApis,
                defaultOpenAPIBuilder, requestBuilder,
                responseBuilder, operationParser,
                requestMappingHandlerMapping);
    }
}