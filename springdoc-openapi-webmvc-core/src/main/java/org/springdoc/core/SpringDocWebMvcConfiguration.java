package org.springdoc.core;

import org.springdoc.api.ActuatorProvider;
import org.springdoc.api.OpenApiResource;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocWebMvcConfiguration {

    @Bean
    public OpenApiResource openApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
                                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> servletContextProvider,
                                           Optional<List<OpenApiCustomiser>> openApiCustomisers) {
        return new OpenApiResource(openAPIBuilder, requestBuilder,
                responseBuilder, operationParser,
                requestMappingHandlerMapping, servletContextProvider,
                openApiCustomisers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ParameterBuilder parameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
        return new ParameterBuilder(localSpringDocParameterNameDiscoverer, ignoredParameterAnnotations);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestBuilder requestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                         OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<ParameterCustomizer>> parameterCustomizers) {
        return new RequestBuilder(parameterBuilder, requestBodyBuilder,
                operationBuilder, operationCustomizers, parameterCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseBuilder responseBuilder(OperationBuilder operationBuilder) {
        return new ResponseBuilder(operationBuilder);
    }

}
