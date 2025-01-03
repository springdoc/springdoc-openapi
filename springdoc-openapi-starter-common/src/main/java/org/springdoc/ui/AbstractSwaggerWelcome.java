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

package org.springdoc.ui;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.utils.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;


/**
 * The type Abstract swagger welcome.
 *
 * @author bnasslashen
 */
public abstract class AbstractSwaggerWelcome {

	/**
	 * The Swagger ui configuration.
	 */
	protected final SwaggerUiConfigProperties swaggerUiConfig;

	/**
	 * The Spring doc config properties.
	 */
	protected final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Abstract swagger welcome.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 */
	protected AbstractSwaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties) {
		this.swaggerUiConfig = swaggerUiConfig;
		this.springDocConfigProperties = springDocConfigProperties;
	}

	/**
	 * Init.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	protected void init(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		springDocConfigProperties.getGroupConfigs().forEach(groupConfig -> swaggerUiConfigParameters.addGroup(groupConfig.getGroup(), groupConfig.getDisplayName()));
		calculateUiRootPath(swaggerUiConfigParameters);
	}

	/**
	 * Build url string.
	 *
	 * @param contextPath the context path
	 * @param docsUrl     the docs url
	 * @return the string
	 */
	protected String buildUrl(String contextPath, String docsUrl) {
		if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
			return contextPath.substring(0, contextPath.length() - 1) + docsUrl;
		}
		if (!docsUrl.startsWith(DEFAULT_PATH_SEPARATOR))
			docsUrl = DEFAULT_PATH_SEPARATOR + docsUrl;
		return contextPath + docsUrl;
	}

	/**
	 * Build config url.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param uriComponentsBuilder      the uri components builder
	 */
	protected void buildConfigUrl(SwaggerUiConfigParameters swaggerUiConfigParameters, UriComponentsBuilder uriComponentsBuilder) {
		if (StringUtils.isEmpty(swaggerUiConfig.getConfigUrl())) {
			buildApiDocUrl(swaggerUiConfigParameters);
			buildSwaggerConfigUrl(swaggerUiConfigParameters);
			if (CollectionUtils.isEmpty(swaggerUiConfigParameters.getUrls())) {
				String swaggerUiUrl = swaggerUiConfig.getUrl();
				if (StringUtils.isEmpty(swaggerUiUrl))
					swaggerUiConfigParameters.setUrl(swaggerUiConfigParameters.getApiDocsUrl());
				else if (swaggerUiConfigParameters.isValidUrl(swaggerUiUrl))
					swaggerUiConfigParameters.setUrl(swaggerUiUrl);
				else
					swaggerUiConfigParameters.setUrl(buildUrlWithContextPath(swaggerUiConfigParameters, swaggerUiUrl));
			}
			else
				swaggerUiConfigParameters.addUrl(swaggerUiConfigParameters.getApiDocsUrl());
			if (!CollectionUtils.isEmpty(swaggerUiConfig.getUrls())) {
				swaggerUiConfig.cloneUrls()
						.stream()
						.filter(swaggerUrl -> !swaggerUiConfigParameters.isValidUrl(swaggerUrl.getUrl()))
						.forEach(swaggerUrl -> {
							final var url = buildUrlWithContextPath(swaggerUiConfigParameters, swaggerUrl.getUrl());
							if (!Objects.equals(url, swaggerUrl.getUrl())) {
								swaggerUiConfigParameters.getUrls()
										.stream()
										.filter(swaggerUrl::equals)
										.forEach(subUrl -> subUrl.setUrl(url));
							}
						});
			}
		}
		calculateOauth2RedirectUrl(swaggerUiConfigParameters, uriComponentsBuilder);
	}

	/**
	 * Build swagger ui url string.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param swaggerUiUrl              the swagger ui url
	 * @return the string
	 */
	protected abstract String buildUrlWithContextPath(SwaggerUiConfigParameters swaggerUiConfigParameters, String swaggerUiUrl);

	/**
	 * Gets uri components builder.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param sbUrl                     the sb url
	 * @return the uri components builder
	 */
	protected UriComponentsBuilder getUriComponentsBuilder(SwaggerUiConfigParameters swaggerUiConfigParameters, String sbUrl) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(sbUrl);
		if ((swaggerUiConfig.getQueryConfigEnabled() != null && swaggerUiConfig.getQueryConfigEnabled())) {
			swaggerUiConfigParameters.getConfigParameters().entrySet().stream()
					.filter(entry -> !SwaggerUiConfigParameters.CONFIG_URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !SwaggerUiConfigParameters.OAUTH2_REDIRECT_URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !SwaggerUiConfigParameters.URL_PROPERTY.equals(entry.getKey()))
					.filter(entry -> !entry.getKey().startsWith(SwaggerUiConfigParameters.URLS_PROPERTY))
					.filter(entry -> (entry.getValue() instanceof String) ? StringUtils.isNotEmpty((String) entry.getValue()) : entry.getValue() != null)
					.forEach(entry -> uriBuilder.queryParam(entry.getKey(), entry.getValue()));
			uriBuilder.queryParam(SwaggerUiConfigParameters.CONFIG_URL_PROPERTY, swaggerUiConfigParameters.getConfigUrl());
		}
		return uriBuilder;
	}

	/**
	 * Calculate oauth 2 redirect url.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param uriComponentsBuilder      the uri components builder
	 */
	protected abstract void calculateOauth2RedirectUrl(SwaggerUiConfigParameters swaggerUiConfigParameters, UriComponentsBuilder uriComponentsBuilder);

	/**
	 * Calculate ui root path.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param sbUrls                    the sb urls
	 */
	protected abstract void calculateUiRootPath(SwaggerUiConfigParameters swaggerUiConfigParameters,StringBuilder... sbUrls);

	/**
	 * Calculate ui root common.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param sbUrl                     the sb url
	 * @param sbUrls                    the sb urls
	 */
	protected void calculateUiRootCommon(SwaggerUiConfigParameters swaggerUiConfigParameters, StringBuilder sbUrl, StringBuilder[] sbUrls) {
		if (ArrayUtils.isNotEmpty(sbUrls))
			sbUrl = sbUrls[0];
		String swaggerPath = swaggerUiConfigParameters.getPath();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			sbUrl.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		swaggerUiConfigParameters.setUiRootPath(sbUrl.toString());
	}

	/**
	 * Build api doc url string.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	protected abstract void buildApiDocUrl(SwaggerUiConfigParameters swaggerUiConfigParameters);

	/**
	 * Build swagger config url string.
	 *
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	protected abstract void buildSwaggerConfigUrl(SwaggerUiConfigParameters swaggerUiConfigParameters);

	/**
	 * Gets oauth2 redirect url.
	 *
	 * @return the oauth2 redirect url
	 */
	protected String getOauth2RedirectUrl() {
		return StringUtils.defaultIfBlank(swaggerUiConfig.getOauth2RedirectUrl(), SWAGGER_UI_OAUTH_REDIRECT_URL);
	}

	/**
	 * Gets swagger ui url.
	 *
	 * @return the swagger ui url
	 */
	protected String getSwaggerUiUrl() {
		return SWAGGER_UI_URL;
	}
}