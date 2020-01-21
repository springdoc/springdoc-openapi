package org.springdoc.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocSecurityConfiguration {

    @Bean
    @Primary
    IgnoredParameterAnnotationsWithSecurity ignoredParameterAnnotationsWithSecurity() {
        return new IgnoredParameterAnnotationsWithSecurity();
    }

    @Bean
    IgnoredParameterTypes ignoredParameterTypes() {
        return new IgnoredParameterTypes();
    }

    @Configuration
    @ConditionalOnBean(FrameworkEndpointHandlerMapping.class)
    class SpringSecurityOAuth2ProviderConfiguration {
        @Bean
        public SpringSecurityOAuth2Provider springSecurityOAuth2Provider(FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping) {
            return new SpringSecurityOAuth2Provider(oauth2EndpointHandlerMapping);
        }
    }
}
