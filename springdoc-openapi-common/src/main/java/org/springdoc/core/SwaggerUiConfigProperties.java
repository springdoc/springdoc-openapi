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

package org.springdoc.core;

import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
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

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SWAGGER_UI_OAUTH_REDIRECT_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * Please refer to the swagger
 * <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md">configuration.md</a>
 * to get the idea what each parameter does.
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiConfigProperties {

	public static final String CONFIG_URL_PROPERTY = "configUrl";

	private static Set<SwaggerUrl> swaggerUrls = new HashSet<>();

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
	 * @see <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/deep-linking.md">deep-linking.md</a>
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

	private String url;

	private Direction groupsOrder = Direction.ASC;

	public static void addGroup(String group) {
		SwaggerUrl swaggerUrl = new SwaggerUrl(group);
		swaggerUrls.add(swaggerUrl);
	}

	public static void addGroup(String group, String url) {
		SwaggerUrl swaggerUrl = new SwaggerUrl(group, url);
		swaggerUrls.add(swaggerUrl);
	}

	public static Set<SwaggerUrl> getSwaggerUrls() {
		return swaggerUrls;
	}

	public static void setSwaggerUrls(Set<SwaggerUrl> swaggerUrls) {
		SwaggerUiConfigProperties.swaggerUrls = swaggerUrls;
	}

	public static void addUrl(String url) {
		swaggerUrls.forEach(elt -> elt.setUrl(url + DEFAULT_PATH_SEPARATOR + elt.getName()));
	}

	public Map<String, Object> getConfigParameters() {
		final Map<String, Object> params = new TreeMap<>();
		SpringDocPropertiesUtils.put("layout", layout, params);
		SpringDocPropertiesUtils.put(CONFIG_URL_PROPERTY, configUrl, params);
		// empty-string prevents swagger-ui default validation
		params.put("validatorUrl", validatorUrl != null ? validatorUrl : "");
		SpringDocPropertiesUtils.put("filter", filter, params);
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
		SpringDocPropertiesUtils.put("oauth2RedirectUrl", oauth2RedirectUrl, params);
		SpringDocPropertiesUtils.put("url", url, params);
		put("urls", swaggerUrls, params);
		return params;
	}

	public String getValidatorUrl() {
		return validatorUrl;
	}

	public void setValidatorUrl(String validatorUrl) {
		this.validatorUrl = validatorUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getConfigUrl() {
		return configUrl;
	}

	public void setConfigUrl(String configUrl) {
		this.configUrl = configUrl;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOperationsSorter() {
		return operationsSorter;
	}

	public void setOperationsSorter(String operationsSorter) {
		this.operationsSorter = operationsSorter;
	}

	public String getTagsSorter() {
		return tagsSorter;
	}

	public void setTagsSorter(String tagsSorter) {
		this.tagsSorter = tagsSorter;
	}

	public Boolean getDeepLinking() {
		return deepLinking;
	}

	public void setDeepLinking(Boolean deepLinking) {
		this.deepLinking = deepLinking;
	}

	public Boolean getDisplayOperationId() {
		return displayOperationId;
	}

	public void setDisplayOperationId(Boolean displayOperationId) {
		this.displayOperationId = displayOperationId;
	}

	public Integer getDefaultModelsExpandDepth() {
		return defaultModelsExpandDepth;
	}

	public void setDefaultModelsExpandDepth(Integer defaultModelsExpandDepth) {
		this.defaultModelsExpandDepth = defaultModelsExpandDepth;
	}

	public Integer getDefaultModelExpandDepth() {
		return defaultModelExpandDepth;
	}

	public void setDefaultModelExpandDepth(Integer defaultModelExpandDepth) {
		this.defaultModelExpandDepth = defaultModelExpandDepth;
	}

	public String getDefaultModelRendering() {
		return defaultModelRendering;
	}

	public void setDefaultModelRendering(String defaultModelRendering) {
		this.defaultModelRendering = defaultModelRendering;
	}

	public Boolean getDisplayRequestDuration() {
		return displayRequestDuration;
	}

	public void setDisplayRequestDuration(Boolean displayRequestDuration) {
		this.displayRequestDuration = displayRequestDuration;
	}

	public String getDocExpansion() {
		return docExpansion;
	}

	public void setDocExpansion(String docExpansion) {
		this.docExpansion = docExpansion;
	}

	public Integer getMaxDisplayedTags() {
		return maxDisplayedTags;
	}

	public void setMaxDisplayedTags(Integer maxDisplayedTags) {
		this.maxDisplayedTags = maxDisplayedTags;
	}

	public Boolean getShowExtensions() {
		return showExtensions;
	}

	public void setShowExtensions(Boolean showExtensions) {
		this.showExtensions = showExtensions;
	}

	public Boolean getShowCommonExtensions() {
		return showCommonExtensions;
	}

	public void setShowCommonExtensions(Boolean showCommonExtensions) {
		this.showCommonExtensions = showCommonExtensions;
	}

	public List<String> getSupportedSubmitMethods() {
		return supportedSubmitMethods;
	}

	public void setSupportedSubmitMethods(List<String> supportedSubmitMethods) {
		this.supportedSubmitMethods = supportedSubmitMethods;
	}

	public String getOauth2RedirectUrl() {
		return oauth2RedirectUrl;
	}

	public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
		this.oauth2RedirectUrl = oauth2RedirectUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isValidUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	private void put(String urls, Set<SwaggerUrl> swaggerUrls, Map<String, Object> params) {
		Comparator<SwaggerUrl> swaggerUrlComparator;
		if (groupsOrder.isAscending())
			swaggerUrlComparator = (h1, h2) -> h1.getName().compareTo(h2.getName());
		else
			swaggerUrlComparator = (h1, h2) -> h2.getName().compareTo(h1.getName());

		swaggerUrls = swaggerUrls.stream().sorted(swaggerUrlComparator).filter(elt -> StringUtils.isNotEmpty(elt.getUrl())).collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(swaggerUrls)) {
			params.put(urls, swaggerUrls);
		}
	}

	static class SwaggerUrl {
		private String url;

		private String name;

		public SwaggerUrl(String group, String url) {
			this.url = url;
			this.name = group;
		}

		public SwaggerUrl(String group) {
			this.name = group;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

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
	}

	static enum Direction {
		ASC,
		DESC;

		public boolean isAscending() {
			return this.equals(ASC);
		}

		public boolean isDescending() {
			return this.equals(DESC);
		}
	}

	public Direction getGroupsOrder() {
		return groupsOrder;
	}

	public void setGroupsOrder(Direction groupsOrder) {
		this.groupsOrder = groupsOrder;
	}
}