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

import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;


/**
 * The type Web mvc actuator provider.
 * @author bnasslahsen
 */
public class WebMvcActuatorProvider implements ActuatorProvider, ApplicationListener<ServletWebServerInitializedEvent> {

	/**
	 * The Web mvc endpoint handler mapping.
	 */
	private WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

	/**
	 * The Controller endpoint handler mapping.
	 */
	private ControllerEndpointHandlerMapping controllerEndpointHandlerMapping;

	private AnnotationConfigServletWebServerApplicationContext managementApplicationContext;

	private AnnotationConfigServletWebServerApplicationContext applicationContext;

	private ManagementServerProperties managementServerProperties;

	private WebEndpointProperties webEndpointProperties;

	private SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Web mvc actuator provider.
	 *
	 * @param webMvcEndpointHandlerMapping the web mvc endpoint handler mapping
	 * @param controllerEndpointHandlerMapping the controller endpoint handler mapping
	 */
	public WebMvcActuatorProvider(Optional<WebMvcEndpointHandlerMapping> webMvcEndpointHandlerMapping,
			Optional<ControllerEndpointHandlerMapping> controllerEndpointHandlerMapping,
			ManagementServerProperties managementServerProperties,
			WebEndpointProperties webEndpointProperties,
			SpringDocConfigProperties springDocConfigProperties) {
		webMvcEndpointHandlerMapping.ifPresent(webMvcEndpointHandlerMapping1 -> this.webMvcEndpointHandlerMapping = webMvcEndpointHandlerMapping1);
		controllerEndpointHandlerMapping.ifPresent(controllerEndpointHandlerMapping1 -> this.controllerEndpointHandlerMapping = controllerEndpointHandlerMapping1);
		this.managementServerProperties = managementServerProperties;
		this.springDocConfigProperties = springDocConfigProperties;
		this.webEndpointProperties = webEndpointProperties;
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


	public int getApplicationPort() {
		return applicationContext.getWebServer().getPort();
	}

	@Override
	public int getActuatorPort() {
		return managementApplicationContext.getWebServer().getPort();
	}

	@Override
	public String getActuatorPath() {
		return managementServerProperties.getBasePath();
	}

	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		if ("application".equals(event.getApplicationContext().getId()))
			applicationContext = (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext();
		else if ("application:management".equals(event.getApplicationContext().getId()))
			managementApplicationContext = (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext();
	}

	@Override
	public boolean isUseManagementPort() {
		return springDocConfigProperties.isUseManagementPort();
	}

	@Override
	public String getBasePath() {
		return webEndpointProperties.getBasePath();
	}

	@Override
	public String getContextPath() {
		return applicationContext.getServletContext().getContextPath();
	}


}
