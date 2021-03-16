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

package org.springdoc.core;

import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger ui config parameters.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiConfigParameters extends AbstractSwaggerUiConfigProperties {

	/**
	 * The constant CONFIG_URL_PROPERTY.
	 */
	public static final String CONFIG_URL_PROPERTY = "configUrl";

	/**
	 * The constant LAYOUT_PROPERTY.
	 */
	public static final String LAYOUT_PROPERTY = "layout";

	/**
	 * The constant FILTER_PROPERTY.
	 */
	public static final String FILTER_PROPERTY = "filter";

	/**
	 * The constant URLS_PROPERTY.
	 */
	public static final String URLS_PROPERTY = "urls";

	/**
	 * The constant OAUTH2_REDIRECT_URL_PROPERTY.
	 */
	public static final String OAUTH2_REDIRECT_URL_PROPERTY = "oauth2RedirectUrl";

	/**
	 * The Ui root path.
	 */
	private String uiRootPath;

	/**
	 * The Swagger ui config.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfig;

	/**
	 * Instantiates a new Swagger ui config parameters.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 */
	public SwaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfig) {
		this.swaggerUiConfig = swaggerUiConfig;
		this.path = StringUtils.defaultIfBlank(swaggerUiConfig.getPath(), Constants.DEFAULT_SWAGGER_UI_PATH);
		this.oauth2RedirectUrl = StringUtils.defaultIfBlank(swaggerUiConfig.getOauth2RedirectUrl(), SWAGGER_UI_OAUTH_REDIRECT_URL);
		this.layout = swaggerUiConfig.getLayout();
		this.configUrl = swaggerUiConfig.getConfigUrl();
		this.validatorUrl = swaggerUiConfig.getValidatorUrl();
		this.filter = swaggerUiConfig.getFilter();
		this.operationsSorter = swaggerUiConfig.getOperationsSorter();
		this.tagsSorter = swaggerUiConfig.getTagsSorter();
		this.deepLinking = swaggerUiConfig.getDeepLinking();
		this.displayOperationId = swaggerUiConfig.getDisplayOperationId();
		this.defaultModelExpandDepth = swaggerUiConfig.getDefaultModelExpandDepth();
		this.defaultModelsExpandDepth = swaggerUiConfig.getDefaultModelsExpandDepth();
		this.defaultModelRendering = swaggerUiConfig.getDefaultModelRendering();
		this.displayRequestDuration = swaggerUiConfig.getDisplayRequestDuration();
		this.docExpansion = swaggerUiConfig.getDocExpansion();
		this.maxDisplayedTags = swaggerUiConfig.getMaxDisplayedTags();
		this.showCommonExtensions = swaggerUiConfig.getShowCommonExtensions();
		this.showExtensions = swaggerUiConfig.getShowExtensions();
		this.supportedSubmitMethods = swaggerUiConfig.getSupportedSubmitMethods();
		this.url = swaggerUiConfig.getUrl();
		this.urls = swaggerUiConfig.getUrls() == null ? new HashSet<>() : swaggerUiConfig.getUrls();
		this.urlsPrimaryName = swaggerUiConfig.getUrlsPrimaryName();
		this.groupsOrder = swaggerUiConfig.getGroupsOrder();
		this.syntaxHighlight = swaggerUiConfig.getSyntaxHighlight();
		this.tryItOutEnabled = swaggerUiConfig.getTryItOutEnabled();
		this.persistAuthorization = swaggerUiConfig.getPersistAuthorization();
	}

	/**
	 * Add group.
	 *
	 * @param group the group
	 */
	public void addGroup(String group) {
		SwaggerUrl swaggerUrl = new SwaggerUrl(group);
		urls.add(swaggerUrl);
	}

	/**
	 * Add url.
	 *
	 * @param url the url
	 */
	public void addUrl(String url) {
		this.urls.forEach(elt ->
				{
					if (!isSwaggerUrlDefined(elt.getName()))
						elt.setUrl(url + DEFAULT_PATH_SEPARATOR + elt.getName());
				}
		);
	}

	/**
	 * Gets ui root path.
	 *
	 * @return the ui root path
	 */
	public String getUiRootPath() {
		return uiRootPath;
	}

	/**
	 * Sets ui root path.
	 *
	 * @param uiRootPath the ui root path
	 */
	public void setUiRootPath(String uiRootPath) {
		this.uiRootPath = uiRootPath;
	}

	/**
	 * Is valid url boolean.
	 *
	 * @param url the url
	 * @return the boolean
	 */
	public boolean isValidUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets config parameters.
	 *
	 * @return the config parameters
	 */
	public Map<String, Object> getConfigParameters() {
		final Map<String, Object> params = new TreeMap<>();
		// empty-string prevents swagger-ui default validation
		params.put("validatorUrl", validatorUrl != null ? validatorUrl : "");
		SpringDocPropertiesUtils.put(CONFIG_URL_PROPERTY, configUrl, params);
		SpringDocPropertiesUtils.put("deepLinking", this.deepLinking, params);
		SpringDocPropertiesUtils.put("displayOperationId", displayOperationId, params);
		SpringDocPropertiesUtils.put("defaultModelsExpandDepth", defaultModelsExpandDepth, params);
		SpringDocPropertiesUtils.put("defaultModelExpandDepth", defaultModelExpandDepth, params);
		SpringDocPropertiesUtils.put("defaultModelRendering", defaultModelRendering, params);
		SpringDocPropertiesUtils.put("displayRequestDuration", displayRequestDuration, params);
		SpringDocPropertiesUtils.put("docExpansion", docExpansion, params);
		SpringDocPropertiesUtils.put("maxDisplayedTags", maxDisplayedTags, params);
		SpringDocPropertiesUtils.put("showExtensions", showExtensions, params);
		SpringDocPropertiesUtils.put("showCommonExtensions", showCommonExtensions, params);
		SpringDocPropertiesUtils.put("operationsSorter", operationsSorter, params);
		SpringDocPropertiesUtils.put("tagsSorter", tagsSorter, params);
		if (supportedSubmitMethods!=null)
			SpringDocPropertiesUtils.put("supportedSubmitMethods", supportedSubmitMethods.toString(), params);
		SpringDocPropertiesUtils.put(OAUTH2_REDIRECT_URL_PROPERTY, oauth2RedirectUrl, params);
		SpringDocPropertiesUtils.put("url", url, params);
		put(URLS_PROPERTY, urls, params);
		SpringDocPropertiesUtils.put("urls.primaryName", urlsPrimaryName, params);
		SpringDocPropertiesUtils.put("tryItOutEnabled", tryItOutEnabled, params);
		SpringDocPropertiesUtils.put("persistAuthorization", persistAuthorization, params);
		return params;
	}

	/**
	 * Put.
	 *
	 * @param urls the urls
	 * @param swaggerUrls the swagger urls
	 * @param params the params
	 */
	private void put(String urls, Set<SwaggerUrl> swaggerUrls, Map<String, Object> params) {
		Comparator<SwaggerUrl> swaggerUrlComparator;
		if (groupsOrder.isAscending())
			swaggerUrlComparator = Comparator.comparing(SwaggerUrl::getName);
		else
			swaggerUrlComparator = (h1, h2) -> h2.getName().compareTo(h1.getName());

		swaggerUrls = swaggerUrls.stream().sorted(swaggerUrlComparator).filter(elt -> StringUtils.isNotEmpty(elt.getUrl())).collect(Collectors.toCollection(LinkedHashSet::new));
		if (!CollectionUtils.isEmpty(swaggerUrls)) {
			params.put(urls, swaggerUrls);
		}
	}

	/**
	 * Is swagger url defined boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 */
	private boolean isSwaggerUrlDefined(String name) {
		if (!CollectionUtils.isEmpty(swaggerUiConfig.getUrls()))
			return swaggerUiConfig.getUrls().stream().anyMatch(swaggerUrl -> name.equals(swaggerUrl.getName()) && StringUtils.isNotBlank(swaggerUrl.getUrl()));
		return false;
	}
}