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

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import reactor.core.publisher.Mono;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springdoc.webflux.api.OpenApiWebfluxResource.findPathPrefix;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
/**
 * The type Swagger welcome.
 * @author bnasslahsen
 */
@Controller
public class SwaggerWelcomeWebFlux extends SwaggerWelcomeCommon {

	/**
	 * The Open api service.
	 */
	private final OpenAPIService openAPIService;

	/**
	 * The Path prefix.
	 */
	private String pathPrefix;

	/**
	 * Instantiates a new Swagger welcome web flux.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param openAPIService the open api service
	 */
	public SwaggerWelcomeWebFlux(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,
			SwaggerUiConfigParameters swaggerUiConfigParameters, OpenAPIService openAPIService) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.openAPIService = openAPIService;
	}


	/**
	 * Redirect to ui mono.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the mono
	 */
	@Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
	@Override
	public Mono<Void> redirectToUi(ServerHttpRequest request, ServerHttpResponse response) {
		return super.redirectToUi(request, response);
	}

	/**
	 * Calculate ui root path.
	 *
	 * @param sbUrls the sb urls
	 */
	@Override
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		calculateUiRootCommon(sbUrl, sbUrls);
	}

	/**
	 * Calculate oauth 2 redirect url.
	 *
	 * @param uriComponentsBuilder the uri components builder
	 */
	@Override
	protected void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder) {
		if ((oauthPrefix == null && !swaggerUiConfigParameters.isValidUrl(swaggerUiConfigParameters.getOauth2RedirectUrl())) || springDocConfigProperties.isCacheDisabled()) {
			this.oauthPrefix = uriComponentsBuilder.path(contextPath).path(swaggerUiConfigParameters.getUiRootPath()).path(webJarsPrefixUrl);
			swaggerUiConfigParameters.setOauth2RedirectUrl(this.oauthPrefix.path(getOauth2RedirectUrl()).build().toString());
		}
	}

	/**
	 * Build api doc url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildApiDocUrl() {
		if (this.pathPrefix == null)
			this.pathPrefix = findPathPrefix(openAPIService, springDocConfigProperties);
		return buildUrl(this.contextPath + this.pathPrefix, springDocConfigProperties.getApiDocs().getPath());
	}

	/**
	 * Build swagger config url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildSwaggerConfigUrl() {
		return this.apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
	}

}
