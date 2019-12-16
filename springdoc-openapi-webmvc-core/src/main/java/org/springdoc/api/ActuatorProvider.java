package org.springdoc.api;

import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;

public class ActuatorProvider {

    private final WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

    public ActuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
        this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping;
    }

    public WebMvcEndpointHandlerMapping getWebMvcHandlerMapping() {
        return webMvcEndpointHandlerMapping;
    }

}
