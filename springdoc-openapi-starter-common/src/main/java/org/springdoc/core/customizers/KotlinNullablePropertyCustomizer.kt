/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
import kotlin.reflect.full.memberProperties

/**
 * Sets `nullable: true` on schema properties for Kotlin data class fields
 * whose return type is marked nullable (`Type?`).
 *
 * Springdoc already uses Kotlin nullability to determine the `required` list
 * (via [org.springdoc.core.utils.SchemaUtils.fieldRequired]), but does not set
 * `nullable: true` on the property schema itself. This causes OpenAPI client
 * generators (e.g., fabrikt) to produce non-null types with null defaults,
 * which fails compilation in Kotlin.
 *
 * See: https://github.com/springdoc/springdoc-openapi/issues/906
 *
 * @author Jeffrey Blayney
 */
class KotlinNullablePropertyCustomizer(
	private val objectMapperProvider: ObjectMapperProvider
) : ModelConverter {

	override fun resolve(
		type: AnnotatedType,
		context: ModelConverterContext,
		chain: Iterator<ModelConverter>
	): Schema<*>? {
		if (!chain.hasNext()) return null
		val resolvedSchema = chain.next().resolve(type, context, chain)

		val javaType: JavaType =
			objectMapperProvider.jsonMapper().constructType(type.type)
		if (javaType.rawClass.packageName.startsWith("java.")) {
			return resolvedSchema
		}

		val kotlinClass = try {
			javaType.rawClass.kotlin
		} catch (_: Throwable) {
			return resolvedSchema
		}

		for (prop in kotlinClass.memberProperties) {
			if (prop.returnType.isMarkedNullable) {
				val fieldName = prop.name
				if (resolvedSchema != null && resolvedSchema.`$ref` != null) {
					val schema =
						context.getDefinedModels()[resolvedSchema.`$ref`.substring(
							Components.COMPONENTS_SCHEMAS_REF.length
						)]
					schema?.properties?.get(fieldName)?.nullable = true
				} else {
					resolvedSchema?.properties?.get(fieldName)?.nullable = true
				}
			}
		}
		return resolvedSchema
	}
}
