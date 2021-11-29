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

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.core.Constants.MVC_SERVLET_PATH;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springdoc.webmvc.api.OpenApiWebMvcResource.findPathPrefix;
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
	 * The Path prefix.
	 */
	private String pathPrefix;

	/**
	 * The Open api service.
	 */
	private final OpenAPIService openAPIService;

	/**
	 * Instantiates a new Swagger welcome web mvc.
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param openAPIService the open api service
	 */
	public SwaggerWelcomeWebMvc(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, OpenAPIService openAPIService) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.openAPIService = openAPIService;
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
	 * Calculate ui root path.
	 *
	 * @param sbUrls the sb urls
	 */
	@Override
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		if (StringUtils.isNotBlank(mvcServletPath))
			sbUrl.append(mvcServletPath);
		calculateUiRootCommon(sbUrl, sbUrls);
	}

	/**
	 * Build url string.
	 *
	 * @param contextPath the context path
	 * @param docsUrl the docs url
	 * @return the string
	 */
	@Override
	protected String buildUrl(String contextPath, final String docsUrl) {
		if (StringUtils.isNotBlank(mvcServletPath))
			contextPath += mvcServletPath;
		return super.buildUrl(contextPath, docsUrl);
	}

	/**
	 * Build api doc url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildApiDocUrl() {
		if (this.pathPrefix == null)
			this.pathPrefix = findPathPrefix(this.openAPIService, this.springDocConfigProperties);
		return buildUrl(contextPath + pathPrefix, springDocConfigProperties.getApiDocs().getPath());
	}

	/**
	 * Build swagger config url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildSwaggerConfigUrl() {
		return apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
	}

}