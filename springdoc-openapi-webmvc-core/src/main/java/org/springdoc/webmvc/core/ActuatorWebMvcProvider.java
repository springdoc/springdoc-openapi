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

package org.springdoc.webmvc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;

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
 * @author bnasslahsen
 */
public class ActuatorWebMvcProvider extends ActuatorProvider {

	private WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

	private ControllerEndpointHandlerMapping controllerEndpointHandlerMapping;

	public ActuatorWebMvcProvider(ServerProperties serverProperties,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ManagementServerProperties> managementServerProperties,
			Optional<WebEndpointProperties> webEndpointProperties,
			Optional<WebMvcEndpointHandlerMapping> webMvcEndpointHandlerMapping,
			Optional<ControllerEndpointHandlerMapping> controllerEndpointHandlerMapping) {
		super(managementServerProperties, webEndpointProperties, serverProperties, springDocConfigProperties);
		webMvcEndpointHandlerMapping.ifPresent(webMvcEndpointHandlerMapping1 -> this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping1);
		controllerEndpointHandlerMapping.ifPresent(controllerEndpointHandlerMapping1 -> this.controllerEndpointHandlerMapping = controllerEndpointHandlerMapping1);
	}

	@Override
	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		Map<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodMap = new HashMap<>();

		if (webMvcEndpointHandlerMapping == null)
			webMvcEndpointHandlerMapping = managementApplicationContext.getBean(WebMvcEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(webMvcEndpointHandlerMapping.getHandlerMethods());

		if (controllerEndpointHandlerMapping == null)
			controllerEndpointHandlerMapping = managementApplicationContext.getBean(ControllerEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(controllerEndpointHandlerMapping.getHandlerMethods());

		return mappingInfoHandlerMethodMap;
	}

	@Override
	public String getContextPath() {
		return StringUtils.defaultIfEmpty(serverProperties.getServlet().getContextPath(),EMPTY);
	}

}
