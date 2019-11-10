package org.springdoc.api;

import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

@ConditionalOnProperty(name = SPRINGDOC_SHOW_ACTUATOR)
@Component
class ActuatorProvider {

    private final WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

    public ActuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
        this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping;
    }

    public WebMvcEndpointHandlerMapping getWebMvcHandlerMapping() {
        return webMvcEndpointHandlerMapping;
    }

}
