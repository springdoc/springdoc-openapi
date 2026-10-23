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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.utils.SchemaUtils.ANNOTATIONS_FOR_NULLABLE;

/**
 * Marks schema properties as nullable for Java class properties that are explicitly
 * annotated with a {@code @Nullable} annotation (e.g. JSpecify, Spring, JSR-305), either on
 * the field, its getter / record accessor, or its constructor parameter. Both declaration
 * and type-use annotations are supported.
 *
 * @author Mattias-Sehlstedt
 * @see AbstractNullablePropertyCustomizer
 */
public class JavaNullablePropertyCustomizer extends AbstractNullablePropertyCustomizer {

	/**
	 * Instantiates a new Java nullable property customizer.
	 *
	 * @param objectMapperProvider the object mapper provider
	 */
	public JavaNullablePropertyCustomizer(ObjectMapperProvider objectMapperProvider) {
		super(objectMapperProvider);
	}

	/**
	 * Finds the serialized names of the properties explicitly annotated as nullable.
	 *
	 * @param javaType the java type
	 * @return the set of nullable property names
	 */
	@Override
	protected Set<String> findNullableProperties(JavaType javaType) {
		List<BeanPropertyDefinition> definitions;
		try {
			ObjectMapper mapper = getObjectMapperProvider().jsonMapper();
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

}
