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


import java.util.Map;
import java.util.TreeMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;

/**
 * Please refer to the swagger
 * <a href="https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/oauth2.md">configuration.md</a>
 * to get the idea what each parameter does.
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui.oauth")
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerUiOAuthProperties {

	private String clientId;

	private String clientSecret;

	private String realm;

	private String appName;

	private String scopeSeparator;

	private Map<String, String> additionalQueryStringParams;

	private String useBasicAuthenticationWithAccessCodeGrant;

	private Boolean usePkceWithAuthorizationCodeGrant;

	public Map<String, Object> getConfigParameters() {
		final Map<String, Object> params = new TreeMap<>();
		SpringDocPropertiesUtils.put("clientId", clientId, params);
		SpringDocPropertiesUtils.put("clientSecret", clientSecret, params);
		SpringDocPropertiesUtils.put("realm", realm, params);
		SpringDocPropertiesUtils.put("scopeSeparator", scopeSeparator, params);
		SpringDocPropertiesUtils.put("appName", appName, params);
		SpringDocPropertiesUtils.put("useBasicAuthenticationWithAccessCodeGrant", useBasicAuthenticationWithAccessCodeGrant, params);
		SpringDocPropertiesUtils.put("usePkceWithAuthorizationCodeGrant", usePkceWithAuthorizationCodeGrant, params);
		SpringDocPropertiesUtils.put("additionalQueryStringParams", additionalQueryStringParams, params);
		return params;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getScopeSeparator() {
		return scopeSeparator;
	}

	public void setScopeSeparator(String scopeSeparator) {
		this.scopeSeparator = scopeSeparator;
	}

	public Map<String, String> getAdditionalQueryStringParams() {
		return additionalQueryStringParams;
	}

	public void setAdditionalQueryStringParams(Map<String, String> additionalQueryStringParams) {
		this.additionalQueryStringParams = additionalQueryStringParams;
	}

	public String getUseBasicAuthenticationWithAccessCodeGrant() {
		return useBasicAuthenticationWithAccessCodeGrant;
	}

	public void setUseBasicAuthenticationWithAccessCodeGrant(String useBasicAuthenticationWithAccessCodeGrant) {
		this.useBasicAuthenticationWithAccessCodeGrant = useBasicAuthenticationWithAccessCodeGrant;
	}

	public Boolean getUsePkceWithAuthorizationCodeGrant() {
		return usePkceWithAuthorizationCodeGrant;
	}

	public void setUsePkceWithAuthorizationCodeGrant(Boolean usePkceWithAuthorizationCodeGrant) {
		this.usePkceWithAuthorizationCodeGrant = usePkceWithAuthorizationCodeGrant;
	}
}