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

package org.springdoc.ui;


import java.util.Arrays;

import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.util.pattern.PatternParseException;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import static org.springdoc.core.utils.Constants.SWAGGER_INITIALIZER_PATTERN;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PREFIX;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_WEBJAR_NAME;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_WEBJAR_NAME_PATTERN;
import static org.springdoc.core.utils.Constants.WEBJARS_RESOURCE_LOCATION;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Abstract swagger configurer.
 */
public abstract class AbstractSwaggerConfigurer {

	/**
	 * The Swagger ui config properties.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	/**
	 * The Spring Web config properties.
	 */
	private final WebProperties springWebProperties;

	/**
	 * The path pattern parser.
	 */
	private final PathPatternParser parser = new PathPatternParser();

	/**
	 * Instantiates a new Abstract swagger configurer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui calculated config
	 * @param springWebProperties       the spring web config
	 */
	protected AbstractSwaggerConfigurer(SwaggerUiConfigProperties swaggerUiConfigProperties, WebProperties springWebProperties) {
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
		this.springWebProperties = springWebProperties;
	}

	/**
	 * Gets the handler configs for mapping Swagger UI resources.
	 *
	 * @return the Swagger UI handler configs.
	 */
	protected SwaggerResourceHandlerConfig[] getSwaggerHandlerConfigs() {
		String swaggerUiPattern = getUiRootPath() + SWAGGER_UI_PREFIX + ALL_PATTERN;
		String swaggerUiInitializerPattern = combinePatterns(swaggerUiPattern, SWAGGER_INITIALIZER_PATTERN);
		String swaggerUiResourceLocation = WEBJARS_RESOURCE_LOCATION + SWAGGER_UI_WEBJAR_NAME + DEFAULT_PATH_SEPARATOR +
				swaggerUiConfigProperties.getVersion() + DEFAULT_PATH_SEPARATOR;

		return new SwaggerResourceHandlerConfig[]{
				SwaggerResourceHandlerConfig.createCached()
						.setPatterns(swaggerUiPattern)
						.setLocations(swaggerUiResourceLocation),
				SwaggerResourceHandlerConfig.createUncached()
						.setPatterns(swaggerUiInitializerPattern)
						.setLocations(swaggerUiResourceLocation)
		};
	}

	/**
	 * Gets the handler configs for mapping webjar resources for the Swagger UI.
	 *
	 * @return the Swagger UI webjar handler configs.
	 */
	protected SwaggerResourceHandlerConfig[] getSwaggerWebjarHandlerConfigs() {
		if (!springWebProperties.getResources().isAddMappings()) return new SwaggerResourceHandlerConfig[]{};

		String swaggerUiWebjarPattern = combinePatterns(getWebjarsPathPattern(), SWAGGER_UI_WEBJAR_NAME_PATTERN) + ALL_PATTERN;
		String swaggerUiWebjarInitializerPattern = combinePatterns(swaggerUiWebjarPattern, SWAGGER_INITIALIZER_PATTERN);
		String swaggerUiWebjarVersionInitializerPattern = combinePatterns(swaggerUiWebjarPattern,
				swaggerUiConfigProperties.getVersion() + SWAGGER_INITIALIZER_PATTERN);
		String swaggerUiWebjarResourceLocation = WEBJARS_RESOURCE_LOCATION;

		return new SwaggerResourceHandlerConfig[]{
				SwaggerResourceHandlerConfig.createCached()
						.setPatterns(swaggerUiWebjarPattern)
						.setLocations(swaggerUiWebjarResourceLocation),
				SwaggerResourceHandlerConfig.createUncached()
						.setPatterns(swaggerUiWebjarInitializerPattern, swaggerUiWebjarVersionInitializerPattern)
						.setLocations(swaggerUiWebjarResourceLocation)
		};
	}

	/**
	 * Gets the root path for the Swagger UI.
	 *
	 * @return the Swagger UI root path.
	 */
	protected abstract String getUiRootPath();

	/**
	 * Gets the path pattern for webjar resources.
	 *
	 * @return the webjars path pattern.
	 */
	protected abstract String getWebjarsPathPattern();

	/**
	 * Combines pattern strings into a new pattern according to the rules of {@link PathPattern#combine}.
	 *
	 * <p>For example:
	 * <ul>
	 * <li><code>/webjars/&#42;&#42;</code> + <code>/swagger-ui/&#42;&#42;</code> => <code>/webjars/swagger-ui/&#42;&#42;</code></li>
	 * <li><code>/documentation/swagger-ui&#42;/&#42;&#42;</code> + <code>/&#42;.js</code> => <code>/documentation/swagger-ui&#42;/&#42;.js</code></li>
	 * </ul>
	 *
	 * @param patterns the patterns to combine.
	 *
	 * @return the combination of the patterns strings.
	 *
	 * @throws IllegalArgumentException if the patterns cannot be combined.
	 *
	 * @see PathPattern#combine
	 */
	protected String combinePatterns(String... patterns) {
		return Arrays.stream(patterns)
				.map(this::parsePattern)
				.reduce(PathPattern::combine)
				.map(PathPattern::getPatternString)
				.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Parses a pattern string as a path pattern.
	 *
	 * @param pattern the pattern string.
	 *
	 * @return the parsed path pattern.
	 *
	 * @throws PatternParseException if the pattern string cannot be parsed.
	 */
	private PathPattern parsePattern(String pattern) {
		return parser.parse(parser.initFullPathPattern(pattern));
	}

	/**
	 * The type Swagger resource handler config.
	 *
	 * @param cacheResources whether to cache resources.
	 * @param patterns       the patterns to match.
	 * @param locations      the locations to use.
	 */
	protected record SwaggerResourceHandlerConfig(boolean cacheResources, String[] patterns, String[] locations) {

		private SwaggerResourceHandlerConfig(boolean cacheResources) {
			this(cacheResources, new String[]{}, new String[]{});
		}

		/**
		 * Sets the patterns.
		 *
		 * @param patterns the patterns to match.
		 *
		 * @return the updated config.
		 */
		public SwaggerResourceHandlerConfig setPatterns(String... patterns) {
			return new SwaggerResourceHandlerConfig(cacheResources, patterns, locations);
		}

		/**
		 * Sets the locations.
		 *
		 * @param locations the locations to use.
		 *
		 * @return the updated config.
		 */
		public SwaggerResourceHandlerConfig setLocations(String... locations) {
			return new SwaggerResourceHandlerConfig(cacheResources, patterns, locations);
		}

		/**
		 * Create a Swagger resource handler config with resource caching enabled.
		 */
		public static SwaggerResourceHandlerConfig createCached() {
			return new SwaggerResourceHandlerConfig(true);
		}

		/**
		 * Create a Swagger resource handler config with resource caching disabled.
		 */
		public static SwaggerResourceHandlerConfig createUncached() {
			return new SwaggerResourceHandlerConfig(false);
		}
	}
}
