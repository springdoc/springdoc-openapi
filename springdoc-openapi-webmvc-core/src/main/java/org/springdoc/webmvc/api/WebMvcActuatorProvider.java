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

package org.springdoc.webmvc.api;

import java.util.Map;

import org.springdoc.core.ActuatorProvider;

import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;


/**
 * The type Web mvc actuator provider.
 * @author bnasslahsen
 */
public class WebMvcActuatorProvider implements ActuatorProvider {

	/**
	 * The Web mvc endpoint handler mapping.
	 */
	private final WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

	/**
	 * Instantiates a new Web mvc actuator provider.
	 *
	 * @param webMvcEndpointHandlerMapping the web mvc endpoint handler mapping
	 */
	public WebMvcActuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
		this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping;
	}

	@Override
	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		return webMvcEndpointHandlerMapping.getHandlerMethods();
	}

}
