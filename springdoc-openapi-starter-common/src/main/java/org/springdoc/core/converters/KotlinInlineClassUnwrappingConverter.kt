/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springdoc.core.converters

import com.fasterxml.jackson.databind.JavaType
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.providers.ObjectMapperProvider
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class KotlinInlineClassUnwrappingConverter(
	private val objectMapperProvider: ObjectMapperProvider
) : ModelConverter {

	override fun resolve(
		type: AnnotatedType?,
		context: ModelConverterContext?,
		chain: Iterator<ModelConverter>
	): Schema<*>? {
		if (type?.type == null || context == null || !chain.hasNext()) {
			return null
		}
		val javaType: JavaType =
			objectMapperProvider.jsonMapper().constructType(type.type)
		val kClass = javaType.rawClass.kotlin
		if (kClass.findAnnotation<JvmInline>() != null) {
			val constructor = kClass.primaryConstructor
			val param = constructor?.parameters?.firstOrNull()
			val unwrappedClass = param?.type?.jvmErasure?.java
			if (unwrappedClass != null) {
				val unwrappedType = AnnotatedType()
					.type(unwrappedClass)
					.ctxAnnotations(type.ctxAnnotations)
					.jsonViewAnnotation(type.jsonViewAnnotation)
					.resolveAsRef(false)

				return chain.next().resolve(unwrappedType, context, chain)
			}
		}
		return chain.next().resolve(type, context, chain)
	}
}
