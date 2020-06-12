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
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.GROUP_NAME_NOT_NULL;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * Please refer to the swagger
 * <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md">configuration.md</a>
 * to get the idea what each parameter does.
 * @author bnasslahsen
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiConfigProperties {

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
	private String path = Constants.DEFAULT_SWAGGER_UI_PATH;

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
	private Set<SwaggerUrl> urls = new HashSet<>();

	/**
	 * The Groups order.
	 */
	private Direction groupsOrder = Direction.ASC;

	/**
	 * The Urls primary name.
	 */
	private String urlsPrimaryName;

	/**
	 * The Disable swagger default url.
	 */
	private boolean disableSwaggerDefaultUrl;

	/**
	 * The Display query params.
	 */
	private boolean displayQueryParams;

	/**
	 * The Display query params without oauth 2.
	 */
	private boolean displayQueryParamsWithoutOauth2;

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
		return this.urls;
	}

	/**
	 * Sets urls.
	 *
	 * @param urls the urls
	 */
	public void setUrls(Set<SwaggerUrl> urls) {
		this.urls = urls;
	}

	/**
	 * Add url.
	 *
	 * @param url the url
	 */
	public void addUrl(String url) {
		this.urls.forEach(elt ->
				{
					if (StringUtils.isBlank(elt.url))
						elt.setUrl(url + DEFAULT_PATH_SEPARATOR + elt.getName());
				}
		);
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
	 * Gets validator url.
	 *
	 * @return the validator url
	 */
	public String getValidatorUrl() {
		return validatorUrl;
	}

	/**
	 * Sets validator url.
	 *
	 * @param validatorUrl the validator url
	 */
	public void setValidatorUrl(String validatorUrl) {
		this.validatorUrl = validatorUrl;
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
	 * Gets operations sorter.
	 *
	 * @return the operations sorter
	 */
	public String getOperationsSorter() {
		return operationsSorter;
	}

	/**
	 * Sets operations sorter.
	 *
	 * @param operationsSorter the operations sorter
	 */
	public void setOperationsSorter(String operationsSorter) {
		this.operationsSorter = operationsSorter;
	}

	/**
	 * Gets tags sorter.
	 *
	 * @return the tags sorter
	 */
	public String getTagsSorter() {
		return tagsSorter;
	}

	/**
	 * Sets tags sorter.
	 *
	 * @param tagsSorter the tags sorter
	 */
	public void setTagsSorter(String tagsSorter) {
		this.tagsSorter = tagsSorter;
	}

	/**
	 * Gets deep linking.
	 *
	 * @return the deep linking
	 */
	public Boolean getDeepLinking() {
		return deepLinking;
	}

	/**
	 * Sets deep linking.
	 *
	 * @param deepLinking the deep linking
	 */
	public void setDeepLinking(Boolean deepLinking) {
		this.deepLinking = deepLinking;
	}

	/**
	 * Gets display operation id.
	 *
	 * @return the display operation id
	 */
	public Boolean getDisplayOperationId() {
		return displayOperationId;
	}

	/**
	 * Sets display operation id.
	 *
	 * @param displayOperationId the display operation id
	 */
	public void setDisplayOperationId(Boolean displayOperationId) {
		this.displayOperationId = displayOperationId;
	}

	/**
	 * Gets default models expand depth.
	 *
	 * @return the default models expand depth
	 */
	public Integer getDefaultModelsExpandDepth() {
		return defaultModelsExpandDepth;
	}

	/**
	 * Sets default models expand depth.
	 *
	 * @param defaultModelsExpandDepth the default models expand depth
	 */
	public void setDefaultModelsExpandDepth(Integer defaultModelsExpandDepth) {
		this.defaultModelsExpandDepth = defaultModelsExpandDepth;
	}

	/**
	 * Gets default model expand depth.
	 *
	 * @return the default model expand depth
	 */
	public Integer getDefaultModelExpandDepth() {
		return defaultModelExpandDepth;
	}

	/**
	 * Sets default model expand depth.
	 *
	 * @param defaultModelExpandDepth the default model expand depth
	 */
	public void setDefaultModelExpandDepth(Integer defaultModelExpandDepth) {
		this.defaultModelExpandDepth = defaultModelExpandDepth;
	}

	/**
	 * Gets default model rendering.
	 *
	 * @return the default model rendering
	 */
	public String getDefaultModelRendering() {
		return defaultModelRendering;
	}

	/**
	 * Sets default model rendering.
	 *
	 * @param defaultModelRendering the default model rendering
	 */
	public void setDefaultModelRendering(String defaultModelRendering) {
		this.defaultModelRendering = defaultModelRendering;
	}

	/**
	 * Gets display request duration.
	 *
	 * @return the display request duration
	 */
	public Boolean getDisplayRequestDuration() {
		return displayRequestDuration;
	}

	/**
	 * Sets display request duration.
	 *
	 * @param displayRequestDuration the display request duration
	 */
	public void setDisplayRequestDuration(Boolean displayRequestDuration) {
		this.displayRequestDuration = displayRequestDuration;
	}

	/**
	 * Gets doc expansion.
	 *
	 * @return the doc expansion
	 */
	public String getDocExpansion() {
		return docExpansion;
	}

	/**
	 * Sets doc expansion.
	 *
	 * @param docExpansion the doc expansion
	 */
	public void setDocExpansion(String docExpansion) {
		this.docExpansion = docExpansion;
	}

	/**
	 * Gets max displayed tags.
	 *
	 * @return the max displayed tags
	 */
	public Integer getMaxDisplayedTags() {
		return maxDisplayedTags;
	}

	/**
	 * Sets max displayed tags.
	 *
	 * @param maxDisplayedTags the max displayed tags
	 */
	public void setMaxDisplayedTags(Integer maxDisplayedTags) {
		this.maxDisplayedTags = maxDisplayedTags;
	}

	/**
	 * Gets show extensions.
	 *
	 * @return the show extensions
	 */
	public Boolean getShowExtensions() {
		return showExtensions;
	}

	/**
	 * Sets show extensions.
	 *
	 * @param showExtensions the show extensions
	 */
	public void setShowExtensions(Boolean showExtensions) {
		this.showExtensions = showExtensions;
	}

	/**
	 * Gets show common extensions.
	 *
	 * @return the show common extensions
	 */
	public Boolean getShowCommonExtensions() {
		return showCommonExtensions;
	}

	/**
	 * Sets show common extensions.
	 *
	 * @param showCommonExtensions the show common extensions
	 */
	public void setShowCommonExtensions(Boolean showCommonExtensions) {
		this.showCommonExtensions = showCommonExtensions;
	}

	/**
	 * Gets supported submit methods.
	 *
	 * @return the supported submit methods
	 */
	public List<String> getSupportedSubmitMethods() {
		return supportedSubmitMethods;
	}

	/**
	 * Sets supported submit methods.
	 *
	 * @param supportedSubmitMethods the supported submit methods
	 */
	public void setSupportedSubmitMethods(List<String> supportedSubmitMethods) {
		this.supportedSubmitMethods = supportedSubmitMethods;
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
	 * Is disable swagger default url boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDisableSwaggerDefaultUrl() {
		return disableSwaggerDefaultUrl;
	}

	/**
	 * Sets disable swagger default url.
	 *
	 * @param disableSwaggerDefaultUrl the disable swagger default url
	 */
	public void setDisableSwaggerDefaultUrl(boolean disableSwaggerDefaultUrl) {
		this.disableSwaggerDefaultUrl = disableSwaggerDefaultUrl;
	}

	/**
	 * Is display query params boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDisplayQueryParams() {
		return displayQueryParams;
	}

	/**
	 * Sets display query params.
	 *
	 * @param displayQueryParams the display query params
	 */
	public void setDisplayQueryParams(boolean displayQueryParams) {
		this.displayQueryParams = displayQueryParams;
	}

	/**
	 * Is display query params without oauth 2 boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDisplayQueryParamsWithoutOauth2() {
		return displayQueryParamsWithoutOauth2;
	}

	/**
	 * Sets display query params without oauth 2.
	 *
	 * @param displayQueryParamsWithoutOauth2 the display query params without oauth 2
	 */
	public void setDisplayQueryParamsWithoutOauth2(boolean displayQueryParamsWithoutOauth2) {
		this.displayQueryParamsWithoutOauth2 = displayQueryParamsWithoutOauth2;
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
	 * Gets groups order.
	 *
	 * @return the groups order
	 */
	public Direction getGroupsOrder() {
		return groupsOrder;
	}

	/**
	 * Sets groups order.
	 *
	 * @param groupsOrder the groups order
	 */
	public void setGroupsOrder(Direction groupsOrder) {
		this.groupsOrder = groupsOrder;
	}

	/**
	 * Gets urls primary name.
	 *
	 * @return the urls primary name
	 */
	public String getUrlsPrimaryName() {
		return urlsPrimaryName;
	}

	/**
	 * Sets urls primary name.
	 *
	 * @param urlsPrimaryName the urls primary name
	 */
	public void setUrlsPrimaryName(String urlsPrimaryName) {
		this.urlsPrimaryName = urlsPrimaryName;
	}

	/**
	 * The enum Direction.
	 * @author bnasslahsen
	 */
	enum Direction {
		/**
		 *Asc direction.
		 */
		ASC,
		/**
		 *Desc direction.
		 */
		DESC;

		/**
		 * Is ascending boolean.
		 *
		 * @return the boolean
		 */
		public boolean isAscending() {
			return this.equals(ASC);
		}
	}

	/**
	 * The type Swagger url.
	 * @author bnasslahsen
	 */
	static class SwaggerUrl {
		/**
		 * The Url.
		 */
		private String url;

		/**
		 * The Name.
		 */
		private String name;

		/**
		 * Instantiates a new Swagger url.
		 */
		public SwaggerUrl() {
		}

		/**
		 * Instantiates a new Swagger url.
		 *
		 * @param group the group
		 * @param url the url
		 */
		public SwaggerUrl(String group, String url) {
			Objects.requireNonNull(group, GROUP_NAME_NOT_NULL);
			this.url = url;
			this.name = group;
		}

		/**
		 * Instantiates a new Swagger url.
		 *
		 * @param group the group
		 */
		public SwaggerUrl(String group) {
			Objects.requireNonNull(group, GROUP_NAME_NOT_NULL);
			this.name = group;
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
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets name.
		 *
		 * @param name the name
		 */
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			SwaggerUrl that = (SwaggerUrl) o;
			return name.equals(that.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name);
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("SwaggerUrl{");
			sb.append("url='").append(url).append('\'');
			sb.append(", name='").append(name).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}