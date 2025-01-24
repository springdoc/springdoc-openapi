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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.utils.SpringDocUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.core.utils.Constants.MVC_SERVLET_PATH;
import static org.springdoc.core.utils.Constants.SWAGGER_CONFIG_FILE;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger welcome.
 *
 * @author bnasslahsen
 */
@Controller
public class SwaggerWelcomeWebMvc extends SwaggerWelcomeCommon {

	/**
	 * The Spring web provider.
	 */
	private final SpringWebProvider springWebProvider;

	/**
	 * The Mvc servlet path.
	 */
// To keep compatibility with spring-boot 1 - WebMvcProperties changed package from spring 4 to spring 5
	@Value(MVC_SERVLET_PATH)
	private String mvcServletPath;

	/**
	 * Instantiates a new Swagger welcome web mvc.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springWebProvider         the spring web provider
	 */
	public SwaggerWelcomeWebMvc(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SpringWebProvider springWebProvider) {
		super(swaggerUiConfig, springDocConfigProperties);
		this.springWebProvider = springWebProvider;
	}
	
	@Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
	@Override
	public ResponseEntity<Void> redirectToUi(HttpServletRequest request) {
		return super.redirectToUi(request);
	}
	
	@Override
	protected void calculateUiRootPath(SwaggerUiConfigParameters swaggerUiConfigParameters, StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		if (SpringDocUtils.isValidPath(mvcServletPath))
			sbUrl.append(mvcServletPath);
		calculateUiRootCommon(swaggerUiConfigParameters, sbUrl, sbUrls);
	}
	
	@Override
	protected String buildUrl(String contextPath, final String docsUrl) {
		if (SpringDocUtils.isValidPath(mvcServletPath))
			contextPath += mvcServletPath;
		return super.buildUrl(contextPath, docsUrl);
	}
	
	@Override
	protected void buildApiDocUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setApiDocsUrl(buildUrlWithContextPath(swaggerUiConfigParameters, springDocConfigProperties.getApiDocs().getPath()));
	}

	@Override
	protected String buildUrlWithContextPath(SwaggerUiConfigParameters swaggerUiConfigParameters, String swaggerUiUrl) {
		if (swaggerUiConfigParameters.getPathPrefix() == null)
			swaggerUiConfigParameters.setPathPrefix(springWebProvider.findPathPrefix(springDocConfigProperties));
		return buildUrl(swaggerUiConfigParameters.getContextPath() + swaggerUiConfigParameters.getPathPrefix(), swaggerUiUrl);
	}
	
	@Override
	protected void buildSwaggerConfigUrl(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		swaggerUiConfigParameters.setConfigUrl(swaggerUiConfigParameters.getApiDocsUrl() + DEFAULT_PATH_SEPARATOR + SWAGGER_CONFIG_FILE);
	}

}