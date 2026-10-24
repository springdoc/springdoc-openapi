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

package org.springdoc.core.customizers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

/**
 * Base model converter marking the schema properties of a model as nullable, for the
 * properties a subclass identifies as nullable.
 * <p>
 * Handles both OAS 3.0 and OAS 3.1 nullable semantics:
 * <ul>
 *     <li><b>OAS 3.0</b>: Sets {@code nullable: true} on the property. For {@code $ref} properties,
 *     wraps in {@code allOf} since {@code $ref} and {@code nullable} are mutually exclusive.</li>
 *     <li><b>OAS 3.1</b>: Adds {@code "null"} to the {@code type} array, or a {@code type: "null"}
 *     alternative to a {@code oneOf}. For {@code $ref} properties, wraps in {@code oneOf} with a
 *     {@code type: "null"} alternative.</li>
 * </ul>
 *
 * @author Jeffrey Blayney
 * @author Mattias-Sehlstedt
 * @author bnasslahsen
 */
public abstract class AbstractNullablePropertyCustomizer implements ModelConverter {

	/**
	 * The constant NULL_TYPE.
	 */
	private static final String NULL_TYPE = "null";

	/**
	 * The Object mapper provider.
	 */
	private final ObjectMapperProvider objectMapperProvider;

	/**
	 * Instantiates a new Abstract nullable property customizer.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	protected AbstractNullablePropertyCustomizer(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (!chain.hasNext())
			return null;
		Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);

		JavaType javaType = objectMapperProvider.jsonMapper().constructType(type.getType());
		Class<?> rawClass = javaType.getRawClass();
		if (rawClass == null || rawClass.isPrimitive() || rawClass.isArray()
				|| rawClass.getPackageName().startsWith("java."))
			return resolvedSchema;

		Schema<?> targetSchema = resolveTargetSchema(resolvedSchema, javaType, context);
		if (targetSchema == null || targetSchema.getProperties() == null)
			return resolvedSchema;

		Set<String> nullableProperties = findNullableProperties(javaType);
		if (nullableProperties.isEmpty())
			return resolvedSchema;

		SpecVersion specVersion = targetSchema.getSpecVersion() != null ? targetSchema.getSpecVersion() : SpecVersion.V30;
		Map<String, Schema> properties = targetSchema.getProperties();
		Map<String, Schema<?>> replacements = new LinkedHashMap<>();
		for (String propertyName : nullableProperties) {
			Schema<?> property = properties.get(propertyName);
			if (property == null)
				continue;
			if (property.get$ref() != null)
				replacements.put(propertyName, wrapRefNullable(property, specVersion));
			else
				markNullable(property, specVersion);
		}
		replacements.forEach(properties::put);

		return resolvedSchema;
	}

	/**
	 * Finds the names of the properties of the model that are nullable.
	 *
	 * @param javaType the java type of the model
	 * @return the names of the nullable properties, possibly empty
	 */
	protected abstract Set<String> findNullableProperties(JavaType javaType);

	/**
	 * Gets object mapper provider.
	 *
	 * @return the object mapper provider
	 */
	protected ObjectMapperProvider getObjectMapperProvider() {
		return objectMapperProvider;
	}

