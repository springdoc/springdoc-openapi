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

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import static org.springdoc.core.Constants.GROUP_NAME_NOT_NULL;

/**
 * Please refer to the swagger
 * <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md">configuration.md</a>
 * to get the idea what each parameter does.
 * @author bnasslahsen
 */
public abstract class AbstractSwaggerUiConfigProperties {

	/**
	 * The path for the Swagger UI pages to load. Will redirect to the springdoc.webjars.prefix property.
	 */
	protected String path;

	/**
	 * The name of a component available via the plugin system to use as the top-level layout for Swagger UI.
	 */
	protected String layout;

	/**
	 * URL to fetch external configuration document from.
	 */
	protected String configUrl;

	/**
	 * URL to validate specs against.
	 */
	protected String validatorUrl;

	/**
	 * If set, enables filtering. The top bar will show an edit box that
	 * could be used to filter the tagged operations that are shown.
	 */
	protected String filter;

	/**
	 * Apply a sort to the operation list of each API
	 */
	protected String operationsSorter;

	/**
	 * Apply a sort to the tag list of each API
	 */
	protected String tagsSorter;

	/**
	 * Enables or disables deep linking for tags and operations.
	 *
	 * @see  <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/deep-linking.md">deep-linking.md</a>
	 */
	protected Boolean deepLinking;

	/**
	 * Controls the display of operationId in operations list.
	 */
	protected Boolean displayOperationId;

	/**
	 * The default expansion depth for models (set to -1 completely hide the models).
	 */
	protected Integer defaultModelsExpandDepth;

	/**
	 * The default expansion depth for the model on the model-example section.
	 */
	protected Integer defaultModelExpandDepth;

	/**
	 * Controls how the model is shown when the API is first rendered.
	 */
	protected String defaultModelRendering;

	/**
	 * Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
	 */
	protected Boolean displayRequestDuration;

	/**
	 * Controls the default expansion setting for the operations and tags.
	 */
	protected String docExpansion;

	/**
	 * If set, limits the number of tagged operations displayed to at most this many.
	 */
	protected Integer maxDisplayedTags;

	/**
	 * Controls the display of vendor extension (x-) fields and values.
	 */
	protected Boolean showExtensions;

	/**
	 * Controls the display of extensions
	 */
	protected Boolean showCommonExtensions;

	/**
	 * The supported try it out methods
	 */
	protected List<String> supportedSubmitMethods;

	/**
	 * OAuth redirect URL.
	 */
	protected String oauth2RedirectUrl;

	/**
	 * The Url.
	 */
	protected String url;

	/**
	 * The Urls.
	 */
	protected Set<SwaggerUrl> urls;

	/**
	 * The Groups order.
	 */
	protected Direction groupsOrder = Direction.ASC;

	/**
	 * The Urls primary name.
	 */
	protected String urlsPrimaryName;

	/**
	 * Try it out enabled
	 */
	protected Boolean tryItOutEnabled;

	/**
	 * The Persist authorization.
	 */
	protected Boolean persistAuthorization;

	/**
	 * The Query config enabled.
	 */
	protected Boolean queryConfigEnabled;

	/**
	 * The With credentials.
	 */
	protected Boolean withCredentials;

	/**
	 * Gets with credentials.
	 *
	 * @return the with credentials
	 */
	public Boolean getWithCredentials() {
		return withCredentials;
	}

	/**
	 * Sets with credentials.
	 *
	 * @param withCredentials the with credentials
	 */
	public void setWithCredentials(Boolean withCredentials) {
		this.withCredentials = withCredentials;
	}

	/**
	 * Gets query config enabled.
	 *
	 * @return the query config enabled
	 */
	public Boolean getQueryConfigEnabled() {
		return queryConfigEnabled;
	}

	/**
	 * Sets query config enabled.
	 *
	 * @param queryConfigEnabled the query config enabled
	 */
	public void setQueryConfigEnabled(Boolean queryConfigEnabled) {
		this.queryConfigEnabled = queryConfigEnabled;
	}

	/**
	 * Gets try it out enabled
	 * @return try it out enabled
	 */
	public Boolean getTryItOutEnabled() {
		return tryItOutEnabled;
	}

	/**
	 * Sets try it out enabled
	 * @param tryItOutEnabled try it out enabled
	 */
	public void setTryItOutEnabled(Boolean tryItOutEnabled) {
		this.tryItOutEnabled = tryItOutEnabled;
	}

	/**
	 * Gets persist authorization.
	 *
	 * @return the persist authorization
	 */
	public Boolean getPersistAuthorization() {
		return persistAuthorization;
	}

	/**
	 * Sets persist authorization.
	 *
	 * @param persistAuthorization the persist authorization
	 */
	public void setPersistAuthorization(Boolean persistAuthorization) {
		this.persistAuthorization = persistAuthorization;
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
	 */
	public static class SwaggerUrl {
		/**
		 * The Url.
		 */
		@JsonProperty("url")
		private String url;

		/**
		 * The Name.
		 */
		@JsonIgnore
		private String name;

		/**
		 * The Display name.
		 */
		@JsonProperty("name")
		private String displayName;

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
		 * @param displayName the display name
		 */
		public SwaggerUrl(String group, String url, String displayName) {
			Objects.requireNonNull(group, GROUP_NAME_NOT_NULL);
			this.url = url;
			this.name = group;
			this.displayName = StringUtils.defaultIfEmpty(displayName, this.name);
		}

		/**
		 * Gets display name.
		 *
		 * @return the display name
		 */
		public String getDisplayName() {
			return displayName;
		}

		/**
		 * Sets display name.
		 *
		 * @param displayName the display name
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
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