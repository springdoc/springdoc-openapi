/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.webflux.core.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ActuatorProvider;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.reactive.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;


/**
 * The type Web flux actuator provider.
 *
 * @author bnasslahsen
 */
public class ActuatorWebFluxProvider extends ActuatorProvider implements ApplicationContextAware {

	
	/**
	 * Instantiates a new Actuator web flux provider.
	 *
	 * @param serverProperties                 the server properties
	 * @param springDocConfigProperties        the spring doc config properties
	 * @param managementServerProperties       the management server properties
	 * @param webEndpointProperties            the web endpoint properties
	 */
	public ActuatorWebFluxProvider(ServerProperties serverProperties,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ManagementServerProperties> managementServerProperties,
			Optional<WebEndpointProperties> webEndpointProperties) {
		super(managementServerProperties, webEndpointProperties, serverProperties, springDocConfigProperties);
	}
	
	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		Map<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodMap = new HashMap<>();

		WebFluxEndpointHandlerMapping webFluxEndpointHandlerMapping = applicationContext.getBeansOfType(WebFluxEndpointHandlerMapping.class).values().stream().findFirst().orElse(null);
		if (webFluxEndpointHandlerMapping == null)
			webFluxEndpointHandlerMapping = managementApplicationContext.getBean(WebFluxEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(webFluxEndpointHandlerMapping.getHandlerMethods());

		ControllerEndpointHandlerMapping controllerEndpointHandlerMapping = applicationContext.getBeansOfType(ControllerEndpointHandlerMapping.class).values().stream().findFirst().orElse(null);
		if (controllerEndpointHandlerMapping == null)
			controllerEndpointHandlerMapping = managementApplicationContext.getBean(ControllerEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(controllerEndpointHandlerMapping.getHandlerMethods());

		return mappingInfoHandlerMethodMap;
	}

}
