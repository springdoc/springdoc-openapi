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

package org.springdoc.core.providers;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.configuration.SpringDocWebServerConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.utils.Constants;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;

import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * The type Actuator provider.
 *
 * @author bnasslahsen
 */
public abstract class ActuatorProvider implements ApplicationContextAware {

	/**
	 * The Management server properties.
	 */
	protected ManagementServerProperties managementServerProperties;

	/**
	 * The Web endpoint properties.
	 */
	protected WebEndpointProperties webEndpointProperties;


	/**
	 * The server context
         */
	protected SpringDocWebServerConfiguration.SpringDocWebServerContext springDocWebServerContext;

	/**
	 * The Spring doc config properties.
	 */
	protected SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Application context.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * Instantiates a new Actuator provider.
	 *
	 * @param managementServerProperties the management server properties
	 * @param webEndpointProperties      the web endpoint properties
	 * @param springDocWebServerContext  the server context
	 * @param springDocConfigProperties  the spring doc config properties
	 */
	protected ActuatorProvider(Optional<ManagementServerProperties> managementServerProperties,
			Optional<WebEndpointProperties> webEndpointProperties,
			SpringDocWebServerConfiguration.SpringDocWebServerContext springDocWebServerContext,
			SpringDocConfigProperties springDocConfigProperties) {

		managementServerProperties.ifPresent(managementServerProperties1 -> this.managementServerProperties = managementServerProperties1);
		webEndpointProperties.ifPresent(webEndpointProperties1 -> this.webEndpointProperties = webEndpointProperties1);

		this.springDocConfigProperties = springDocConfigProperties;
		this.springDocWebServerContext = springDocWebServerContext;
	}

	/**
	 * Gets tag.
	 *
	 * @return the tag
	 */
	public static Tag getTag() {
		Tag actuatorTag = new Tag();
		actuatorTag.setName(Constants.SPRINGDOC_ACTUATOR_TAG);
		actuatorTag.setDescription(Constants.SPRINGDOC_ACTUATOR_DESCRIPTION);
		actuatorTag.setExternalDocs(
				new ExternalDocumentation()
						.url(Constants.SPRINGDOC_ACTUATOR_DOC_URL)
						.description(Constants.SPRINGDOC_ACTUATOR_DOC_DESCRIPTION)
		);
		return actuatorTag;
	}


	/**
	 * Is rest controller boolean.
	 *
	 * @param operationPath the operation path
	 * @param handlerMethod the handler method
	 * @return the boolean
	 */
	public boolean isRestController(String operationPath, HandlerMethod handlerMethod) {
		return operationPath.startsWith(AntPathMatcher.DEFAULT_PATH_SEPARATOR)
				&& !AbstractOpenApiResource.isHiddenRestControllers(handlerMethod.getBeanType())
				&& AbstractOpenApiResource.containsResponseBody(handlerMethod);
	}

	/**
	 * Is use management port boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUseManagementPort() {
		return springDocConfigProperties.isUseManagementPort();
	}

	/**
	 * Gets base path.
	 *
	 * @return the base path
	 */
	public String getBasePath() {
		return webEndpointProperties.getBasePath();
	}

	/**
	 * Gets context path.
	 *
	 * @return the context path
	 */
	public String getContextPath() {
		return EMPTY;
	}

	/**
	 * Gets actuator path.
	 *
	 * @return the actuator path
	 */
	public String getActuatorPath() {
		return managementServerProperties.getBasePath();
	}

	/**
	 * Gets application port.
	 *
	 * @return the application port
	 */
	public int getApplicationPort() {
		return this.springDocWebServerContext.getApplicationPort().get();
	}

	/**
	 * Gets actuator port.
	 *
	 * @return the actuator port
	 */
	public int getActuatorPort() {
		return this.springDocWebServerContext.getActuatorPort().get();
	}

	/**
	 * Gets methods.
	 *
	 * @return the methods
	 */
	public abstract Map getMethods();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
