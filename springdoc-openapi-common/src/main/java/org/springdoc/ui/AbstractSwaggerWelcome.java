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

package org.springdoc.ui;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;


/**
 * The type Abstract swagger welcome.
 * @author bnasslahsen
 */
public abstract class AbstractSwaggerWelcome implements InitializingBean {

	/**
	 * The Swagger ui config.
	 */
	protected final SwaggerUiConfigProperties swaggerUiConfig;

	/**
	 * The Spring doc config properties.
	 */
	protected final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Ui root path.
	 */
	protected String uiRootPath;

	/**
	 * The Oauth 2 redirect url.
	 */
	protected String oauth2RedirectUrl;

	/**
	 * The Origin config url.
	 */
	private String originConfigUrl;

	/**
	 * The Swagger ui url.
	 */
	private String swaggerUiUrl;

	/**
	 * Instantiates a new Abstract swagger welcome.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public AbstractSwaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties) {
		this.swaggerUiConfig = swaggerUiConfig;
		this.springDocConfigProperties = springDocConfigProperties;
		this.oauth2RedirectUrl = swaggerUiConfig.getOauth2RedirectUrl();
		this.originConfigUrl = swaggerUiConfig.getConfigUrl();
		this.swaggerUiUrl = swaggerUiConfig.getUrl();
	}

	@Override
	public void afterPropertiesSet() {
		springDocConfigProperties.getGroupConfigs().forEach(groupConfig -> swaggerUiConfig.addGroup(groupConfig.getGroup()));
		calculateUiRootPath();
	}

	/**
	 * Build url string.
	 *
	 * @param contextPath the context path
	 * @param docsUrl the docs url
	 * @return the string
	 */
	protected String buildUrl(String contextPath, final String docsUrl) {
		if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
			return contextPath.substring(0, contextPath.length() - 1) + docsUrl;
		}
		return contextPath + docsUrl;
	}

	/**
	 * Build config url.
	 *
	 * @param contextPath the context path
	 * @param uriComponentsBuilder the uri components builder
	 */
	protected void buildConfigUrl(String contextPath, UriComponentsBuilder uriComponentsBuilder) {
		String apiDocsUrl = springDocConfigProperties.getApiDocs().getPath();
		if (StringUtils.isEmpty(originConfigUrl)) {
			String url = buildUrl(contextPath, apiDocsUrl);
			String swaggerConfigUrl = url + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
			swaggerUiConfig.setConfigUrl(swaggerConfigUrl);
			if (swaggerUiConfig.getUrls().isEmpty()) {
				if (StringUtils.isEmpty(swaggerUiUrl))
					swaggerUiConfig.setUrl(url);
				else
					swaggerUiConfig.setUrl(swaggerUiUrl);
			}
			else
				swaggerUiConfig.addUrl(url);
		}
		calculateOauth2RedirectUrl(uriComponentsBuilder);
	}

	/**
	 * Gets uri components builder.
	 *
	 * @param sbUrl the sb url
	 * @return the uri components builder
	 */
	protected UriComponentsBuilder getUriComponentsBuilder(String sbUrl) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(sbUrl);
		if (swaggerUiConfig.isDisplayQueryParams() && StringUtils.isNotEmpty(swaggerUiConfig.getUrl())) {
			swaggerUiConfig.getConfigParameters().entrySet().stream()
					.filter(entry -> !SwaggerUiConfigProperties.CONFIG_URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !entry.getKey().startsWith(SwaggerUiConfigProperties.URLS_PROPERTY))
					.filter(entry -> StringUtils.isNotEmpty((String) entry.getValue()))
					.forEach(entry -> uriBuilder.queryParam(entry.getKey(), entry.getValue()));
		} else if (swaggerUiConfig.isDisplayQueryParamsWithoutOauth2() && StringUtils.isNotEmpty(swaggerUiConfig.getUrl())) {
			swaggerUiConfig.getConfigParameters().entrySet().stream()
					.filter(entry -> !SwaggerUiConfigProperties.CONFIG_URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !SwaggerUiConfigProperties.OAUTH2_REDIRECT_URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !entry.getKey().startsWith(SwaggerUiConfigProperties.URLS_PROPERTY))
					.filter(entry -> StringUtils.isNotEmpty((String) entry.getValue()))
					.forEach(entry -> uriBuilder.queryParam(entry.getKey(), entry.getValue()));
		}
		else {
			uriBuilder.queryParam(SwaggerUiConfigProperties.CONFIG_URL_PROPERTY, swaggerUiConfig.getConfigUrl());
			if (StringUtils.isNotEmpty(swaggerUiConfig.getLayout()))
				uriBuilder.queryParam(SwaggerUiConfigProperties.LAYOUT_PROPERTY, swaggerUiConfig.getLayout());
			if (StringUtils.isNotEmpty(swaggerUiConfig.getFilter()))
				uriBuilder.queryParam(SwaggerUiConfigProperties.FILTER_PROPERTY, swaggerUiConfig.getFilter());
		}
		return uriBuilder;
	}

	/**
	 * Calculate oauth 2 redirect url.
	 *
	 * @param uriComponentsBuilder the uri components builder
	 */
	protected abstract void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder);

	/**
	 * Calculate ui root path.
	 *
	 * @param sbUrls the sb urls
	 */
	protected abstract void calculateUiRootPath(StringBuilder... sbUrls);

}