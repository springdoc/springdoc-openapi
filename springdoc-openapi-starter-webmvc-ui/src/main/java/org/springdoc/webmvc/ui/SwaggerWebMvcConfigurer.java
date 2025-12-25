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

package org.springdoc.webmvc.ui;

import java.util.List;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerConfigurer;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.CachingResourceResolver;

import static org.springdoc.core.utils.Constants.SWAGGER_RESOURCE_CACHE_NAME;

/**
 * The type Swagger web mvc configurer.
 *
 * @author bnasslahsen
 */
public class SwaggerWebMvcConfigurer extends AbstractSwaggerConfigurer implements WebMvcConfigurer {

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
	 * The Spring MVC config properties.
	 */
	private final WebMvcProperties springWebMvcProperties;

	/**
	 * The Swagger welcome common.
	 */
	private final SwaggerWelcomeCommon swaggerWelcomeCommon;

	/**
	 * The Swagger resource chain cache.
	 */
	private Cache cache;

	/**
	 * Instantiates a new Swagger web mvc configurer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui calculated config
	 * @param springWebProperties       the spring web config
	 * @param springWebMvcProperties    the spring mvc config
	 * @param swaggerIndexTransformer   the swagger index transformer
	 * @param swaggerResourceResolver   the swagger resource resolver
	 * @param swaggerWelcomeCommon      the swagger welcome common
	 */
	public SwaggerWebMvcConfigurer(SwaggerUiConfigProperties swaggerUiConfigProperties,
			WebProperties springWebProperties, WebMvcProperties springWebMvcProperties,
			SwaggerIndexTransformer swaggerIndexTransformer, SwaggerResourceResolver swaggerResourceResolver,
			SwaggerWelcomeCommon swaggerWelcomeCommon) {
		super(swaggerUiConfigProperties, springWebProperties);
		this.swaggerIndexTransformer = swaggerIndexTransformer;
		this.swaggerResourceResolver = swaggerResourceResolver;
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
		this.springWebMvcProperties = springWebMvcProperties;
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
		return springWebMvcProperties.getWebjarsPathPattern();
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

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}


	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		// This implementation is empty to keep compatibility with spring 4 applications.
	}

	@Override
	@Nullable
	public Validator getValidator() {
		// This implementation is empty to keep compatibility with spring 4 applications.
		return null;
	}

	@Override
	@Nullable
	public MessageCodesResolver getMessageCodesResolver() {
		// This implementation is empty to keep compatibility with spring 4 applications.
		return null;
	}
}
