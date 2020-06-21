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

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;


/**
 * The type Swagger ui config properties.
 * @author bnasslahsen
 */
@Configuration
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

}