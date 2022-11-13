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

package org.springdoc.webmvc.ui;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.providers.ActuatorProvider;

import org.springframework.format.FormatterRegistry;
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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springdoc.core.utils.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.utils.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PREFIX;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springdoc.core.utils.Constants.SWAGGER_INITIALIZER_JS;
/**
 * The type Swagger web mvc configurer.
 * @author bnasslahsen
 */
public class SwaggerWebMvcConfigurer implements WebMvcConfigurer {

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
	 * Instantiates a new Swagger web mvc configurer.
	 *
	 * @param swaggerUiConfigParameters the swagger ui calculated config
	 * @param swaggerIndexTransformer the swagger index transformer
	 * @param actuatorProvider the actuator provider
	 */
	public SwaggerWebMvcConfigurer(SwaggerUiConfigParameters swaggerUiConfigParameters,
			SwaggerIndexTransformer swaggerIndexTransformer,
			Optional<ActuatorProvider> actuatorProvider) {
		this.swaggerPath = swaggerUiConfigParameters.getPath();
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

		registry.addResourceHandler(uiRootPath + SWAGGER_UI_PREFIX + "*/*" + SWAGGER_INITIALIZER_JS)
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.setCachePeriod(0)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);

		registry.addResourceHandler(uiRootPath + SWAGGER_UI_PREFIX + "*/**")
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);
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
