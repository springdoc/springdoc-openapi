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

package org.springdoc.kotlin;

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.swagger.v3.core.util.Json;
import kotlin.Deprecated;
import kotlin.coroutines.Continuation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
class SpringDocKotlinConfiguration {

	static {
		getConfig().addRequestWrapperToIgnore(Continuation.class)
				.addDeprecatedType(Deprecated.class);
		Json.mapper().registerModule(new KotlinModule());
	}

	@Bean
	@Lazy(false)
	@ConditionalOnMissingBean
	KotlinCoroutinesReturnTypeParser kotlinCoroutinesReturnTypeParser() {
		return new KotlinCoroutinesReturnTypeParser();
	}

}
