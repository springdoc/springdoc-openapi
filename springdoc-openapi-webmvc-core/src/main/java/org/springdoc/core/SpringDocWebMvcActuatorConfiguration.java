package org.springdoc.core;

import org.springdoc.api.ActuatorProvider;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SHOW_ACTUATOR)
public class SpringDocWebMvcActuatorConfiguration {

    @Bean
    public ActuatorProvider actuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
        return new ActuatorProvider(webMvcEndpointHandlerMapping);
    }

}
