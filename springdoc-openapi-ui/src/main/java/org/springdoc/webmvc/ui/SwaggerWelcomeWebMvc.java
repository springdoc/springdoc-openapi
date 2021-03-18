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

package org.springdoc.webmvc.ui;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springdoc.core.Constants.MVC_SERVLET_PATH;
import static org.springdoc.core.Constants.SWAGGER_CONFIG_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger welcome.
 * @author bnasslahsen
 */
@Controller
public class SwaggerWelcomeWebMvc extends SwaggerWelcomeCommon {

	/**
	 * The Mvc servlet path.
	 */
// To keep compatiblity with spring-boot 1 - WebMvcProperties changed package from srping 4 to spring 5
	@Value(MVC_SERVLET_PATH)
	private String mvcServletPath;

	/**
	 * Instantiates a new Swagger welcome.
	 *
	 * @param swaggerUiConfig the swagger ui config 
	 * @param springDocConfigProperties the spring doc config properties 
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public SwaggerWelcomeWebMvc(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,SwaggerUiConfigParameters swaggerUiConfigParameters) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
	}

	/**
	 * Redirect to ui string.
	 *
	 * @param request the request 
	 * @return the string
	 */
	@Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
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
	@GetMapping(value = SWAGGER_CONFIG_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Override
	public Map<String, Object> openapiJson(HttpServletRequest request) {
		return super.openapiJson(request);
	}

	@Override
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		if (StringUtils.isNotBlank(mvcServletPath))
			sbUrl.append(mvcServletPath);
		calculateUiRootCommon(sbUrl, sbUrls);
	}

	@Override
	protected String buildUrl(String contextPath, final String docsUrl) {
		if (StringUtils.isNotBlank(mvcServletPath))
			contextPath += mvcServletPath;
		return super.buildUrl(contextPath, docsUrl);
	}

	@Override
	protected String buildApiDocUrl() {
		return buildUrl(contextPath, springDocConfigProperties.getApiDocs().getPath());
	}

	@Override
	protected String buildSwaggerConfigUrl() {
		return  apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
	}
}