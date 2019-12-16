package org.springdoc.core;

import org.springdoc.core.converters.PageableSupportConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocDataRestConfiguration {

    @Bean
    PageableSupportConverter pageableSupportConverter() {
        return new PageableSupportConverter();
    }

}
