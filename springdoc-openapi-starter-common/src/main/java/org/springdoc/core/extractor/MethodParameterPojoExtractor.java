/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */
package org.springdoc.core.extractor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.parsers.ReturnTypeParser;

import org.springframework.core.MethodParameter;

import static org.springdoc.core.utils.Constants.DOT;

/**
 * The type Method parameter pojo extractor.
 * @author bnasslahsen
 */
public class MethodParameterPojoExtractor {

	/**
	 * The constant SIMPLE_TYPE_PREDICATES.
	 */
	private static final List<Predicate<Class<?>>> SIMPLE_TYPE_PREDICATES = new ArrayList<>();

	/**
	 * The constant SIMPLE_TYPES.
	 */
	private static final Set<Class<?>> SIMPLE_TYPES = new LinkedHashSet<>();

	static {
		SIMPLE_TYPES.add(CharSequence.class);
		SIMPLE_TYPES.add(Optional.class);
		SIMPLE_TYPES.add(OptionalInt.class);
		SIMPLE_TYPES.add(OptionalLong.class);
		SIMPLE_TYPES.add(OptionalDouble.class);
		SIMPLE_TYPES.add(AtomicLong.class);
		SIMPLE_TYPES.add(AtomicInteger.class);
		SIMPLE_TYPES.add(Charset.class);

		SIMPLE_TYPES.add(Map.class);
		SIMPLE_TYPES.add(Iterable.class);
		SIMPLE_TYPES.add(Duration.class);
		SIMPLE_TYPES.add(LocalTime.class);

		SIMPLE_TYPE_PREDICATES.add(Class::isPrimitive);
		SIMPLE_TYPE_PREDICATES.add(Class::isEnum);
		SIMPLE_TYPE_PREDICATES.add(Class::isArray);
		SIMPLE_TYPE_PREDICATES.add(MethodParameterPojoExtractor::isSwaggerPrimitiveType);
		SIMPLE_TYPE_PREDICATES.add(aClass -> aClass.getName().startsWith("org.codehaus.groovy.reflection"));
	}

	/**
	 * Instantiates a new Method parameter pojo extractor.
	 */
	private MethodParameterPojoExtractor() {
	}

	/**
	 * Extract from stream.
	 *
	 * @param clazz the clazz
	 * @return the stream
	 */
	static Stream<MethodParameter> extractFrom(Class<?> clazz) {
		return extractFrom(clazz, "");
	}

	/**
	 * Extract from stream.
	 *
	 * @param clazz the clazz
	 * @param fieldNamePrefix the field name prefix
	 * @return the stream
	 */
	private static Stream<MethodParameter> extractFrom(Class<?> clazz, String fieldNamePrefix) {
		return allFieldsOf(clazz).stream()
				.filter(field -> !field.getType().equals(clazz))
				.flatMap(f -> fromGetterOfField(clazz, f, fieldNamePrefix))
				.filter(Objects::nonNull);
	}

	/**
	 * From getter of field stream.
	 *
	 * @param paramClass the param class
	 * @param field the field
	 * @param fieldNamePrefix the field name prefix
	 * @return the stream
	 */
	private static Stream<MethodParameter> fromGetterOfField(Class<?> paramClass, Field field, String fieldNamePrefix) {
		Class<?> type = extractType(paramClass, field);

		if (Objects.isNull(type))
			return Stream.empty();

		if (isSimpleType(type))
			return fromSimpleClass(paramClass, field, fieldNamePrefix);
		else {
			String prefix = fieldNamePrefix + field.getName() + DOT;
			return extractFrom(type, prefix);
		}
	}

	/**
	 * Extract the type
	 * @param paramClass
	 * @param field
	 * @return The revoled type or null if it was not a reifiable type
	 */
	private static Class<?> extractType(Class<?> paramClass, Field field) {
		Class<?> type = field.getType();
		if (field.getGenericType() instanceof TypeVariable<?>) {
			Type fieldType = ReturnTypeParser.resolveType(field.getGenericType(), paramClass);

			if (fieldType instanceof Class<?>)
				type = (Class<?>) fieldType;
			else    // This is the case for not reifiable types
				type = null;
		}

		return type;
	}

	/**
	 * From simple class stream.
	 *
	 * @param paramClass the param class
	 * @param field the field
	 * @param fieldNamePrefix the field name prefix
	 * @return the stream
	 */
	private static Stream<MethodParameter> fromSimpleClass(Class<?> paramClass, Field field, String fieldNamePrefix) {
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		try {
			Parameter parameter = field.getAnnotation(Parameter.class);
			boolean isNotRequired = parameter == null || !parameter.required();
			if (paramClass.getSuperclass() != null && paramClass.isRecord()) {
				return Stream.of(paramClass.getRecordComponents())
						.filter(d -> d.getName().equals(field.getName()))
						.map(RecordComponent::getAccessor)
						.map(method -> new MethodParameter(method, -1))
						.map(methodParameter -> DelegatingMethodParameter.changeContainingClass(methodParameter, paramClass))
						.map(param -> new DelegatingMethodParameter(param, fieldNamePrefix + field.getName(), fieldAnnotations, true, isNotRequired));

			}
			else
				return Stream.of(Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
						.filter(d -> d.getName().equals(field.getName()))
						.map(PropertyDescriptor::getReadMethod)
						.filter(Objects::nonNull)
						.map(method -> new MethodParameter(method, -1))
						.map(methodParameter -> DelegatingMethodParameter.changeContainingClass(methodParameter, paramClass))
						.map(param -> new DelegatingMethodParameter(param, fieldNamePrefix + field.getName(), fieldAnnotations, true, isNotRequired));
		}
		catch (IntrospectionException e) {
			return Stream.of();
		}
	}

	/**
	 * All fields of list.
	 *
	 * @param clazz the clazz
	 * @return the list
	 */
	private static List<Field> allFieldsOf(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return fields;
	}

	/**
	 * Is simple type boolean.
	 *
	 * @param clazz the clazz
	 * @return the boolean
	 */
	static boolean isSimpleType(Class<?> clazz) {
		return SIMPLE_TYPE_PREDICATES.stream().anyMatch(p -> p.test(clazz)) ||
				SIMPLE_TYPES.stream().anyMatch(c -> c.isAssignableFrom(clazz));
	}

	/**
	 * Is swagger primitive type boolean.
	 *
	 * @param clazz the clazz
	 * @return the boolean
	 */
	public static boolean isSwaggerPrimitiveType(Class<?> clazz) {
		PrimitiveType primitiveType = PrimitiveType.fromType(clazz);
		return primitiveType != null;
	}

	/**
	 * Add simple type predicate.
	 *
	 * @param predicate the predicate
	 */
	public static void addSimpleTypePredicate(Predicate<Class<?>> predicate) {
		SIMPLE_TYPE_PREDICATES.add(predicate);
	}

	/**
	 * Add simple types.
	 *
	 * @param classes the classes
	 */
	public static void addSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.addAll(Arrays.asList(classes));
	}

	/**
	 * Remove simple types.
	 *
	 * @param classes the classes
	 */
	public static void removeSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.removeAll(Arrays.asList(classes));
	}

}
