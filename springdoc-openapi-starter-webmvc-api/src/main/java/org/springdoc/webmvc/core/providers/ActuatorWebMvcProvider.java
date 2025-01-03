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

package org.springdoc.webmvc.core.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ActuatorProvider;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * The type Web mvc actuator provider.
 *
 * @author bnasslahsen
 */
public class ActuatorWebMvcProvider extends ActuatorProvider {

	/**
	 * Instantiates a new Actuator web mvc provider.
	 *
	 * @param serverProperties           the server properties
	 * @param springDocConfigProperties  the spring doc config properties
	 * @param managementServerProperties the management server properties
	 * @param webEndpointProperties      the web endpoint properties
	 */
	public ActuatorWebMvcProvider(ServerProperties serverProperties,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ManagementServerProperties> managementServerProperties,
			Optional<WebEndpointProperties> webEndpointProperties) {
		super(managementServerProperties, webEndpointProperties, serverProperties, springDocConfigProperties);
	}

	@Override
	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		Map<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodMap = new HashMap<>();

		WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping = applicationContext.getBeansOfType(WebMvcEndpointHandlerMapping.class).values().stream().findFirst().orElse(null);
		if (webMvcEndpointHandlerMapping == null)
			webMvcEndpointHandlerMapping = managementApplicationContext.getBean(WebMvcEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(webMvcEndpointHandlerMapping.getHandlerMethods());

		ControllerEndpointHandlerMapping controllerEndpointHandlerMapping = applicationContext.getBeansOfType(ControllerEndpointHandlerMapping.class).values().stream().findFirst().orElse(null);
		if (controllerEndpointHandlerMapping == null)
			controllerEndpointHandlerMapping = managementApplicationContext.getBean(ControllerEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(controllerEndpointHandlerMapping.getHandlerMethods());

		return mappingInfoHandlerMethodMap;
	}

	@Override
	public String getContextPath() {
		return StringUtils.defaultIfEmpty(serverProperties.getServlet().getContextPath(), EMPTY);
	}

}