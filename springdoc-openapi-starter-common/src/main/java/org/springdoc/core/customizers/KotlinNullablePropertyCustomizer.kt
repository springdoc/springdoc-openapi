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

		val targetSchema = resolveTargetSchema(resolvedSchema, javaType, context)

		if (targetSchema?.properties == null) return resolvedSchema

		val specVersion = targetSchema.specVersion ?: SpecVersion.V30

		val replacements = mutableMapOf<String, Schema<*>>()
		for (prop in kotlinClass.memberProperties) {
			if (!prop.returnType.isMarkedNullable) continue
			val fieldName = prop.name
			val property = targetSchema.properties[fieldName] ?: continue

			if (property.`$ref` != null) {
				replacements[fieldName] = wrapRefNullable(property, specVersion)
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
	 * Resolves the schema that actually carries the model's properties.
	 *
	 * When a property is a `$ref`, the target lives in the defined models. When the
	 * resolved schema is a composed/polymorphic wrapper (for example an abstract Kotlin
	 * class or interface rendered with a discriminator), it has no direct `properties`, so
	 * the named model is looked up in the defined models by its resolved schema name. See
	 * https://github.com/springdoc/springdoc-openapi/issues/3304
	 */
	private fun resolveTargetSchema(
		resolvedSchema: Schema<*>?,
		javaType: JavaType,
		context: ModelConverterContext
	): Schema<*>? {
		if (resolvedSchema?.`$ref` != null) {
			return context.definedModels[resolvedSchema.`$ref`.substring(Components.COMPONENTS_SCHEMAS_REF.length)]
		}
		if (resolvedSchema?.properties != null) {
			return resolvedSchema
		}
		val definedModels = context.definedModels
		return definedModels[javaType.rawClass.name] ?: definedModels[javaType.rawClass.simpleName]
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
	 * Wraps a $ref property in a nullable composite schema. A fresh wrapper schema is returned
	 * (the original property object is left untouched) with any sibling attributes such as
	 * `description`, `title`, `example` copied over, so they are not lost when the bare `$ref`
	 * is moved into the composite.
	 * - OAS 3.0: `{ nullable: true, allOf: [{ $ref: "..." }] }`
	 * - OAS 3.1: `{ oneOf: [{ $ref: "..." }, { type: "null" }] }`
	 */
	private fun wrapRefNullable(property: Schema<*>, specVersion: SpecVersion): Schema<*> {
		val refSchema = Schema<Any>().apply { `$ref` = property.`$ref` }
		val wrapper = Schema<Any>()
		copySiblingMetadata(property, wrapper)
		if (specVersion == SpecVersion.V31) {
			wrapper.oneOf = listOf(refSchema, Schema<Any>().apply { addType("null") })
		} else {
			wrapper.nullable = true
			wrapper.allOf = listOf(refSchema)
		}
		return wrapper
	}

	/**
	 * Copies the attributes swagger-core may set as siblings of a `$ref` property onto the
	 * nullable wrapper, so annotations like `@Schema(description = ...)` survive.
	 */
	private fun copySiblingMetadata(source: Schema<*>, target: Schema<*>) {
		source.description?.let { target.description = it }
		source.title?.let { target.title = it }
		source.deprecated?.let { target.deprecated = it }
		if (source.exampleSetFlag) target.example = source.example
		source.externalDocs?.let { target.externalDocs = it }
		source.readOnly?.let { target.readOnly = it }
		source.writeOnly?.let { target.writeOnly = it }
		source.extensions?.let { target.extensions = it }
	}
}