	/**
	 * Resolves the schema that actually carries the model's properties.
	 * <p>
	 * When a property is a {@code $ref}, the target lives in the defined models. When the
	 * resolved schema is a composed/polymorphic wrapper (for example an abstract class or
	 * interface rendered with a discriminator), it has no direct {@code properties}, so the
	 * named model is looked up in the defined models by its resolved schema name. See
	 * https://github.com/springdoc/springdoc-openapi/issues/3304
	 *
	 * @param resolvedSchema the resolved schema
	 * @param javaType       the java type
	 * @param context        the context
	 * @return the target schema
	 */
	private Schema<?> resolveTargetSchema(Schema<?> resolvedSchema, JavaType javaType, ModelConverterContext context) {
		Map<String, Schema> definedModels = context.getDefinedModels();
		if (resolvedSchema != null && resolvedSchema.get$ref() != null)
			return definedModels.get(resolvedSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length()));
		if (resolvedSchema != null && resolvedSchema.getProperties() != null)
			return resolvedSchema;
		Schema<?> schema = definedModels.get(javaType.getRawClass().getName());
		return schema != null ? schema : definedModels.get(javaType.getRawClass().getSimpleName());
	}

	/**
	 * Marks a non-$ref property as nullable.
	 * <ul>
	 *     <li>OAS 3.0: {@code nullable: true}</li>
	 *     <li>OAS 3.1: adds {@code "null"} to the {@code types} set, or, for a {@code oneOf} schema,
	 *     appends a {@code type: "null"} alternative</li>
	 * </ul>
	 * A schema without any type constraint, such as one representing {@code Object} or
	 * {@code Any?}, already permits any value including {@code null}: it is left untouched
	 * rather than being narrowed to {@code type: "null"} (3.1) or decorated with a redundant
	 * {@code nullable: true} (3.0).
	 *
	 * @param property    the property
	 * @param specVersion the spec version
	 */
	private void markNullable(Schema<?> property, SpecVersion specVersion) {
		if (isAnySchema(property))
			return;
		if (specVersion == SpecVersion.V31) {
			if (property.getOneOf() != null && !property.getOneOf().isEmpty()) {
				boolean hasNullBranch = property.getOneOf().stream().anyMatch(schema -> NULL_TYPE.equals(schema.getType())
						|| (schema.getTypes() != null && schema.getTypes().contains(NULL_TYPE)));
				if (!hasNullBranch) {
					List<Schema> oneOf = new ArrayList<>(property.getOneOf());
					oneOf.add(nullSchema());
					property.setOneOf(oneOf);
				}
				return;
			}
			Set<String> currentTypes = new LinkedHashSet<>();
			if (property.getTypes() != null)
				currentTypes.addAll(property.getTypes());
			else if (property.getType() != null)
				currentTypes.add(property.getType());
			if (!currentTypes.contains(NULL_TYPE)) {
				currentTypes.add(NULL_TYPE);
				property.setTypes(currentTypes);
			}
		}
		else
			property.setNullable(true);
	}

	/**
	 * Returns true when the schema imposes no type constraint, in which case it already allows
	 * any value, including {@code null}.
	 *
	 * @param property the property
	 * @return the boolean
	 */
	private boolean isAnySchema(Schema<?> property) {
		return property.get$ref() == null
				&& property.getType() == null
				&& (property.getTypes() == null || property.getTypes().isEmpty())
				&& (property.getOneOf() == null || property.getOneOf().isEmpty());
	}

	/**
	 * Wraps a $ref property in a nullable composite schema. A fresh wrapper schema is returned
	 * (the original property object is left untouched) with any sibling attributes such as
	 * {@code description}, {@code title}, {@code example} copied over, so they are not lost when
	 * the bare {@code $ref} is moved into the composite.
	 * <ul>
	 *     <li>OAS 3.0: {@code { nullable: true, allOf: [{ $ref: "..." }] }}</li>
	 *     <li>OAS 3.1: {@code { oneOf: [{ $ref: "..." }, { type: "null" }] }}</li>
	 * </ul>
	 *
	 * @param property    the property
	 * @param specVersion the spec version
	 * @return the wrapper schema
	 */
	private Schema<?> wrapRefNullable(Schema<?> property, SpecVersion specVersion) {
		Schema<Object> refSchema = new Schema<>();
		refSchema.set$ref(property.get$ref());
		Schema<Object> wrapper = new Schema<>();
		copySiblingMetadata(property, wrapper);
		if (specVersion == SpecVersion.V31)
			wrapper.setOneOf(List.of(refSchema, nullSchema()));
		else {
			wrapper.setNullable(true);
			wrapper.setAllOf(List.of(refSchema));
		}
		return wrapper;
	}

	/**
	 * Builds a schema only allowing {@code null}.
	 *
	 * @return the null schema
	 */
	private Schema<Object> nullSchema() {
		Schema<Object> nullSchema = new Schema<>();
		nullSchema.addType(NULL_TYPE);
		return nullSchema;
	}

	/**
	 * Copies the attributes swagger-core may set as siblings of a $ref property onto the
	 * nullable wrapper, so annotations like {@code @Schema(description = ...)} survive.
	 *
	 * @param source the source
	 * @param target the target
	 */
	private void copySiblingMetadata(Schema<?> source, Schema<Object> target) {
		if (source.getDescription() != null)
			target.setDescription(source.getDescription());
		if (source.getTitle() != null)
			target.setTitle(source.getTitle());
		if (source.getDeprecated() != null)
			target.setDeprecated(source.getDeprecated());
		if (source.getExampleSetFlag())
			target.setExample(source.getExample());
		if (source.getExternalDocs() != null)
			target.setExternalDocs(source.getExternalDocs());
		if (source.getReadOnly() != null)
			target.setReadOnly(source.getReadOnly());
		if (source.getWriteOnly() != null)
			target.setWriteOnly(source.getWriteOnly());
		if (source.getExtensions() != null)
			target.setExtensions(source.getExtensions());
	}

}
