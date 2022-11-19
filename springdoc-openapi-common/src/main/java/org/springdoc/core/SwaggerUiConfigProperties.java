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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;


/**
 * The type Swagger ui config properties.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiConfigProperties extends AbstractSwaggerUiConfigProperties {

	/**
	 * The Disable swagger default url.
	 */
	private boolean disableSwaggerDefaultUrl;


	/**
	 * The Swagger ui version.
	 */
	private String version;

	/**
	 * The Csrf configuration.
	 */
	private Csrf csrf = new Csrf();

	/**
	 * The Syntax Highlight configuration.
	 */
	private SyntaxHighlight syntaxHighlight = new SyntaxHighlight();

	/**
	 * Whether to generate and serve an OpenAPI document.
	 */
	private boolean enabled = true;

	/**
	 * The Use root path.
	 */
	private boolean useRootPath;

	/**
	 * Gets swagger ui version.
	 *
	 * @return the swagger ui version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets swagger ui version.
	 *
	 * @param version the swagger ui version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Is use root path boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUseRootPath() {
		return useRootPath;
	}

	/**
	 * Sets use root path.
	 *
	 * @param useRootPath the use root path
	 */
	public void setUseRootPath(boolean useRootPath) {
		this.useRootPath = useRootPath;
	}

	/**
	 * Is enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets enabled.
	 *
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	 * Gets csrf.
	 *
	 * @return the csrf
	 */
	public Csrf getCsrf() {
		return csrf;
	}

	/**
	 * Sets csrf.
	 *
	 * @param csrf the csrf
	 */
	public void setCsrf(Csrf csrf) {
		this.csrf = csrf;
	}

	/**
	 * Is csrf enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isCsrfEnabled() {
		return csrf.isEnabled();
	}

	/**
	 * Gets syntaxHighlight.
	 *
	 * @return the syntaxHighlight
	 */
	public SyntaxHighlight getSyntaxHighlight() {
		return syntaxHighlight;
	}

	/**
	 * Sets syntaxHighlight.
	 *
	 * @param syntaxHighlight the syntaxHighlight
	 */
	public void setSyntaxHighlight(SyntaxHighlight syntaxHighlight) {
		this.syntaxHighlight = syntaxHighlight;
	}

	/**
	 * Clone urls set.
	 *
	 * @return the set
	 */
	public Set<SwaggerUrl> cloneUrls() {
		return this.urls.stream().map(swaggerUrl -> new SwaggerUrl(swaggerUrl.getName(), swaggerUrl.getUrl(), swaggerUrl.getDisplayName())).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * The type Csrf.
	 * @author bnasslashen
	 */
	public static class Csrf {

		/**
		 * The Enabled.
		 */
		private boolean enabled;

		/**
		 * Use Local storage.
		 */
		private boolean useLocalStorage;

		/**
		 * Use Session storage.
		 */
		private boolean useSessionStorage;

		/**
		 * The Cookie name.
		 */
		private String cookieName = Constants.CSRF_DEFAULT_COOKIE_NAME;

		/**
		 * The Local storage key.
		 */
		private String localStorageKey = Constants.CSRF_DEFAULT_LOCAL_STORAGE_KEY;

		/**
		 * The Session storage key.
		 */
		private String sessionStorageKey = Constants.CSRF_DEFAULT_LOCAL_STORAGE_KEY;

		/**
		 * The Header name.
		 */
		private String headerName = Constants.CSRF_DEFAULT_HEADER_NAME;

		/**
		 * Is enabled boolean.
		 *
		 * @return the boolean
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Sets enabled.
		 *
		 * @param enabled the enabled
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * Use Local storage boolean.
		 *
		 * @return the boolean
		 */
		public boolean isUseLocalStorage() {
			return useLocalStorage;
		}

		/**
		 * Sets useLocalStorage.
		 *
		 * @param useLocalStorage the use local storage
		 */
		public void setUseLocalStorage(boolean useLocalStorage) {
			this.useLocalStorage = useLocalStorage;
		}

		/**
		 * Use Session storage boolean.
		 *
		 * @return the boolean
		 */
		public boolean isUseSessionStorage() {
			return useSessionStorage;
		}

		/**
		 * Sets useSessionStorage.
		 *
		 * @param useSessionStorage the use local storage
		 */
		public void setUseSessionStorage(boolean useSessionStorage) {
			this.useSessionStorage = useSessionStorage;
		}

		/**
		 * Gets cookie name.
		 *
		 * @return the cookie name
		 */
		public String getCookieName() {
			return cookieName;
		}

		/**
		 * Sets cookie name.
		 *
		 * @param cookieName the cookie name
		 */
		public void setCookieName(String cookieName) {
			this.cookieName = cookieName;
		}

		/**
		 * Gets local storage key.
		 *
		 * @return the cookie name
		 */
		public String getLocalStorageKey() {
			return localStorageKey;
		}

		/**
		 * Sets local storage key.
		 *
		 * @param localStorageKey the local storage key
		 */
		public void setLocalStorageKey(String localStorageKey) {
			this.localStorageKey = localStorageKey;
		}

		/**
		 * Gets session storage key.
		 *
		 * @return the cookie name
		 */
		public String getSessionStorageKey() {
			return sessionStorageKey;
		}

		/**
		 * Sets local storage key.
		 *
		 * @param sessionStorageKey the local storage key
		 */
		public void setSessionStorageKey(String sessionStorageKey) {
			this.sessionStorageKey = sessionStorageKey;
		}

		/**
		 * Gets header name.
		 *
		 * @return the header name
		 */
		public String getHeaderName() {
			return headerName;
		}

		/**
		 * Sets header name.
		 *
		 * @param headerName the header name
		 */
		public void setHeaderName(String headerName) {
			this.headerName = headerName;
		}
	}

	/**
	 * The type Syntax highlight.
	 * @author bnasslashen
	 */
	public static class SyntaxHighlight {

		/**
		 * The Activated.
		 */
		private Boolean activated;

		/**
		 * The Theme.
		 */
		private String theme;

		/**
		 * Gets activated.
		 *
		 * @return the activated
		 */
		public Boolean getActivated() {
			return activated;
		}

		/**
		 * Sets activated.
		 *
		 * @param activated the activated
		 */
		public void setActivated(Boolean activated) {
			this.activated = activated;
		}

		/**
		 * Gets theme.
		 *
		 * @return the theme
		 */
		public String getTheme() {
			return theme;
		}

		/**
		 * Sets theme.
		 *
		 * @param theme the theme
		 */
		public void setTheme(String theme) {
			this.theme = theme;
		}

		/**
		 * Is present boolean.
		 *
		 * @return the boolean
		 */
		public boolean isPresent() {
			return activated != null || StringUtils.isNotEmpty(theme);
		}
	}

}
