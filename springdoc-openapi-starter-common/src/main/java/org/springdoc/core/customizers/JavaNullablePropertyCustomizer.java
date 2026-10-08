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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.utils.SchemaUtils.ANNOTATIONS_FOR_NULLABLE;

/**
 * Marks schema properties as nullable for Java class properties that are explicitly
 * annotated with a {@code @Nullable} annotation (e.g. JSpecify, Spring, JSR-305), either on
 * the field, its getter / record accessor, or its constructor parameter. Both declaration
 * and type-use annotations are supported.
 * <p>
 * Handles both OAS 3.0 and OAS 3.1 nullable semantics:
 * <ul>
 *     <li><b>OAS 3.0</b>: Sets {@code nullable: true} on the property. For {@code $ref} properties,
 *     wraps in {@code allOf} since {@code $ref} and {@code nullable} are mutually exclusive.</li>
 *     <li><b>OAS 3.1</b>: Adds {@code "null"} to the {@code type} array. For {@code $ref} properties,
 *     wraps in {@code oneOf} with a {@code type: "null"} alternative.</li>
 * </ul>
 *
 * @author Mattias-Sehlstedt
 */
public class JavaNullablePropertyCustomizer implements ModelConverter {

	/**
	 * The constant NULL_TYPE.
	 */
	private static final String NULL_TYPE = "null";

	/**
	 * The Object mapper provider.
	 */
	private final ObjectMapperProvider objectMapperProvider;

	/**
	 * Instantiates a new Java nullable property customizer.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public JavaNullablePropertyCustomizer(ObjectMapperProvider objectMapperProvider) {
		this.objectMapperProvider = objectMapperProvider;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (!chain.hasNext())
			return null;
		Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);

		ObjectMapper mapper = objectMapperProvider.jsonMapper();
		JavaType javaType = mapper.constructType(type.getType());
		Class<?> rawClass = javaType.getRawClass();
		if (rawClass == null || rawClass.isPrimitive() || rawClass.isArray()
				|| rawClass.getPackageName().startsWith("java."))
			return resolvedSchema;

		Schema<?> targetSchema = resolveTargetSchema(resolvedSchema, javaType, context);
		if (targetSchema == null || targetSchema.getProperties() == null)
			return resolvedSchema;

		Set<String> nullableProperties = findNullableProperties(mapper, javaType);
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
	 * Resolves the schema that actually carries the model's properties.
	 * When the resolved schema is a {@code $ref}, the target lives in the defined models. When it is
	 * a composed/polymorphic wrapper without direct properties, the named model is looked up by name.
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
	 * Finds the serialized names of the properties explicitly annotated as nullable.
	 *
	 * @param mapper   the mapper
	 * @param javaType the java type
	 * @return the set of nullable property names
	 */
	private Set<String> findNullableProperties(ObjectMapper mapper, JavaType javaType) {
		List<BeanPropertyDefinition> definitions;
		try {
			BeanDescription beanDescription = mapper.getSerializationConfig().introspect(javaType);
			definitions = beanDescription.findProperties();
		}
		catch (Exception e) {
			return Collections.emptySet();
		}
		Set<String> result = new LinkedHashSet<>();
		for (BeanPropertyDefinition definition : definitions) {
			if (isNullable(definition))
				result.add(definition.getName());
		}
		return result;
	}

	/**
	 * Whether the given property is explicitly annotated as nullable.
	 *
	 * @param definition the property definition
	 * @return the boolean
	 */
	private boolean isNullable(BeanPropertyDefinition definition) {
		AnnotatedField annotatedField = definition.getField();
		if (annotatedField != null) {
			Field field = annotatedField.getAnnotated();
			if (field.getType().isPrimitive())
				return false;
			if (hasNullableAnnotation(field) || hasNullableAnnotation(field.getAnnotatedType()))
				return true;
		}
		AnnotatedMethod getter = definition.getGetter();
		if (getter != null) {
			Method method = getter.getAnnotated();
			if (method.getReturnType().isPrimitive())
				return false;
			if (hasNullableAnnotation(method) || hasNullableAnnotation(method.getAnnotatedReturnType()))
				return true;
		}
		AnnotatedParameter ctorParameter = definition.getConstructorParameter();
		if (ctorParameter != null) {
			Parameter parameter = toReflectParameter(ctorParameter);
			return parameter != null && !parameter.getType().isPrimitive()
					&& (hasNullableAnnotation(parameter) || hasNullableAnnotation(parameter.getAnnotatedType()));
		}
		return false;
	}

