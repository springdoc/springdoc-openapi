/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

import java.nio.charset.StandardCharsets;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.ui.AbstractSwaggerIndexTransformer;
import org.springdoc.ui.SpringDocUIException;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.resource.ResourceTransformerChain;
import org.springframework.web.reactive.resource.TransformedResource;
import org.springframework.web.server.ServerWebExchange;

import static org.springdoc.core.utils.Constants.SWAGGER_INITIALIZER_JS;

/**
 * The type Swagger index transformer.
 * @author bnasslahsen
 */
public class SwaggerIndexPageTransformer extends AbstractSwaggerIndexTransformer implements SwaggerIndexTransformer {

	/**
	 * The Swagger welcome common.
	 */
	private final SwaggerWelcomeCommon swaggerWelcomeCommon;

	/**
	 * Instantiates a new Swagger index transformer.
	 * @param swaggerUiConfig the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param swaggerWelcomeCommon the swagger welcome common
	 * @param objectMapperProvider the object mapper provider
	 */
	public SwaggerIndexPageTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, 
			SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
		super(swaggerUiConfig, swaggerUiOAuthProperties, objectMapperProvider);
		this.swaggerWelcomeCommon = swaggerWelcomeCommon;
	}

	@Override
	public Mono<Resource> transform(ServerWebExchange serverWebExchange, Resource resource, ResourceTransformerChain resourceTransformerChain) {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfig);
		swaggerWelcomeCommon.buildFromCurrentContextPath(swaggerUiConfigParameters, serverWebExchange.getRequest());
		
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		try {
			boolean isIndexFound = antPathMatcher.match("**/swagger-ui/**/" + SWAGGER_INITIALIZER_JS, resource.getURL().toString());
			if (isIndexFound) {
				String html = defaultTransformations(swaggerUiConfigParameters, resource.getInputStream());
				return Mono.just(new TransformedResource(resource, html.getBytes(StandardCharsets.UTF_8)));
			}
			else {
				return Mono.just(resource);
			}
		}
		catch (Exception e) {
			throw new SpringDocUIException("Failed to transform Index", e);
		}
	}

}