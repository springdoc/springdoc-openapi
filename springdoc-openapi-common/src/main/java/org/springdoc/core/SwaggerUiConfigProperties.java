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
public class SwaggerUiConfigProperties extends AbstractSwaggerUiConfigProperties{

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
	 * The Csrf configuration.
	 */
	private Csrf csrf = new Csrf();

	/**
	 * Whether to generate and serve an OpenAPI document.
	 */
	private boolean enabled = true;

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
	 * The type Csrf.
	 */
	public static class Csrf {

		/**
		 * The Enabled.
		 */
		private boolean enabled;

		/**
		 * The Cookie name.
		 */
		private String cookieName = Constants.CSRF_DEFAULT_COOKIE_NAME;

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
	public boolean isCsrfEnabled(){
		return csrf.isEnabled();
	}

	/**
	 * The type Syntax highlight.
	 */
	public static class SyntaxHighlight{

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
	}
}