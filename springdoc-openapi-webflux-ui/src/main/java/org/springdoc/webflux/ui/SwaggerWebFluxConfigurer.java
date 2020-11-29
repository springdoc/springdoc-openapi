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

package org.springdoc.webflux.ui;

import java.util.Optional;

import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static org.springdoc.core.Constants.ALL_PATTERN;
import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger web flux configurer.
 * @author bnasslahsen
 */
public class SwaggerWebFluxConfigurer implements WebFluxConfigurer {

	/**
	 * The Swagger path.
	 */
	private String swaggerPath;

	/**
	 * The Web jars prefix url.
	 */
	private String webJarsPrefixUrl;

	/**
	 * The Swagger index transformer.
	 */
	private SwaggerIndexTransformer swaggerIndexTransformer;

	/**
	 * The Actuator provider.
	 */
	private Optional<ActuatorProvider> actuatorProvider;

	/**
	 * Instantiates a new Swagger web flux configurer.
	 *
	 * @param swaggerUiConfigParameters the swagger ui calculated config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerIndexTransformer the swagger index transformer
	 * @param actuatorProvider the actuator provider
	 */
	public SwaggerWebFluxConfigurer(SwaggerUiConfigParameters swaggerUiConfigParameters,
			SpringDocConfigProperties springDocConfigProperties,
			SwaggerIndexTransformer swaggerIndexTransformer,
			Optional<ActuatorProvider> actuatorProvider) {
		this.swaggerPath = swaggerUiConfigParameters.getPath();
		this.webJarsPrefixUrl = springDocConfigProperties.getWebjars().getPrefix();
		this.swaggerIndexTransformer = swaggerIndexTransformer;
		this.actuatorProvider = actuatorProvider;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		if (actuatorProvider.isPresent() && actuatorProvider.get().isUseManagementPort())
			uiRootPath.append(actuatorProvider.get().getBasePath());
		registry.addResourceHandler(uiRootPath + webJarsPrefixUrl + ALL_PATTERN)
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);
	}

}
