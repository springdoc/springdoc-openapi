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

package org.springdoc.core.customizers

import com.fasterxml.jackson.databind.JavaType
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.providers.ObjectMapperProvider
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * Adds the properties that `kotlinx.serialization` requires to the `required` list of the
 * schemas of `@Serializable` classes, as `jackson-module-kotlin` does for the other Kotlin
 * classes: a primary constructor property is required unless it has a default value, or
 * when it is annotated with `@Required`. `@Transient` properties are never required.
 *
 * It is only registered when `jackson-module-kotlin` is absent, and it never removes a
 * required property, nor adds one marked `@Schema(requiredMode = NOT_REQUIRED)`.
 *
 * See: https://github.com/springdoc/springdoc-openapi/issues/3433
 *
 * @author bnasslahsen
 */
class KotlinxSerializationRequiredPropertyCustomizer(
	private val objectMapperProvider: ObjectMapperProvider
) : ModelConverter {

	override fun resolve(type: AnnotatedType, context: ModelConverterContext, chain: Iterator<ModelConverter>): Schema<*>? {
		if (!chain.hasNext())
			return null
		val resolvedSchema = chain.next().resolve(type, context, chain)
		val javaType = try {
			objectMapperProvider.jsonMapper().constructType(type.type)
		} catch (_: Exception) {
			return resolvedSchema
		}
		val rawClass = javaType.rawClass
		if (rawClass == null || rawClass.annotations.none { it.annotationClass.java.name == SERIALIZABLE })
			return resolvedSchema
		val targetSchema = resolveTargetSchema(resolvedSchema, context) ?: return resolvedSchema
		val properties = targetSchema.properties ?: return resolvedSchema
		for (propertyName in findRequiredProperties(javaType)) {
			if (properties.containsKey(propertyName) && targetSchema.required?.contains(propertyName) != true)
				targetSchema.addRequiredItem(propertyName)
		}
		return resolvedSchema
	}

	/**
	 * Finds the JSON names of the properties that `kotlinx.serialization` requires.
	 *
	 * @param javaType the java type of the model
	 * @return the names of the required properties, possibly empty
	 */
	private fun findRequiredProperties(javaType: JavaType): Set<String> {
		val requiredKotlinNames = try {
			val kotlinClass = javaType.rawClass.kotlin
			val constructor = kotlinClass.primaryConstructor ?: return emptySet()
			val propertiesByName = kotlinClass.memberProperties.associateBy { it.name }
			constructor.parameters.mapNotNullTo(LinkedHashSet()) { parameter ->
				val property = parameter.name?.let { propertiesByName[it] } ?: return@mapNotNullTo null
				val annotatedElements = listOf<KAnnotatedElement>(parameter, property)
				val annotations = annotatedElements.flatMap { it.annotations } +
						listOfNotNull(property.javaField, property.javaGetter).flatMap { it.annotations.asList() }
				when {
					annotations.any { it.annotationClass.java.name == TRANSIENT } -> null
					annotations.any { it is io.swagger.v3.oas.annotations.media.Schema && it.requiredMode == io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED } -> null
					!parameter.isOptional || annotations.any { it.annotationClass.java.name == REQUIRED } -> property.name
					else -> null
				}
			}
		} catch (_: Throwable) {
			return emptySet()
		}
		if (requiredKotlinNames.isEmpty())
			return requiredKotlinNames
		// The schema properties are keyed by their JSON names, which differ from the Kotlin
		// names when renamed with @JsonProperty or @JsonNaming: map them through Jackson.
		val definitions = try {
			objectMapperProvider.jsonMapper().serializationConfig.introspect(javaType).findProperties()
		} catch (_: Exception) {
			return requiredKotlinNames
		}
		val result = LinkedHashSet<String>()
		val mappedKotlinNames = HashSet<String>()
		for (definition in definitions) {
			if (definition.internalName in requiredKotlinNames) {
				result.add(definition.name)
				mappedKotlinNames.add(definition.internalName)
			}
		}
		requiredKotlinNames.filterNotTo(result) { it in mappedKotlinNames }
		return result
	}

	/**
	 * Resolves the schema that carries the model's properties: the defined model of a
	 * `$ref`, or the resolved schema itself.
	 *
	 * @param resolvedSchema the resolved schema
	 * @param context the context
	 * @return the target schema
	 */
	private fun resolveTargetSchema(resolvedSchema: Schema<*>?, context: ModelConverterContext): Schema<*>? {
		if (resolvedSchema == null)
			return null
		val ref = resolvedSchema.`$ref`
		if (ref != null)
			return context.definedModels[ref.removePrefix(Components.COMPONENTS_SCHEMAS_REF)]
		return if (resolvedSchema.properties != null) resolvedSchema else null
	}

	private companion object {

		/**
		 * The kotlinx.serialization Serializable annotation.
		 */
		const val SERIALIZABLE = "kotlinx.serialization.Serializable"

		/**
		 * The kotlinx.serialization Required annotation.
		 */
		const val REQUIRED = "kotlinx.serialization.Required"

		/**
		 * The kotlinx.serialization Transient annotation.
		 */
		const val TRANSIENT = "kotlinx.serialization.Transient"
	}
}