	/**
	 * Converts a Jackson annotated parameter to a reflection parameter.
	 *
	 * @param annotatedParameter the annotated parameter
	 * @return the parameter or {@code null}
	 */
	private Parameter toReflectParameter(AnnotatedParameter annotatedParameter) {
		try {
			AnnotatedElement owner = annotatedParameter.getOwner().getAnnotated();
			if (owner instanceof Executable executable) {
				Parameter[] parameters = executable.getParameters();
				int index = annotatedParameter.getIndex();
				if (index >= 0 && index < parameters.length)
					return parameters[index];
			}
		}
		catch (Exception ignored) {
			// best-effort only
		}
		return null;
	}

	/**
	 * Whether the element carries a {@code @Nullable} annotation.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	private boolean hasNullableAnnotation(AnnotatedElement element) {
		if (element == null)
			return false;
		for (Annotation annotation : element.getAnnotations()) {
			if (ANNOTATIONS_FOR_NULLABLE.contains(annotation.annotationType().getSimpleName()))
				return true;
		}
		return false;
	}

	/**
	 * Marks a non-$ref property as nullable.
	 * - OAS 3.0: {@code nullable: true}
	 * - OAS 3.1: adds {@code "null"} to the {@code types} set, or, for {@code oneOf} schemas,
	 *   appends a {@code { type: "null" }} alternative
	 * <p>
	 * A schema without any type constraint already permits {@code null} and is left untouched.
	 *
	 * @param property    the property
	 * @param specVersion the spec version
	 */
	private void markNullable(Schema<?> property, SpecVersion specVersion) {
		boolean hasOneOf = property.getOneOf() != null && !property.getOneOf().isEmpty();
		if (!hasOneOf && isAnySchema(property))
			return;
		if (specVersion == SpecVersion.V31) {
			if (hasOneOf) {
				boolean hasNullBranch = property.getOneOf().stream().anyMatch(s -> NULL_TYPE.equals(s.getType())
						|| (s.getTypes() != null && s.getTypes().contains(NULL_TYPE)));
				if (!hasNullBranch) {
					List<Schema> oneOf = new java.util.ArrayList<>(property.getOneOf());
					Schema<Object> nullSchema = new Schema<>();
					nullSchema.addType(NULL_TYPE);
					oneOf.add(nullSchema);
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
		else {
			property.setNullable(true);
		}
	}

	/**
	 * Returns true when the schema imposes no type constraint.
	 *
	 * @param property the property
	 * @return the boolean
	 */
	private boolean isAnySchema(Schema<?> property) {
		return property.get$ref() == null
				&& property.getType() == null
				&& (property.getTypes() == null || property.getTypes().isEmpty());
	}

	/**
	 * Wraps a $ref property in a nullable composite schema. Sibling metadata is copied over.
	 * - OAS 3.0: {@code { nullable: true, allOf: [{ $ref: "..." }] }}
	 * - OAS 3.1: {@code { oneOf: [{ $ref: "..." }, { type: "null" }] }}
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
		if (specVersion == SpecVersion.V31) {
			Schema<Object> nullSchema = new Schema<>();
			nullSchema.addType(NULL_TYPE);
			wrapper.setOneOf(List.of(refSchema, nullSchema));
		}
		else {
			wrapper.setNullable(true);
			wrapper.setAllOf(List.of(refSchema));
		}
		return wrapper;
	}

	/**
	 * Copies the attributes swagger-core may set as siblings of a $ref property onto the wrapper.
	 *
	 * @param source the source
	 * @param target the target
	 */
	private void copySiblingMetadata(Schema<?> source, Schema<Object> target) {
		if (source.getDescription() != null) target.setDescription(source.getDescription());
		if (source.getTitle() != null) target.setTitle(source.getTitle());
		if (source.getDeprecated() != null) target.setDeprecated(source.getDeprecated());
		if (source.getExampleSetFlag()) target.setExample(source.getExample());
		if (source.getExternalDocs() != null) target.setExternalDocs(source.getExternalDocs());
		if (source.getReadOnly() != null) target.setReadOnly(source.getReadOnly());
		if (source.getWriteOnly() != null) target.setWriteOnly(source.getWriteOnly());
		if (source.getExtensions() != null) target.setExtensions(source.getExtensions());
	}
}