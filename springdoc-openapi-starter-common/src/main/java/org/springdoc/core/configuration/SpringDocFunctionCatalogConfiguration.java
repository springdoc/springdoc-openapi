/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.configuration;

import java.util.Optional;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.CloudFunctionProvider;
import org.springdoc.core.providers.SpringCloudFunctionProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.web.function.FunctionEndpointInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * The type Spring doc function catalog configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.show-spring-cloud-functions:true}")
@ConditionalOnClass(FunctionEndpointInitializer.class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocFunctionCatalogConfiguration {

	/**
	 * Spring cloud function provider spring cloud function provider.
	 *
	 * @param functionCatalog           the function catalog
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the spring cloud function provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	CloudFunctionProvider springCloudFunctionProvider(Optional<FunctionCatalog> functionCatalog, SpringDocConfigProperties springDocConfigProperties) {
		return new SpringCloudFunctionProvider(functionCatalog, springDocConfigProperties);
	}
}