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

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import static org.springdoc.core.utils.Constants.SWAGGER_CONFIG_FILE;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger welcome.
 *
 * @author bnasslahsen
 */
@Controller
public class SwaggerWelcomeWebFlux extends SwaggerWelcomeCommon {

	/**
	 * The Spring web provider.
	 */
	private final SpringWebProvider springWebProvider;

	/**
	 * Instantiates a new Swagger welcome web flux.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springWebProvider         the spring web provider
	 */
	public SwaggerWelcomeWebFlux(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SpringWebProvider springWebProvider) {
		super(swaggerUiConfig, springDocConfigProperties);
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

	@Override
	protected void calculateUiRootPath(SwaggerUiConfigParameters swaggerUiConfigParameters, StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		calculateUiRootCommon(swaggerUiConfigParameters,sbUrl, sbUrls);
	}

	@Override
	protected void buildApiDocUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setApiDocsUrl(buildUrlWithContextPath(swaggerUiConfigParameters, springDocConfigProperties.getApiDocs().getPath()));
	}

	@Override
	protected String buildUrlWithContextPath(SwaggerUiConfigParameters swaggerUiConfigParameters, String swaggerUiUrl) {
		if (swaggerUiConfigParameters.getPathPrefix() == null)
			swaggerUiConfigParameters.setPathPrefix(springWebProvider.findPathPrefix(springDocConfigProperties));
		if (swaggerUiUrl.startsWith(swaggerUiConfigParameters.getPathPrefix())) {
			return buildUrl(swaggerUiConfigParameters.getContextPath(), swaggerUiUrl);
		} else {
			return buildUrl(swaggerUiConfigParameters.getContextPath() + swaggerUiConfigParameters.getPathPrefix(), swaggerUiUrl);
		}
	}

	@Override
	protected void buildSwaggerConfigUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setConfigUrl(swaggerUiConfigParameters.getApiDocsUrl()+ DEFAULT_PATH_SEPARATOR + SWAGGER_CONFIG_FILE);
	}

}
