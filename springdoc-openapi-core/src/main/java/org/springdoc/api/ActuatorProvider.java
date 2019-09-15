package org.springdoc.api;

import static org.springdoc.core.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = SPRINGDOC_SHOW_ACTUATOR, matchIfMissing = false)
@Component
public class ActuatorProvider {

	@Autowired
	private WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

	public WebMvcEndpointHandlerMapping getWebMvcHandlerMapping() {
		return webMvcEndpointHandlerMapping;
	}

}
