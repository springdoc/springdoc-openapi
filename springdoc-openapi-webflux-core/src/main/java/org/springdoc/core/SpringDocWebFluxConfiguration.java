package org.springdoc.core;

import org.springdoc.api.OpenApiCustomiser;
import org.springdoc.api.OpenApiResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(Mono.class)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocWebFluxConfiguration {

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
    public OpenApiResource openApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
                                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
                                           Optional<List<OpenApiCustomiser>> openApiCustomisers) {
        return new OpenApiResource(openAPIBuilder, requestBuilder,
                responseBuilder, operationParser,
                requestMappingHandlerMapping,
                openApiCustomisers);
    }

    @Bean
    public ParameterBuilder parameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
        return new ParameterBuilder(localSpringDocParameterNameDiscoverer, ignoredParameterAnnotations);
    }

    @Bean
    public OperationBuilder operationBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                             SecurityParser securityParser, OpenAPIBuilder openAPIBuilder, PropertyResolverUtils propertyResolverUtils) {
        return new OperationBuilder(parameterBuilder, requestBodyBuilder,
                securityParser, openAPIBuilder, propertyResolverUtils);
    }

    @Bean
    public RequestBodyBuilder requestBodyBuilder(AbstractParameterBuilder parameterBuilder) {
        return new RequestBodyBuilder(parameterBuilder);
    }

    @Bean
    public RequestBuilder requestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                         OperationBuilder operationBuilder) {
        return new RequestBuilder(parameterBuilder, requestBodyBuilder,
                operationBuilder);
    }

    @Bean
    public ResponseBuilder responseBuilder(OperationBuilder operationBuilder) {
        return new ResponseBuilder(operationBuilder);
    }

}
