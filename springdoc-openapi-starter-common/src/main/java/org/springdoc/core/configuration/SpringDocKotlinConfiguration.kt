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

package org.springdoc.core.configuration

import org.springdoc.core.converters.KotlinInlineClassUnwrappingConverter
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer
import org.springdoc.core.customizers.KotlinDeprecatedPropertyCustomizer
import org.springdoc.core.extractor.DelegatingMethodParameter
import org.springdoc.core.providers.ObjectMapperProvider
import org.springdoc.core.utils.Constants
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import kotlin.coroutines.Continuation
import kotlin.reflect.full.primaryConstructor

/**
 * The type Spring doc kotlin configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = [Constants.SPRINGDOC_ENABLED], matchIfMissing = true)
@ConditionalOnExpression("\${springdoc.api-docs.enabled:true} and \${springdoc.enable-kotlin:true}")
@ConditionalOnClass(Continuation::class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration::class)
class SpringDocKotlinConfiguration() {

	/**
	 * Instantiates a new Spring doc kotlin configuration.
	 *
	 */
	init {
		SpringDocUtils.getConfig()
			.addResponseTypeToIgnore(Unit::class.java)
			.addRequestWrapperToIgnore(Continuation::class.java)
			.addDeprecatedType(Deprecated::class.java)
	}

	@ConditionalOnClass(name = ["kotlin.reflect.full.KClasses"])
	class KotlinReflectDependingConfiguration {

		@Bean
		@Lazy(false)
		@ConditionalOnMissingBean
		fun kotlinDeprecatedPropertyCustomizer(objectMapperProvider: ObjectMapperProvider): KotlinDeprecatedPropertyCustomizer {
			return KotlinDeprecatedPropertyCustomizer(objectMapperProvider)
		}

		@Bean
		@Lazy(false)
		@ConditionalOnMissingBean
		fun kotlinModelConverter(objectMapperProvider: ObjectMapperProvider): KotlinInlineClassUnwrappingConverter {
			return KotlinInlineClassUnwrappingConverter(objectMapperProvider)
		}

		@Bean
		@ConditionalOnProperty(
			name = [Constants.SPRINGDOC_NULLABLE_REQUEST_PARAMETER_ENABLED],
			matchIfMissing = true
		)
		@Lazy(false)
		fun kotlinDefaultsInParamObjects(): DelegatingMethodParameterCustomizer =
			DelegatingMethodParameterCustomizer { _, mp ->
				val kProp = mp.containingClass.kotlin.primaryConstructor
					?.parameters
					?.firstOrNull { it.name == mp.parameterName }
				if (kProp?.isOptional == true)
					(mp as DelegatingMethodParameter).isNotRequired = true
			}
	}

}
