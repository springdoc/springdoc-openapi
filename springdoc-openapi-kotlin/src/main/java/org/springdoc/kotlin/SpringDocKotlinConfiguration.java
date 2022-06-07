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

package org.springdoc.kotlin;

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import kotlin.Deprecated;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Spring doc kotlin configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocKotlinConfiguration {

	/**
	 * Instantiates a new Spring doc kotlin configuration.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public SpringDocKotlinConfiguration(ObjectMapperProvider objectMapperProvider) {
		getConfig()
				.addRequestWrapperToIgnore(Continuation.class)
				.replaceWithSchema(byte[].class, new ByteArraySchema())
				.addDeprecatedType(Deprecated.class);
		objectMapperProvider.jsonMapper().registerModule( new KotlinModule.Builder().build());
	}

	/**
	 * Kotlin coroutines return type parser kotlin coroutines return type parser.
	 *
	 * @return the kotlin coroutines return type parser
	 */
	@Bean
	@Lazy(false)
	@ConditionalOnMissingBean
	KotlinCoroutinesReturnTypeParser kotlinCoroutinesReturnTypeParser() {
		return new KotlinCoroutinesReturnTypeParser();
	}

}
