package org.springdoc.core;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.converters.ObjectNodeConverter;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_CACHE_DISABLED;
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
    PropertyCustomizingConverter propertyCustomizingConverter(Optional<List<PropertyCustomizer>> customizers) {
        return new PropertyCustomizingConverter(customizers);
    }

    @Bean
    IgnoredParameterAnnotationsDefault ignoredParameterAnnotationsDefault() {
        return new IgnoredParameterAnnotationsDefault();
    }

    @Bean
    public OpenAPIBuilder openAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityParser securityParser,Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider) {
        return new OpenAPIBuilder(openAPI, context, securityParser,springSecurityOAuth2Provider);
    }

    @Bean
    public ModelConverterRegistrar modelConverterRegistrar(Optional<List<ModelConverter>> modelConverters) {
        return new ModelConverterRegistrar(modelConverters.orElse(Collections.emptyList()));
    }

    @Bean
    @ConditionalOnWebApplication
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
    @ConditionalOnWebApplication
    public RequestBodyBuilder requestBodyBuilder(AbstractParameterBuilder parameterBuilder) {
        return new RequestBodyBuilder(parameterBuilder);
    }

    @Bean
    public SecurityParser securityParser(PropertyResolverUtils propertyResolverUtils) {
        return new SecurityParser(propertyResolverUtils);
    }

    static class ConditionOnCacheOrGroupedOpenApi extends AnyNestedCondition {

        ConditionOnCacheOrGroupedOpenApi() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @Bean
        @ConditionalOnBean(GroupedOpenApi.class)
        public BeanFactoryPostProcessor beanFactoryPostProcessor1() {
            return getBeanFactoryPostProcessor();
        }

        @Bean
        @ConditionalOnProperty(name = SPRINGDOC_CACHE_DISABLED)
        @ConditionalOnMissingBean(GroupedOpenApi.class)
        public BeanFactoryPostProcessor beanFactoryPostProcessor2() {
            return getBeanFactoryPostProcessor();
        }

        private BeanFactoryPostProcessor getBeanFactoryPostProcessor() {
            return beanFactory -> {
                for (String beanName : beanFactory.getBeanNamesForType(OpenAPIBuilder.class)) {
                    beanFactory.getBeanDefinition(beanName).setScope("prototype");
                }
                for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class)) {
                    beanFactory.getBeanDefinition(beanName).setScope("prototype");
                }
            };
        }
    }
}
