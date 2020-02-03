/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.ui;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_CONFIG_URL_VALUE;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_URL_VALUE;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.SWAGGER_UI_URL;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springdoc.core.Constants.WEB_JARS_PREFIX_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Controller
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerWelcome {

	@Autowired
	public SwaggerUiConfigProperties swaggerUiConfig;

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@Value(SWAGGER_UI_PATH)
	private String uiPath;

	@Value(WEB_JARS_PREFIX_URL)
	private String webJarsPrefixUrl;

	@Value(SPRINGDOC_SWAGGER_UI_URL_VALUE)
	private String swaggerUiUrl;

	@Value(SPRINGDOC_SWAGGER_UI_CONFIG_URL_VALUE)
	private String originConfigUrl;

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
	RouterFunction<ServerResponse> routerFunction() {
		buildConfigUrl();
		String baseUrl = webJarsPrefixUrl + SWAGGER_UI_URL;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
		uriBuilder.queryParam(SwaggerUiConfigProperties.CONFIG_URL_PROPERTY, swaggerUiConfig.getConfigUrl());
		return route(GET(uiPath),
				req -> ServerResponse.temporaryRedirect(URI.create(uriBuilder.build().encode().toString())).build());
	}

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
	RouterFunction<ServerResponse> getSwaggerUiConfig() {
		buildConfigUrl();
		return RouterFunctions.route(GET(swaggerUiConfig.getConfigUrl()).and(accept(MediaType.APPLICATION_JSON)), req -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(swaggerUiConfig.getConfigParameters()));
	}

	private void buildConfigUrl() {
		if (StringUtils.isEmpty(originConfigUrl)) {
			String swaggerConfigUrl = apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
			swaggerUiConfig.setConfigUrl(swaggerConfigUrl);
			if (SwaggerUiConfigProperties.getSwaggerUrls().isEmpty()) {
				if (StringUtils.isEmpty(swaggerUiUrl))
					swaggerUiConfig.setUrl(apiDocsUrl);
				else
					swaggerUiConfig.setUrl(swaggerUiUrl);
			}
			else
				SwaggerUiConfigProperties.addUrl(apiDocsUrl);
		}
	}

}
