package org.springdoc.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

@ConditionalOnProperty(name = SPRINGDOC_SHOW_ACTUATOR, matchIfMissing = false)
@Component
class ActuatorProvider {

    @Autowired
    private WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

    public WebMvcEndpointHandlerMapping getWebMvcHandlerMapping() {
        return webMvcEndpointHandlerMapping;
    }

}
