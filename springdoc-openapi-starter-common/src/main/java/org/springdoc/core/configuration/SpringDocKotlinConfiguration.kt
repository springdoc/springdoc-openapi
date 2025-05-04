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

import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.converters.KotlinInlineClassUnwrappingConverter
import org.springdoc.core.customizers.KotlinDeprecatedPropertyCustomizer
import org.springdoc.core.customizers.ParameterCustomizer
import org.springdoc.core.providers.ObjectMapperProvider
import org.springdoc.core.utils.Constants
import org.springdoc.core.utils.SchemaUtils
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
import org.springframework.core.KotlinDetector
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ValueConstants
import kotlin.coroutines.Continuation
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

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

	/**
	 * Kotlin springdoc-openapi ParameterCustomizer.
	 * deprecated as not anymore required, use [SchemaUtils.fieldNullable]
	 *
	 * @return the nullable Kotlin Request Parameter Customizer
	 * @see SchemaUtils.fieldNullable()
	 */
	@Deprecated("Deprecated since 2.8.7", level = DeprecationLevel.ERROR)
	fun nullableKotlinRequestParameterCustomizer(): ParameterCustomizer {
		return ParameterCustomizer { parameterModel, methodParameter ->
			if (parameterModel == null) return@ParameterCustomizer null
			if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(methodParameter.parameterType)) {
				val kParameter = methodParameter.toKParameter()
				if (kParameter != null) {
					val parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
						AnnotatedElementUtils.forAnnotations(*methodParameter.parameterAnnotations),
						Parameter::class.java
					)
					val requestParam = AnnotatedElementUtils.findMergedAnnotation(
						AnnotatedElementUtils.forAnnotations(*methodParameter.parameterAnnotations),
						RequestParam::class.java
					)
					// Swagger @Parameter annotation takes precedence
					if (parameterDoc != null && parameterDoc.required)
						parameterModel.required = parameterDoc.required
					// parameter is not required if a default value is provided in @RequestParam
					else if (requestParam != null && requestParam.defaultValue != ValueConstants.DEFAULT_NONE)
						parameterModel.required = false
					else{
						val isJavaNullableAnnotationPresent = methodParameter.parameterAnnotations.any {
							it.annotationClass.qualifiedName == "jakarta.annotation.Nullable"
						}
						parameterModel.required =
							kParameter.type.isMarkedNullable == false && !isJavaNullableAnnotationPresent
					}
				}
			}
			return@ParameterCustomizer parameterModel
		}
	}

	private fun MethodParameter.toKParameter(): KParameter? {
		// ignore return type, see org.springframework.core.MethodParameter.getParameterIndex
		if (parameterIndex == -1) return null
		val kotlinFunction = method?.kotlinFunction ?: return null
		// The first parameter of the kotlin function is the "this" reference and not needed here.
		// See also kotlin.reflect.KCallable.getParameters
		return kotlinFunction.parameters[parameterIndex + 1]
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
	}

}
