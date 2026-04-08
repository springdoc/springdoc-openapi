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
import io.swagger.v3.oas.models.SpecVersion
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.providers.ObjectMapperProvider
import kotlin.reflect.full.memberProperties

/**
 * Marks schema properties as nullable for Kotlin data class fields whose
 * return type is marked nullable (`Type?`).
 *
 * Handles both OAS 3.0 and OAS 3.1 nullable semantics:
 * - **OAS 3.0**: Sets `nullable: true` on the property. For `$ref` properties,
 *   wraps in `allOf` since `$ref` and `nullable` are mutually exclusive.
 * - **OAS 3.1**: Adds `"null"` to the `type` array. For `$ref` properties,
 *   wraps in `oneOf` with a `type: "null"` alternative.
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

		val targetSchema = if (resolvedSchema != null && resolvedSchema.`$ref` != null) {
			context.getDefinedModels()[resolvedSchema.`$ref`.substring(Components.COMPONENTS_SCHEMAS_REF.length)]
		} else {
			resolvedSchema
		}

		if (targetSchema?.properties == null) return resolvedSchema

		val specVersion = targetSchema.specVersion ?: SpecVersion.V30

		val replacements = mutableMapOf<String, Schema<*>>()
		for (prop in kotlinClass.memberProperties) {
			if (!prop.returnType.isMarkedNullable) continue
			val fieldName = prop.name
			val property = targetSchema.properties[fieldName] ?: continue

			if (property.`$ref` != null) {
				replacements[fieldName] = wrapRefNullable(property.`$ref`, specVersion)
			} else {
				markNullable(property, specVersion)
			}
		}

		replacements.forEach { (name, wrapper) ->
			targetSchema.properties[name] = wrapper
		}

		return resolvedSchema
	}

	/**
	 * Marks a non-$ref property as nullable.
	 * - OAS 3.0: `nullable: true`
	 * - OAS 3.1: adds `"null"` to the `types` set
	 */
	private fun markNullable(property: Schema<*>, specVersion: SpecVersion) {
		if (specVersion == SpecVersion.V31) {
			val currentTypes = property.types ?: property.type?.let { setOf(it) } ?: emptySet()
			if ("null" !in currentTypes) {
				property.types = currentTypes + "null"
			}
		} else {
			property.nullable = true
		}
	}

	/**
	 * Wraps a $ref in a nullable composite schema.
	 * - OAS 3.0: `{ nullable: true, allOf: [{ $ref: "..." }] }`
	 * - OAS 3.1: `{ oneOf: [{ $ref: "..." }, { type: "null" }] }`
	 */
	private fun wrapRefNullable(ref: String, specVersion: SpecVersion): Schema<*> {
		val refSchema = Schema<Any>().apply { `$ref` = ref }
		return if (specVersion == SpecVersion.V31) {
			Schema<Any>().apply {
				oneOf = listOf(refSchema, Schema<Any>().apply { addType("null") })
			}
		} else {
			Schema<Any>().apply {
				nullable = true
				allOf = listOf(refSchema)
			}
		}
	}
}
