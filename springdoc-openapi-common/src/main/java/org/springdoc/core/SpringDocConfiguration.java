package org.springdoc.core;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.converters.ObjectNodeConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
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
    public OpenAPIBuilder openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityParser securityParser, List<ModelConverter> modelConverters) {
        return new OpenAPIBuilder(openAPI, context, securityParser,modelConverters);
    }

    @Bean
    public PropertyResolverUtils propertyResolverUtils(ConfigurableBeanFactory factory) {
        return new PropertyResolverUtils(factory);
    }

    @Bean
    public SecurityParser securityParser(PropertyResolverUtils propertyResolverUtils) {
        return new SecurityParser(propertyResolverUtils);
    }

}
