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

import java.net.URI;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * Home redirection to swagger api documentation
 *
 * @author bnasslahsen
 */
@Controller
public class SwaggerUiHome {

	/**
	 * The Optional web flux properties.
	 */
	private final Optional<WebFluxProperties> optionalWebFluxProperties;

	/**
	 * The Swagger ui path.
	 */
	@Value(SWAGGER_UI_PATH)
	private String swaggerUiPath;

	/**
	 * Instantiates a new Swagger ui home.
	 *
	 * @param optionalWebFluxProperties the optional web flux properties
	 */
	public SwaggerUiHome(Optional<WebFluxProperties> optionalWebFluxProperties) {
		this.optionalWebFluxProperties = optionalWebFluxProperties;
	}

	/**
	 * Index mono.
	 *
	 * @param response the response
	 * @return the mono
	 */
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	@Operation(hidden = true)
	public Mono<Void> index(ServerHttpResponse response) {
		String basePath = optionalWebFluxProperties
				.map(props -> StringUtils.defaultIfEmpty(props.getBasePath(), StringUtils.EMPTY))
				.orElse(StringUtils.EMPTY);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(basePath + swaggerUiPath);
		response.setStatusCode(HttpStatus.FOUND);
		response.getHeaders().setLocation(URI.create(uriBuilder.build().encode().toString()));
		return response.setComplete();
	}
}