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

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import reactor.core.publisher.Mono;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.utils.Constants.SWAGGER_CONFIG_FILE;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger welcome.
 * @author bnasslahsen
 */
@Controller
public class SwaggerWelcomeWebFlux extends SwaggerWelcomeCommon {


	/**
	 * The Spring web provider.
	 */
	private final SpringWebProvider springWebProvider;

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
	 * @param springWebProvider the spring web provider
	 */
	public SwaggerWelcomeWebFlux(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,
			SwaggerUiConfigParameters swaggerUiConfigParameters, SpringWebProvider springWebProvider) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.springWebProvider = springWebProvider;
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
		if (StringUtils.isBlank(swaggerUiConfig.getOauth2RedirectUrl()) || !swaggerUiConfigParameters.isValidUrl(swaggerUiConfig.getOauth2RedirectUrl())) {
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
		return buildUrlWithContextPath(springDocConfigProperties.getApiDocs().getPath());
	}

	@Override
	protected String buildUrlWithContextPath(String swaggerUiUrl) {
		if (this.pathPrefix == null)
			this.pathPrefix = springWebProvider.findPathPrefix(springDocConfigProperties);
		return buildUrl(this.contextPath + this.pathPrefix, swaggerUiUrl);
	}

	/**
	 * Build swagger config url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildSwaggerConfigUrl() {
		return this.apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGER_CONFIG_FILE;
	}

}
