package org.springdoc.core;

import org.springdoc.api.ActuatorProvider;
import org.springdoc.api.OpenApiCustomiser;
import org.springdoc.api.OpenApiResource;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(DispatcherServlet.class)
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

    @Configuration
    @ConditionalOnProperty(name = SPRINGDOC_SHOW_ACTUATOR)
    @ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
    public static class SpringDocWebMvcActuatorConfiguration {

        @Bean
        public ActuatorProvider actuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
            return new ActuatorProvider(webMvcEndpointHandlerMapping);
        }

    }

}
