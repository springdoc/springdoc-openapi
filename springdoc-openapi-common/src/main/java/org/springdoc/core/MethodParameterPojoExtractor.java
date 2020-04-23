package org.springdoc.core;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.ArrayUtils;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

class MethodParameterPojoExtractor {
	static Stream<MethodParameter> extractFrom(Class<?> clazz) {
		return extractFrom(clazz, "");
	}

	private static Stream<MethodParameter> extractFrom(Class<?> clazz, String fieldNamePrefix) {
		return allFieldsOf(clazz).stream()
				.flatMap(f -> fromGetterOfField(clazz, f, fieldNamePrefix))
				.filter(Objects::nonNull);
	}

	private static Stream<MethodParameter> fromGetterOfField(Class<?> paramClass, Field field, String fieldNamePrefix) {
		if (isSimpleType(field.getType()))
			return fromSimpleClass(paramClass, field, fieldNamePrefix);
		else
			return extractFrom(field.getType(), fieldNamePrefix + field.getName() + ".");
	}

	private static Stream<MethodParameter> fromSimpleClass(Class<?> paramClass, Field field, String fieldNamePrefix) {
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		if (isOptional(field))
			fieldAnnotations = ArrayUtils.add(fieldAnnotations, NULLABLE_ANNOTATION);
		try {
			Annotation[] finalFieldAnnotations = fieldAnnotations;
			return Stream.of(Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
					.filter(d -> d.getName().equals(field.getName()))
					.map(PropertyDescriptor::getReadMethod)
					.filter(Objects::nonNull)
					.map(method -> new MethodParameter(method, -1))
					.map(param -> new DelegatingMethodParameter(param, fieldNamePrefix + field.getName(), finalFieldAnnotations));
		}
		catch (IntrospectionException e) {
			return Stream.of();
		}
	}

	private static boolean isOptional(Field field) {
		Parameter parameter = field.getAnnotation(Parameter.class);
		return parameter == null || !parameter.required();
	}

	private static List<Field> allFieldsOf(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return fields;
	}

	private static boolean isSimpleType(Class<?> clazz) {
		return SIMPLE_TYPE_PREDICATES.stream().anyMatch(p -> p.test(clazz)) ||
				SIMPLE_TYPES.stream().anyMatch(c -> c.isAssignableFrom(clazz));
	}

	private static final Nullable NULLABLE_ANNOTATION = new Nullable() {
		@Override
		public Class<? extends Annotation> annotationType() {
			return Nullable.class;
		}
	};

	private static final List<Predicate<Class<?>>> SIMPLE_TYPE_PREDICATES = new ArrayList<>();

	private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>();

	static void addSimpleTypePredicate(Predicate<Class<?>> predicate) {
		SIMPLE_TYPE_PREDICATES.add(predicate);
	}

	static void addSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.addAll(Arrays.asList(classes));
	}

	static void removeSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.removeAll(Arrays.asList(classes));
	}

	static {
		SIMPLE_TYPES.add(Boolean.class);
		SIMPLE_TYPES.add(Character.class);
		SIMPLE_TYPES.add(Number.class);
		SIMPLE_TYPES.add(CharSequence.class);
		SIMPLE_TYPES.add(Optional.class);
		SIMPLE_TYPES.add(OptionalInt.class);
		SIMPLE_TYPES.add(OptionalLong.class);
		SIMPLE_TYPES.add(OptionalDouble.class);

		SIMPLE_TYPES.add(Map.class);
		SIMPLE_TYPES.add(Iterable.class);

		SIMPLE_TYPE_PREDICATES.add(Class::isPrimitive);
		SIMPLE_TYPE_PREDICATES.add(Class::isEnum);
		SIMPLE_TYPE_PREDICATES.add(Class::isArray);
	}
}
