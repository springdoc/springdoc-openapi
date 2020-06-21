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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger ui config parameters.
 * @author bnasslahsen
 */
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiConfigParameters {

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
	 * The path for the Swagger UI pages to load. Will redirect to the springdoc.webjars.prefix property.
	 */
	private String path;

	/**
	 * The name of a component available via the plugin system to use as the top-level layout for Swagger UI.
	 */
	private String layout;

	/**
	 * URL to fetch external configuration document from.
	 */
	private String configUrl;

	/**
	 * URL to validate specs against.
	 */
	private String validatorUrl;

	/**
	 * If set, enables filtering. The top bar will show an edit box that
	 * could be used to filter the tagged operations that are shown.
	 */
	private String filter;

	/**
	 * Apply a sort to the operation list of each API
	 */
	private String operationsSorter;

	/**
	 * Apply a sort to the tag list of each API
	 */
	private String tagsSorter;

	/**
	 * Enables or disables deep linking for tags and operations.
	 *
	 * @see  <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/deep-linking.md">deep-linking.md</a>
	 */
	private Boolean deepLinking;

	/**
	 * Controls the display of operationId in operations list.
	 */
	private Boolean displayOperationId;

	/**
	 * The default expansion depth for models (set to -1 completely hide the models).
	 */
	private Integer defaultModelsExpandDepth;

	/**
	 * The default expansion depth for the model on the model-example section.
	 */
	private Integer defaultModelExpandDepth;

	/**
	 * Controls how the model is shown when the API is first rendered.
	 */
	private String defaultModelRendering;

	/**
	 * Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
	 */
	private Boolean displayRequestDuration;

	/**
	 * Controls the default expansion setting for the operations and tags.
	 */
	private String docExpansion;

	/**
	 * If set, limits the number of tagged operations displayed to at most this many.
	 */
	private Integer maxDisplayedTags;

	/**
	 * Controls the display of vendor extension (x-) fields and values.
	 */
	private Boolean showExtensions;

	/**
	 * Controls the display of extensions
	 */
	private Boolean showCommonExtensions;

	/**
	 * The supported try it out methods
	 */
	private List<String> supportedSubmitMethods;

	/**
	 * OAuth redirect URL.
	 */
	private String oauth2RedirectUrl = SWAGGER_UI_OAUTH_REDIRECT_URL;

	/**
	 * The Url.
	 */
	private String url;

	/**
	 * The Urls.
	 */
	private Set<SwaggerUrl> urls;

	/**
	 * The Groups order.
	 */
	private Direction groupsOrder;

	/**
	 * The Urls primary name.
	 */
	private String urlsPrimaryName;

	/**
	 * The Ui root path.
	 */
	private String uiRootPath;

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
	 * Gets urls.
	 *
	 * @return the urls
	 */
	public Set<SwaggerUrl> getUrls() {
		return urls;
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

	private boolean isSwaggerUrlDefined(String name) {
		if (!CollectionUtils.isEmpty(swaggerUiConfig.getUrls()))
			return swaggerUiConfig.getUrls().stream().anyMatch(swaggerUrl -> name.equals(swaggerUrl.getName()) && StringUtils.isNotBlank(swaggerUrl.getUrl()));
		return false;
	}

	/**
	 * Gets path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets path.
	 *
	 * @param path the path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets layout.
	 *
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * Sets layout.
	 *
	 * @param layout the layout
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * Gets config url.
	 *
	 * @return the config url
	 */
	public String getConfigUrl() {
		return configUrl;
	}

	/**
	 * Sets config url.
	 *
	 * @param configUrl the config url
	 */
	public void setConfigUrl(String configUrl) {
		this.configUrl = configUrl;
	}

	/**
	 * Gets filter.
	 *
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets filter.
	 *
	 * @param filter the filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Gets oauth 2 redirect url.
	 *
	 * @return the oauth 2 redirect url
	 */
	public String getOauth2RedirectUrl() {
		return oauth2RedirectUrl;
	}

	/**
	 * Sets oauth 2 redirect url.
	 *
	 * @param oauth2RedirectUrl the oauth 2 redirect url
	 */
	public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
		this.oauth2RedirectUrl = oauth2RedirectUrl;
	}

	/**
	 * Gets url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets url.
	 *
	 * @param url the url
	 */
	public void setUrl(String url) {
		this.url = url;
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
		if (!CollectionUtils.isEmpty(supportedSubmitMethods))
			SpringDocPropertiesUtils.put("supportedSubmitMethods", supportedSubmitMethods.toString(), params);
		SpringDocPropertiesUtils.put(OAUTH2_REDIRECT_URL_PROPERTY, oauth2RedirectUrl, params);
		SpringDocPropertiesUtils.put("url", url, params);
		put(URLS_PROPERTY, urls, params);
		SpringDocPropertiesUtils.put("urls.primaryName", urlsPrimaryName, params);
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
}