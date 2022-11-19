/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.net.URL;
import java.util.Comparator;
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
	 * The constant URL_PROPERTY.
	 */
	public static final String URL_PROPERTY = "url";

	/**
	 * The constant URLS_PROPERTY.
	 */
	public static final String URLS_PROPERTY = "urls";

	/**
	 * The constant OAUTH2_REDIRECT_URL_PROPERTY.
	 */
	public static final String OAUTH2_REDIRECT_URL_PROPERTY = "oauth2RedirectUrl";

	/**
	 * The constant VALIDATOR_URL_PROPERTY.
	 */
	public static final String VALIDATOR_URL_PROPERTY = "validatorUrl";

	/**
	 * The constant QUERY_CONFIG_ENABLED_PROPERTY.
	 */
	public static final String QUERY_CONFIG_ENABLED_PROPERTY = "queryConfigEnabled";

	/**
	 * The constant DISPLAY_OPERATION_ID.
	 */
	public static final String DISPLAY_OPERATION_ID_PROPERTY = "displayOperationId";

	/**
	 * The constant DEEP_LINKING.
	 */
	public static final String DEEP_LINKING_PROPERTY = "deepLinking";

	/**
	 * The constant DISPLAY_REQUEST_DURATION.
	 */
	public static final String DISPLAY_REQUEST_DURATION_PROPERTY = "displayRequestDuration";

	/**
	 * The constant SHOW_EXTENSIONS_PROPERTY.
	 */
	public static final String SHOW_EXTENSIONS_PROPERTY = "showExtensions";

	/**
	 * The constant SHOW_COMMON_EXTENSIONS_PROPERTY.
	 */
	public static final String SHOW_COMMON_EXTENSIONS_PROPERTY = "showCommonExtensions";

	/**
	 * The constant TRY_IT_ENABLED_PROPERTY.
	 */
	public static final String TRY_IT_ENABLED_PROPERTY = "tryItOutEnabled";

	/**
	 * The constant PERSIST_AUTHORIZATION_PROPERTY.
	 */
	public static final String PERSIST_AUTHORIZATION_PROPERTY = "persistAuthorization";

	/**
	 * The constant WITH_CREDENTIALS_PROPERTY.
	 */
	public static final String WITH_CREDENTIALS_PROPERTY = "withCredentials";

	/**
	 * The Swagger ui config.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfig;

	/**
	 * The Ui root path.
	 */
	private String uiRootPath;


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
		this.urls = swaggerUiConfig.getUrls() == null ? new LinkedHashSet<>() : swaggerUiConfig.cloneUrls();
		this.urlsPrimaryName = swaggerUiConfig.getUrlsPrimaryName();
		this.groupsOrder = swaggerUiConfig.getGroupsOrder();
		this.tryItOutEnabled = swaggerUiConfig.getTryItOutEnabled();
		this.persistAuthorization = swaggerUiConfig.getPersistAuthorization();
		this.queryConfigEnabled = swaggerUiConfig.getQueryConfigEnabled();
		this.withCredentials = swaggerUiConfig.getWithCredentials();
	}

	/**
	 * Add group.
	 *
	 * @param group the group
	 * @param displayName the display name
	 */
	public void addGroup(String group, String displayName) {
		SwaggerUrl swaggerUrl = new SwaggerUrl(group, null, displayName);
		urls.add(swaggerUrl);
	}

	/**
	 * Add group.
	 *
	 * @param group the group
	 */
	public void addGroup(String group) {
		SwaggerUrl swaggerUrl = new SwaggerUrl(group, null, group);
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
		params.put(VALIDATOR_URL_PROPERTY, validatorUrl != null ? validatorUrl : "");
		SpringDocPropertiesUtils.put(CONFIG_URL_PROPERTY, configUrl, params);
		SpringDocPropertiesUtils.put(DEEP_LINKING_PROPERTY, this.deepLinking, params);
		SpringDocPropertiesUtils.put(DISPLAY_OPERATION_ID_PROPERTY, displayOperationId, params);
		SpringDocPropertiesUtils.put("defaultModelsExpandDepth", defaultModelsExpandDepth, params);
		SpringDocPropertiesUtils.put("defaultModelExpandDepth", defaultModelExpandDepth, params);
		SpringDocPropertiesUtils.put("defaultModelRendering", defaultModelRendering, params);
		SpringDocPropertiesUtils.put(DISPLAY_REQUEST_DURATION_PROPERTY, displayRequestDuration, params);
		SpringDocPropertiesUtils.put("docExpansion", docExpansion, params);
		SpringDocPropertiesUtils.put("maxDisplayedTags", maxDisplayedTags, params);
		SpringDocPropertiesUtils.put(SHOW_EXTENSIONS_PROPERTY, showExtensions, params);
		SpringDocPropertiesUtils.put(SHOW_COMMON_EXTENSIONS_PROPERTY, showCommonExtensions, params);
		SpringDocPropertiesUtils.put("operationsSorter", operationsSorter, params);
		SpringDocPropertiesUtils.put("tagsSorter", tagsSorter, params);
		SpringDocPropertiesUtils.put(SwaggerUiConfigParameters.LAYOUT_PROPERTY, layout, params);
		if (supportedSubmitMethods != null)
			SpringDocPropertiesUtils.put("supportedSubmitMethods", supportedSubmitMethods.toString(), params);
		SpringDocPropertiesUtils.put(OAUTH2_REDIRECT_URL_PROPERTY, oauth2RedirectUrl, params);
		SpringDocPropertiesUtils.put(URL_PROPERTY, url, params);
		put(URLS_PROPERTY, urls, params);
		SpringDocPropertiesUtils.put("urls.primaryName", urlsPrimaryName, params);
		SpringDocPropertiesUtils.put(TRY_IT_ENABLED_PROPERTY, tryItOutEnabled, params);
		SpringDocPropertiesUtils.put(PERSIST_AUTHORIZATION_PROPERTY, persistAuthorization, params);
		SpringDocPropertiesUtils.put(FILTER_PROPERTY, filter, params);
		SpringDocPropertiesUtils.put(WITH_CREDENTIALS_PROPERTY, withCredentials, params);
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
