/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.webflux.core;

import java.util.Map;

import org.springdoc.core.ActuatorProvider;

import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;


/**
 * The type Web flux actuator provider.
 * @author bnasslahsen
 */
public class WebFluxActuatorProvider implements ActuatorProvider {

	/**
	 * The Web flux endpoint handler mapping.
	 */
	private final WebFluxEndpointHandlerMapping webFluxEndpointHandlerMapping;

	/**
	 * Instantiates a new Web flux actuator provider.
	 *
	 * @param webFluxEndpointHandlerMapping the web flux endpoint handler mapping
	 */
	public WebFluxActuatorProvider(WebFluxEndpointHandlerMapping webFluxEndpointHandlerMapping) {
		this.webFluxEndpointHandlerMapping = webFluxEndpointHandlerMapping;
	}

	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		return webFluxEndpointHandlerMapping.getHandlerMethods();
	}
}
