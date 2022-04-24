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

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.core.providers.CloudFunctionProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.RepositoryRestResourceProvider;
import org.springdoc.core.providers.RouterFunctionProvider;
import org.springdoc.core.providers.SecurityOAuth2Provider;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.providers.WebConversionServiceProvider;

/**
 * The type Spring doc providers.
 * @author bnasslahsen
 */
public class SpringDocProviders {

	/**
	 * The Actuator provider.
	 */
	private final Optional<ActuatorProvider> actuatorProvider;

	/**
	 * The Spring cloud function provider.
	 */
	private final Optional<CloudFunctionProvider> springCloudFunctionProvider;

	/**
	 * The Spring security o auth 2 provider.
	 */
	private final Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider;

	/**
	 * The Repository rest resource provider.
	 */
	private final Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider;

	/**
	 * The Router function provider.
	 */
	private final Optional<RouterFunctionProvider> routerFunctionProvider;

	/**
	 * The Spring web provider.
	 */
	private final Optional<SpringWebProvider> springWebProvider;

	/**
	 * The Web conversion service.
	 */
	private final Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider;

	/**
	 * The Object mapper provider.
	 */
	private final ObjectMapperProvider objectMapperProvider;

	/**
	 * Instantiates a new Spring doc providers.
	 * @param actuatorProvider the actuator provider
	 * @param springCloudFunctionProvider the spring cloud function provider
	 * @param springSecurityOAuth2Provider the spring security o auth 2 provider
	 * @param repositoryRestResourceProvider the repository rest resource provider
	 * @param routerFunctionProvider the router function provider
	 * @param springWebProvider the spring web provider
	 * @param optionalWebConversionServiceProvider the optional web conversion service provider
	 * @param objectMapperProvider the object mapper provider
	 */
	public SpringDocProviders(Optional<ActuatorProvider> actuatorProvider, Optional<CloudFunctionProvider> springCloudFunctionProvider,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider, Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider,
			Optional<RouterFunctionProvider> routerFunctionProvider, Optional<SpringWebProvider> springWebProvider,
			Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider,
			ObjectMapperProvider objectMapperProvider) {
		this.actuatorProvider = actuatorProvider;
		this.springCloudFunctionProvider = springCloudFunctionProvider;
		this.springSecurityOAuth2Provider = springSecurityOAuth2Provider;
		this.repositoryRestResourceProvider = repositoryRestResourceProvider;
		this.routerFunctionProvider = routerFunctionProvider;
		this.springWebProvider = springWebProvider;
		this.optionalWebConversionServiceProvider = optionalWebConversionServiceProvider;
		this.objectMapperProvider = objectMapperProvider;
	}

	/**
	 * Gets actuator provider.
	 *
	 * @return the actuator provider
	 */
	public Optional<ActuatorProvider> getActuatorProvider() {
		return actuatorProvider;
	}

	/**
	 * Gets spring cloud function provider.
	 *
	 * @return the spring cloud function provider
	 */
	public Optional<CloudFunctionProvider> getSpringCloudFunctionProvider() {
		return springCloudFunctionProvider;
	}

	/**
	 * Gets spring security o auth 2 provider.
	 *
	 * @return the spring security o auth 2 provider
	 */
	public Optional<SecurityOAuth2Provider> getSpringSecurityOAuth2Provider() {
		return springSecurityOAuth2Provider;
	}

	/**
	 * Gets repository rest resource provider.
	 *
	 * @return the repository rest resource provider
	 */
	public Optional<RepositoryRestResourceProvider> getRepositoryRestResourceProvider() {
		return repositoryRestResourceProvider;
	}

	/**
	 * Gets router function provider.
	 *
	 * @return the router function provider
	 */
	public Optional<RouterFunctionProvider> getRouterFunctionProvider() {
		return routerFunctionProvider;
	}

	/**
	 * Gets spring web provider.
	 *
	 * @return the spring web provider
	 */
	public Optional<SpringWebProvider> getSpringWebProvider() {
		return springWebProvider;
	}

	/**
	 * Gets optional web conversion service provider.
	 *
	 * @return the optional web conversion service provider
	 */
	public Optional<WebConversionServiceProvider> getOptionalWebConversionServiceProvider() {
		return optionalWebConversionServiceProvider;
	}

	/**
	 * Json mapper object mapper.
	 *
	 * @return the object mapper provider
	 */
	public ObjectMapper jsonMapper() {
		return objectMapperProvider.jsonMapper();
	}

	/**
	 * Yaml mapper object mapper.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper yamlMapper() {
		return objectMapperProvider.yamlMapper();
	}
}
