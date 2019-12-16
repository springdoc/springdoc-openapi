package org.springdoc.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
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

}
