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

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.core.utils.Constants.DEFAULT_SWAGGER_UI_ACTUATOR_PATH;
import static org.springdoc.core.utils.Constants.SWAGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger actuator welcome.
 */
@ControllerEndpoint(id = DEFAULT_SWAGGER_UI_ACTUATOR_PATH)
public class SwaggerWelcomeActuator extends SwaggerWelcomeCommon {

	/**
	 * The constant SWAGGER_CONFIG_ACTUATOR_URL.
	 */
	private static final String SWAGGER_CONFIG_ACTUATOR_URL = DEFAULT_PATH_SEPARATOR + SWAGGER_CONFIG_FILE;

	/**
	 * The Web endpoint properties.
	 */
	private final WebEndpointProperties webEndpointProperties;

	/**
	 * Instantiates a new Swagger welcome.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the swagger ui config parameters
	 * @param webEndpointProperties     the web endpoint properties
	 */
	public SwaggerWelcomeActuator(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,WebEndpointProperties webEndpointProperties) {
		super(swaggerUiConfig, springDocConfigProperties);
		this.webEndpointProperties = webEndpointProperties;
	}

	/**
	 * Redirect to ui string.
	 *
	 * @param request the request
	 * @return the string
	 */
	@Operation(hidden = true)
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	@Override
	public ResponseEntity<Void> redirectToUi(HttpServletRequest request) {
		return super.redirectToUi(request);
	}

	/**
	 * Openapi yaml map.
	 *
	 * @param request the request
	 * @return the map
	 */
	@Operation(hidden = true)
	@GetMapping(value = SWAGGER_CONFIG_ACTUATOR_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Override
	public Map<String, Object> openapiJson(HttpServletRequest request) {
		return super.openapiJson(request);
	}

	@Override
	protected void calculateUiRootPath(SwaggerUiConfigParameters swaggerUiConfigParameters, StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(webEndpointProperties.getBasePath());
		calculateUiRootCommon(swaggerUiConfigParameters, sbUrl, sbUrls);
	}

	@Override
	protected void buildApiDocUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setApiDocsUrl(buildUrl(swaggerUiConfigParameters.getContextPath() + webEndpointProperties.getBasePath(), DEFAULT_API_DOCS_ACTUATOR_URL));
	}

	@Override
	protected String buildUrlWithContextPath(SwaggerUiConfigParameters swaggerUiConfigParameters, String swaggerUiUrl) {
		return buildUrl(swaggerUiConfigParameters.getContextPath() + webEndpointProperties.getBasePath(), swaggerUiUrl);
	}

	@Override
	protected void buildSwaggerConfigUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setConfigUrl(swaggerUiConfigParameters.getContextPath() + webEndpointProperties.getBasePath()
				+ DEFAULT_PATH_SEPARATOR + DEFAULT_SWAGGER_UI_ACTUATOR_PATH
				+ DEFAULT_PATH_SEPARATOR + SWAGGER_CONFIG_FILE);
	}

}
