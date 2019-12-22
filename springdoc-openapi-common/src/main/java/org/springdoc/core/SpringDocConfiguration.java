package org.springdoc.core;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.converters.ObjectNodeConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.util.List;
import java.util.Optional;

@Configuration
public class SpringDocConfiguration {

    @Bean
    LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
        return new LocalVariableTableParameterNameDiscoverer();
    }

    @Bean
    ObjectNodeConverter objectNodeConverter() {
        return new ObjectNodeConverter();
    }

    @Bean
    IgnoredParameterAnnotationsDefault ignoredParameterAnnotationsDefault() {
        return new IgnoredParameterAnnotationsDefault();
    }

    @Bean
    public OpenAPIBuilder openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityParser securityParser) {
        return new OpenAPIBuilder(openAPI, context, securityParser);
    }

    @Bean
    public OperationBuilder operationBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                             SecurityParser securityParser, OpenAPIBuilder openAPIBuilder, PropertyResolverUtils propertyResolverUtils) {
        return new OperationBuilder(parameterBuilder, requestBodyBuilder,
                securityParser, openAPIBuilder, propertyResolverUtils);
    }

    @Bean
    public PropertyResolverUtils propertyResolverUtils(ConfigurableBeanFactory factory) {
        return new PropertyResolverUtils(factory);
    }

    @Bean
    public RequestBodyBuilder requestBodyBuilder(AbstractParameterBuilder parameterBuilder) {
        return new RequestBodyBuilder(parameterBuilder);
    }

    @Bean
    public SecurityParser securityParser(PropertyResolverUtils propertyResolverUtils) {
        return new SecurityParser(propertyResolverUtils);
    }

    @Bean
    public SpringDocAnnotationsUtils springDocAnnotationsUtils(List<ModelConverter> modelConverters) {
        return new SpringDocAnnotationsUtils(modelConverters);
    }

}
