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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.reactive.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.reactive.context.ReactiveWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;


/**
 * The type Web flux actuator provider.
 * @author bnasslahsen
 */
public class WebFluxActuatorProvider implements ActuatorProvider, ApplicationListener<ReactiveWebServerInitializedEvent> {

	/**
	 * The Web flux endpoint handler mapping.
	 */
	private WebFluxEndpointHandlerMapping webFluxEndpointHandlerMapping;

	/**
	 * The Controller endpoint handler mapping.
	 */
	private ControllerEndpointHandlerMapping controllerEndpointHandlerMapping;


	private AnnotationConfigReactiveWebServerApplicationContext managementApplicationContext;

	private AnnotationConfigReactiveWebServerApplicationContext applicationContext;

	private ManagementServerProperties managementServerProperties;

	private WebEndpointProperties webEndpointProperties;

	private SpringDocConfigProperties springDocConfigProperties;


	/**
	 * Instantiates a new Web flux actuator provider.
	 *
	 * @param webFluxEndpointHandlerMapping the web flux endpoint handler mapping
	 * @param controllerEndpointHandlerMapping the controller endpoint handler mapping
	 */
	public WebFluxActuatorProvider(Optional<WebFluxEndpointHandlerMapping> webFluxEndpointHandlerMapping,
			Optional<ControllerEndpointHandlerMapping> controllerEndpointHandlerMapping,
			Optional<ManagementServerProperties> managementServerProperties,
			Optional<WebEndpointProperties> webEndpointProperties,
			SpringDocConfigProperties springDocConfigProperties) {
		webFluxEndpointHandlerMapping.ifPresent(webFluxEndpointHandlerMapping1 -> this.webFluxEndpointHandlerMapping = webFluxEndpointHandlerMapping1);
		controllerEndpointHandlerMapping.ifPresent(controllerEndpointHandlerMapping1 -> this.controllerEndpointHandlerMapping = controllerEndpointHandlerMapping1);
		managementServerProperties.ifPresent(managementServerProperties1 -> this.managementServerProperties=managementServerProperties1);
		webEndpointProperties.ifPresent(webEndpointProperties1 -> this.webEndpointProperties=webEndpointProperties1);
		this.springDocConfigProperties = springDocConfigProperties;

	}

	public Map<RequestMappingInfo, HandlerMethod> getMethods() {
		Map<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodMap = new HashMap<>();

		if (webFluxEndpointHandlerMapping == null)
			webFluxEndpointHandlerMapping = managementApplicationContext.getBean(WebFluxEndpointHandlerMapping.class);
		mappingInfoHandlerMethodMap.putAll(webFluxEndpointHandlerMapping.getHandlerMethods());

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
	public void onApplicationEvent(ReactiveWebServerInitializedEvent event) {
		if ("application".equals(event.getApplicationContext().getId()))
			applicationContext = (AnnotationConfigReactiveWebServerApplicationContext) event.getApplicationContext();
		else if ("application:management".equals(event.getApplicationContext().getId()))
			managementApplicationContext = (AnnotationConfigReactiveWebServerApplicationContext) event.getApplicationContext();

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
	public String getServletContextPath() {
		return "";
	}

}
