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

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * The type Spring doc kotlin module configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KotlinModule.class)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-kotlin:true}")
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocJacksonKotlinModuleConfiguration {

	/**
	 * Instantiates a new objectMapperProvider with a kotlin module.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the object mapper provider
	 */
	@Bean
	@Primary
	ObjectMapperProvider springdocKotlinObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {
		ObjectMapperProvider mapperProvider =  new ObjectMapperProvider(springDocConfigProperties);
		mapperProvider.jsonMapper().registerModule(new KotlinModule.Builder().build());
		return mapperProvider;
	}
}
