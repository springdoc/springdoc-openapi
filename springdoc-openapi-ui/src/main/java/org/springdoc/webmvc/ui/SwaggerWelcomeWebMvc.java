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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.webmvc.api.OpenApiResource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.MVC_SERVLET_PATH;
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
	 * The Path prefix.
	 */
	private String pathPrefix;

	/**
	 * The Request mapping handler mapping.
	 */
	private final Optional<RequestMappingInfoHandlerMapping>  requestMappingInfoHandlerMappingOptional;

	/**
	 * Instantiates a new Swagger welcome.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param requestMappingInfoHandlerMappingOptional the request mapping info handler mapping optional
	 */
	public SwaggerWelcomeWebMvc(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,SwaggerUiConfigParameters swaggerUiConfigParameters, Optional<RequestMappingInfoHandlerMapping> requestMappingInfoHandlerMappingOptional) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.requestMappingInfoHandlerMappingOptional = requestMappingInfoHandlerMappingOptional;
	}

	/**
	 * Init.
	 */
	@PostConstruct
	private void init() {
		requestMappingInfoHandlerMappingOptional.ifPresent(requestMappingHandlerMapping -> {
			Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
			List<Entry<RequestMappingInfo, HandlerMethod>> entries = new ArrayList<>(map.entrySet());
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
				RequestMappingInfo requestMappingInfo = entry.getKey();
				Set<String> patterns = OpenApiResource.getActivePatterns(requestMappingInfo);
				if (!CollectionUtils.isEmpty(patterns)) {
					for (String operationPath : patterns) {
						if (operationPath.endsWith(springDocConfigProperties.getApiDocs().getPath()))
							pathPrefix =  operationPath.replace(springDocConfigProperties.getApiDocs().getPath(), StringUtils.EMPTY);
					}
				}
			}
		});
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
		return buildUrl(contextPath + pathPrefix, springDocConfigProperties.getApiDocs().getPath());
	}

	/**
	 * Build swagger config url string.
	 *
	 * @return the string
	 */
	@Override
	protected String buildSwaggerConfigUrl() {
		return  apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
	}
}