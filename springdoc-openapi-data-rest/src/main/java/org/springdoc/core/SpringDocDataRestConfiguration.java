package org.springdoc.core;

import org.springdoc.core.converters.PageableSupportConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocDataRestConfiguration {

    @Bean
    PageableSupportConverter pageableSupportConverter() {
        return new PageableSupportConverter();
    }

}
