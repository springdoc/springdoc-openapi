/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.webflux.ui;

import java.util.Optional;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.providers.ActuatorProvider;

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import static org.springdoc.core.utils.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.utils.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger web flux configurer.
 * @author bnasslahsen
 */
public class SwaggerWebFluxConfigurer implements WebFluxConfigurer {

	/**
	 * The Swagger path.
	 */
	private final String swaggerPath;

	/**
	 * The Swagger index transformer.
	 */
	private final SwaggerIndexTransformer swaggerIndexTransformer;

	/**
	 * The Actuator provider.
	 */
	private final Optional<ActuatorProvider> actuatorProvider;

	/**
	 * The Swagger resource resolver.
	 */
	private final SwaggerResourceResolver swaggerResourceResolver;

	/**
	 * Instantiates a new Swagger web flux configurer.
	 *
	 * @param swaggerUiConfigParameters the swagger ui calculated config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerIndexTransformer the swagger index transformer
	 * @param actuatorProvider the actuator provider
	 * @param swaggerResourceResolver the swagger resource resolver
	 */
	public SwaggerWebFluxConfigurer(SwaggerUiConfigParameters swaggerUiConfigParameters,
			SpringDocConfigProperties springDocConfigProperties,
			SwaggerIndexTransformer swaggerIndexTransformer,
			Optional<ActuatorProvider> actuatorProvider, SwaggerResourceResolver swaggerResourceResolver) {
		this.swaggerPath = swaggerUiConfigParameters.getPath();
		this.swaggerIndexTransformer = swaggerIndexTransformer;
		this.actuatorProvider = actuatorProvider;
		this.swaggerResourceResolver = swaggerResourceResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		if (actuatorProvider.isPresent() && actuatorProvider.get().isUseManagementPort())
			uiRootPath.append(actuatorProvider.get().getBasePath());
		registry.addResourceHandler(uiRootPath + ALL_PATTERN)
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addResolver(swaggerResourceResolver)
				.addTransformer(swaggerIndexTransformer);
	}

}
