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

package org.springdoc.core.customizers

import com.fasterxml.jackson.databind.JavaType
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.providers.ObjectMapperProvider
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Kotlin Deprecated PropertyCustomizer to handle Kotlin Deprecated annotations.
 * @author bnasslahsen
 */
class KotlinDeprecatedPropertyCustomizer(
	private val objectMapperProvider: ObjectMapperProvider
) : ModelConverter {

	override fun resolve(
		type: AnnotatedType,
		context: ModelConverterContext,
		chain: Iterator<ModelConverter>
	): Schema<*>? {
		if (!chain.hasNext()) return null
		// Resolve the next model in the chain
		val resolvedSchema = chain.next().resolve(type, context, chain)

		val javaType: JavaType =
			objectMapperProvider.jsonMapper().constructType(type.type)
		val kotlinClass = javaType.rawClass.kotlin

		// Check each property of the class
		for (prop in kotlinClass.memberProperties) {
			val deprecatedAnnotation = prop.findAnnotation<Deprecated>()
			prop.hasAnnotation<Deprecated>()
			if (deprecatedAnnotation != null) {
				val fieldName = prop.name
				if (resolvedSchema.`$ref` != null) {
					val schema =
						context.getDefinedModels()[resolvedSchema.`$ref`.substring(
							Components.COMPONENTS_SCHEMAS_REF.length
						)]
					schema?.properties?.get(fieldName)?.deprecated = true
					if (deprecatedAnnotation.message.isNotBlank()) {
						schema?.properties?.get(fieldName)?.description =
							schema?.properties?.get(fieldName)?.description?.takeIf { it.isNotBlank() }
								?: deprecatedAnnotation.message
					}
				}
			}
		}
		return resolvedSchema
	}
}
