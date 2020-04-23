package org.springdoc.core;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.ArrayUtils;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

class MethodParameterPojoExtractor {
	private static final Set<Class<?>> SIMPLE_TYPES;
	private static final Set<Class<?>> COLLECTION_TYPES;

	static {
		Set<Class<?>> simpleTypes = new HashSet<>();
		simpleTypes.add(boolean.class);
		simpleTypes.add(char.class);
		simpleTypes.add(byte.class);
		simpleTypes.add(short.class);
		simpleTypes.add(int.class);
		simpleTypes.add(long.class);
		simpleTypes.add(float.class);
		simpleTypes.add(double.class);

		simpleTypes.add(Boolean.class);
		simpleTypes.add(Character.class);
		simpleTypes.add(Byte.class);
		simpleTypes.add(Short.class);
		simpleTypes.add(Integer.class);
		simpleTypes.add(Long.class);
		simpleTypes.add(Float.class);
		simpleTypes.add(Double.class);

		simpleTypes.add(CharSequence.class);
		simpleTypes.add(Number.class);

		SIMPLE_TYPES = Collections.unmodifiableSet(simpleTypes);

		Set<Class<?>> collectionTypes = new HashSet<>();
		collectionTypes.add(Iterable.class);

		COLLECTION_TYPES = Collections.unmodifiableSet(collectionTypes);
	}

	private static final Nullable NULLABLE_ANNOTATION = new Nullable() {
		@Override
		public Class<? extends Annotation> annotationType() {
			return Nullable.class;
		}
	};

	@NonNull
	static Stream<MethodParameter> extractFrom(Class<?> clazz) {
		return allFieldsOf(clazz).stream()
				.flatMap(f -> fromGetterOfField(clazz, f))
				.filter(Objects::nonNull);
	}

	private static Stream<MethodParameter> fromGetterOfField(Class<?> paramClass, Field field) {
		try {
			Annotation[] filedAnnotations = field.getDeclaredAnnotations();
			Parameter parameter = field.getAnnotation(Parameter.class);
			if (parameter != null && !parameter.required()) {
				filedAnnotations = ArrayUtils.add(filedAnnotations, NULLABLE_ANNOTATION);
			}
			Annotation[] filedAnnotationsNew = filedAnnotations;
			return Stream.of(Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
					.filter(d -> d.getName().equals(field.getName()))
					.map(PropertyDescriptor::getReadMethod)
					.filter(Objects::nonNull)
					.map(method -> new MethodParameter(method, -1))
					.map(param -> new DelegatingMethodParameter(param, field.getName(), filedAnnotationsNew));
		}
		catch (IntrospectionException e) {
			return Stream.of();
		}
	}

	private static List<Field> allFieldsOf(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return fields;
	}
}
