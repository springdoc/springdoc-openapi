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

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerWelcome;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

/**
 * The type Swagger welcome common.
 *
 * @author bnasslashen
 */
public abstract class SwaggerWelcomeCommon extends AbstractSwaggerWelcome {


	/**
	 * Instantiates a new Abstract swagger welcome.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 */
	protected SwaggerWelcomeCommon(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties) {
		super(swaggerUiConfig, springDocConfigProperties);
	}

	/**
	 * Redirect to ui mono.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the mono
	 */
	protected Mono<Void> redirectToUi(ServerHttpRequest request, ServerHttpResponse response) {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfig);
		buildFromCurrentContextPath(swaggerUiConfigParameters, request);
		String sbUrl = swaggerUiConfigParameters.getContextPath() + swaggerUiConfigParameters.getUiRootPath() + getSwaggerUiUrl();
		UriComponentsBuilder uriBuilder = getUriComponentsBuilder(swaggerUiConfigParameters, sbUrl);

		// forward all queryParams from original request
		request.getQueryParams().forEach(uriBuilder::queryParam);

		response.setStatusCode(HttpStatus.FOUND);
		response.getHeaders().setLocation(URI.create(uriBuilder.build().encode().toString()));
		return response.setComplete();
	}

	/**
	 * Openapi json map.
	 *
	 * @param request the request
	 * @return the map
	 */
	protected Map<String, Object> openapiJson(ServerHttpRequest request) {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfig);
		buildFromCurrentContextPath(swaggerUiConfigParameters, request);
		return swaggerUiConfigParameters.getConfigParameters();
	}

	@Override
	protected void calculateOauth2RedirectUrl(SwaggerUiConfigParameters swaggerUiConfigParameters, UriComponentsBuilder uriComponentsBuilder) {
		if (StringUtils.isBlank(swaggerUiConfig.getOauth2RedirectUrl()) || !swaggerUiConfigParameters.isValidUrl(swaggerUiConfig.getOauth2RedirectUrl())) {
			swaggerUiConfigParameters.setOauth2RedirectUrl(uriComponentsBuilder
					.path(swaggerUiConfigParameters.getUiRootPath())
					.path(getOauth2RedirectUrl()).build().toString());
		}
	}

	/**
	 * Gets swagger ui config.
	 *
	 * @param request the request
	 * @return the swagger ui config
	 */
	protected Map<String, Object> getSwaggerUiConfig(ServerHttpRequest request) {
		SwaggerUiConfigParameters swaggerUiConfigParameters = new SwaggerUiConfigParameters(swaggerUiConfig);
		this.buildFromCurrentContextPath(swaggerUiConfigParameters, request);
		return swaggerUiConfigParameters.getConfigParameters();
	}

	/**
	 * From current context path string.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param request                   the request
	 * @return the string
	 */
	void buildFromCurrentContextPath(SwaggerUiConfigParameters swaggerUiConfigParameters, ServerHttpRequest request) {
		super.init(swaggerUiConfigParameters);
		swaggerUiConfigParameters.setContextPath(request.getPath().contextPath().value());
		String url = UriComponentsBuilder.fromHttpRequest(request).toUriString();
		String target = UriComponentsBuilder.fromPath(request.getPath().contextPath().value()).toUriString();
		int endIndex = url.indexOf(target) + target.length();
		if (endIndex > 0) {
			url = url.substring(0, endIndex);
		} else {
			url = url.replace(request.getPath().toString(), "");
		}
		buildConfigUrl(swaggerUiConfigParameters, UriComponentsBuilder.fromUriString(url));
	}

}
