/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

import org.springdoc.ui.AbstractSwaggerConfigurer;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webflux.autoconfigure.WebFluxProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.ResourceChainRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.CachingResourceResolver;

import static org.springdoc.core.utils.Constants.SWAGGER_RESOURCE_CACHE_NAME;

/**
 * The type Swagger web flux configurer.
 *
 * @author bnasslahsen
 */
public class SwaggerWebFluxConfigurer extends AbstractSwaggerConfigurer implements WebFluxConfigurer {

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
	 * The Spring WebFlux config properties.
	 */
	private final WebFluxProperties springWebFluxProperties;

	/**
	 * The Swagger welcome common.
	 */
	private final SwaggerWelcomeCommon swaggerWelcomeCommon;

	/**
	 * The Swagger resource chain cache.
	 */
	private Cache cache;

	/**
	 * Instantiates a new Swagger web flux configurer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui calculated config
	 * @param springWebProperties       the spring web config
	 * @param springWebFluxProperties   the spring webflux config
	 * @param swaggerIndexTransformer   the swagger index transformer
	 * @param swaggerResourceResolver   the swagger resource resolver
	 * @param swaggerWelcomeCommon      the swagger welcome common
	 */
	public SwaggerWebFluxConfigurer(SwaggerUiConfigProperties swaggerUiConfigProperties,
			WebProperties springWebProperties, WebFluxProperties springWebFluxProperties,
			SwaggerIndexTransformer swaggerIndexTransformer, SwaggerResourceResolver swaggerResourceResolver,
			SwaggerWelcomeCommon swaggerWelcomeCommon) {
		super(swaggerUiConfigProperties, springWebProperties);
		this.swaggerIndexTransformer = swaggerIndexTransformer;
		this.swaggerResourceResolver = swaggerResourceResolver;
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
		this.springWebFluxProperties = springWebFluxProperties;
		this.swaggerWelcomeCommon = swaggerWelcomeCommon;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		addSwaggerResourceHandlers(registry, getSwaggerHandlerConfigs());
		addSwaggerResourceHandlers(registry, getSwaggerWebjarHandlerConfigs());
	}

	/**
	 * Add resource handlers that use the Swagger resource resolver and transformer.
	 *
	 * @param registry the resource handler registry.
	 * @param handlerConfigs the swagger handler configs.
	 */
	protected void addSwaggerResourceHandlers(ResourceHandlerRegistry registry, SwaggerResourceHandlerConfig... handlerConfigs) {
		for (SwaggerResourceHandlerConfig handlerConfig : handlerConfigs) {
			addSwaggerResourceHandler(registry, handlerConfig);
		}
	}

	/**
	 * Add a resource handler that uses the Swagger resource resolver and transformer.
	 *
	 * @param registry the resource handler registry.
	 * @param handlerConfig the swagger handler config.
	 */
	protected void addSwaggerResourceHandler(ResourceHandlerRegistry registry, SwaggerResourceHandlerConfig handlerConfig) {
		ResourceHandlerRegistration handlerRegistration = registry.addResourceHandler(handlerConfig.patterns());
		handlerRegistration.addResourceLocations(handlerConfig.locations());

		ResourceChainRegistration chainRegistration;
		if (handlerConfig.cacheResources()) {
			chainRegistration = handlerRegistration.resourceChain(true, getCache());
		} else {
			handlerRegistration.setUseLastModified(false);
			handlerRegistration.setCacheControl(CacheControl.noStore());

			chainRegistration = handlerRegistration.resourceChain(false);
			chainRegistration.addResolver(new CachingResourceResolver(getCache())); // only use cache for resolving
		}

		chainRegistration.addResolver(swaggerResourceResolver);
		chainRegistration.addTransformer(swaggerIndexTransformer);
	}

	@Override
	protected String getUiRootPath() {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfigProperties);
		swaggerWelcomeCommon.calculateUiRootPath(swaggerUiConfigParameters);

		return swaggerUiConfigParameters.getUiRootPath();
	}

	@Override
	protected String getWebjarsPathPattern() {
		return springWebFluxProperties.getWebjarsPathPattern();
	}

	/**
	 * Gets the Swagger resource chain cache.
	 *
	 * @return the cache.
	 */
	protected Cache getCache() {
		if (cache == null) cache = new ConcurrentMapCache(SWAGGER_RESOURCE_CACHE_NAME);

		return cache;
	}
}
