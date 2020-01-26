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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.MVC_SERVLET_PATH;
import static org.springdoc.core.Constants.SPRINGDOC_OAUTH2_REDIRECT_URL_VALUE;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_CONFIG_URL_VALUE;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_URL_VALUE;
import static org.springdoc.core.Constants.SWAGGER_CONFIG_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.SWAGGER_UI_URL;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
class SwaggerWelcome implements InitializingBean {

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@Value(SWAGGER_UI_PATH)
	private String swaggerPath;

	@Value(MVC_SERVLET_PATH)
	private String mvcServletPath;

	@Value(SPRINGDOC_SWAGGER_UI_CONFIG_URL_VALUE)
	private String originConfigUrl;

	@Value(SPRINGDOC_SWAGGER_UI_URL_VALUE)
	private String swaggerUiUrl;

	@Value(SPRINGDOC_OAUTH2_REDIRECT_URL_VALUE)
	private String oauth2RedirectUrl;

	@Autowired
	private SwaggerUiConfigProperties swaggerUiConfig;

	private String uiRootPath;


	@Override
	public void afterPropertiesSet() {
		calculateUiRootPath();
	}

	@Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
	public String redirectToUi(HttpServletRequest request) {
		buildConfigUrl(request);
		String sbUrl = REDIRECT_URL_PREFIX + this.uiRootPath
				+ SWAGGER_UI_URL;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(sbUrl);
		return uriBuilder.queryParam(SwaggerUiConfigProperties.CONFIG_URL_PROPERTY, swaggerUiConfig.getConfigUrl()).build().encode().toString();
	}

	@Operation(hidden = true)
	@GetMapping(value = SWAGGER_CONFIG_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> openapiYaml(HttpServletRequest request) {
		buildConfigUrl(request);
		return swaggerUiConfig.getConfigParameters();
	}

	private String buildUrl(final HttpServletRequest request, final String docsUrl) {
		String contextPath = request.getContextPath();
		if (StringUtils.isNotBlank(mvcServletPath))
			contextPath += mvcServletPath;
		if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
			return contextPath.substring(0, contextPath.length() - 1) + docsUrl;
		}
		return contextPath + docsUrl;
	}

	private void buildConfigUrl(HttpServletRequest request) {
		if (StringUtils.isEmpty(originConfigUrl)) {
			String url = buildUrl(request, apiDocsUrl);
			String swaggerConfigUrl = url + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
			swaggerUiConfig.setConfigUrl(swaggerConfigUrl);
			if (SwaggerUiConfigProperties.getSwaggerUrls().isEmpty()) {
				if (StringUtils.isEmpty(swaggerUiUrl))
					swaggerUiConfig.setUrl(url);
				else
					swaggerUiConfig.setUrl(swaggerUiUrl);
			}
			else
				SwaggerUiConfigProperties.addUrl(url);
		}
		if (StringUtils.isEmpty(oauth2RedirectUrl)) {
			swaggerUiConfig.setOauth2RedirectUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path(this.uiRootPath).path(SWAGGER_UI_OAUTH_REDIRECT_URL).build().toString());
		}
		else if (!swaggerUiConfig.isValidUrl(swaggerUiConfig.getOauth2RedirectUrl())) {
			swaggerUiConfig.setOauth2RedirectUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path(this.uiRootPath).path(swaggerUiConfig.getOauth2RedirectUrl()).build().toString());
		}
	}

	private void calculateUiRootPath() {
		StringBuilder sbUrl = new StringBuilder();
		if (StringUtils.isNotBlank(mvcServletPath))
			sbUrl.append(mvcServletPath);
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			sbUrl.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		this.uiRootPath = sbUrl.toString();
	}
}