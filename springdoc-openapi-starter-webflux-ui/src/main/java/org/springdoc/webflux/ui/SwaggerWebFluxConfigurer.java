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

package org.springdoc.webflux.ui;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webflux.autoconfigure.WebFluxProperties;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import static org.springdoc.core.utils.Constants.SWAGGER_INITIALIZER_PATTERN;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PREFIX;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_WEBJAR_NAME;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_WEBJAR_NAME_PATTERN;
import static org.springdoc.core.utils.Constants.WEBJARS_RESOURCE_LOCATION;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger web flux configurer.
 *
 * @author bnasslahsen
 */
public class SwaggerWebFluxConfigurer implements WebFluxConfigurer {

	/**
	 * The Swagger index transformer.
	 */
	private final SwaggerIndexTransformer swaggerIndexTransformer;

	/**
	 * The Swagger resource resolver.
	 */
	private final SwaggerResourceResolver swaggerResourceResolver;

	/**
	 * The Swagger ui config properties.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	/**
	 * The Spring Web config properties.
	 */
	private final WebProperties springWebProperties;

	/**
	 * The Spring WebFlux config properties.
	 */
	private final WebFluxProperties springWebFluxProperties;

	/**
	 * The Swagger welcome common.
	 */
	private final SwaggerWelcomeCommon swaggerWelcomeCommon;

	private final PathPatternParser parser = new PathPatternParser();

	/**
	 * Instantiates a new Swagger web flux configurer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui calculated config
	 * @param springWebProperties       the spring web config
	 * @param springWebFluxProperties   the spring webflux config
	 * @param swaggerIndexTransformer   the swagger index transformer
	 * @param swaggerResourceResolver   the swagger resource resolver
	 * @param swaggerWelcomeCommon   the swagger welcome common
	 */
	public SwaggerWebFluxConfigurer(SwaggerUiConfigProperties swaggerUiConfigProperties,
			WebProperties springWebProperties, WebFluxProperties springWebFluxProperties,
			SwaggerIndexTransformer swaggerIndexTransformer, SwaggerResourceResolver swaggerResourceResolver,
			SwaggerWelcomeCommon swaggerWelcomeCommon) {
		this.swaggerIndexTransformer = swaggerIndexTransformer;
		this.swaggerResourceResolver = swaggerResourceResolver;
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
		this.springWebProperties = springWebProperties;
		this.springWebFluxProperties = springWebFluxProperties;
		this.swaggerWelcomeCommon = swaggerWelcomeCommon;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String swaggerUiPattern = getUiRootPath() + SWAGGER_UI_PREFIX + ALL_PATTERN;
		String swaggerUiResourceLocation = WEBJARS_RESOURCE_LOCATION + SWAGGER_UI_WEBJAR_NAME + DEFAULT_PATH_SEPARATOR +
				swaggerUiConfigProperties.getVersion() + DEFAULT_PATH_SEPARATOR;

		addSwaggerUiResourceHandler(registry, swaggerUiPattern,  swaggerUiResourceLocation);

		// Add custom mappings for Swagger UI WebJar resources if Spring resource mapping is enabled
		if (springWebProperties.getResources().isAddMappings()) {
			String webjarsPathPattern = springWebFluxProperties.getWebjarsPathPattern();

			String swaggerUiWebjarPattern = mergePatterns(webjarsPathPattern, SWAGGER_UI_WEBJAR_NAME_PATTERN) + ALL_PATTERN;
			String swaggerUiWebjarResourceLocation = WEBJARS_RESOURCE_LOCATION;

			addSwaggerUiResourceHandler(registry, swaggerUiWebjarPattern, swaggerUiWebjarResourceLocation);
		}
	}

	/**
	 * Adds the resource handlers for serving the Swagger UI resources.
	 */
	protected void addSwaggerUiResourceHandler(ResourceHandlerRegistry registry, String pattern, String... resourceLocations) {
		registry.addResourceHandler(pattern)
				.addResourceLocations(resourceLocations)
				.resourceChain(false)
				.addResolver(swaggerResourceResolver)
				.addTransformer(swaggerIndexTransformer);

		// Ensure Swagger initializer has "no-store" Cache-Control header
		registry.addResourceHandler(mergePatterns(pattern, SWAGGER_INITIALIZER_PATTERN))
				.setCacheControl(CacheControl.noStore())
				.addResourceLocations(resourceLocations)
				.resourceChain(false)
				.addResolver(swaggerResourceResolver)
				.addTransformer(swaggerIndexTransformer);
	}

	/**
	 * Computes and returns the root path for the Swagger UI.
	 *
	 * @return the Swagger UI root path.
	 */
	protected String getUiRootPath() {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfigProperties);
		swaggerWelcomeCommon.calculateUiRootPath(swaggerUiConfigParameters);

		return swaggerUiConfigParameters.getUiRootPath();
	}

	/**
	 * Combines two patterns into a new pattern according to the rules of {@link PathPattern#combine}.
	 *
	 * <p>For example:
	 * <ul>
	 * <li><code>/webjars/&#42;&#42;</code> + <code>/swagger-ui/&#42;&#42;</code> => <code>/webjars/swagger-ui/&#42;&#42;</code></li>
	 * <li><code>/documentation/swagger-ui&#42;/&#42;&#42;</code> + <code>/&#42;.js</code> => <code>/documentation/swagger-ui&#42;/&#42;.js</code></li>
	 * </ul>
	 *
	 * @param pattern1 the first pattern
	 * @param pattern2 the second pattern
	 *
	 * @return the combination of the two patterns
	 *
	 * @see PathPattern#combine
	 */
	private String mergePatterns(String pattern1, String pattern2) {
		PathPattern pathPattern1 = parser.parse(parser.initFullPathPattern(pattern1));
		PathPattern pathPattern2 = parser.parse(parser.initFullPathPattern(pattern2));

		return pathPattern1.combine(pathPattern2).getPatternString();
	}
}
