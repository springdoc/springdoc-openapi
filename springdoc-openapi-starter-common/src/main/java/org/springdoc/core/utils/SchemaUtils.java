package org.springdoc.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

import org.springframework.lang.Nullable;

import static org.springdoc.core.utils.Constants.OPENAPI_ARRAY_TYPE;
import static org.springdoc.core.utils.Constants.OPENAPI_STRING_TYPE;
import static org.springdoc.core.utils.SpringDocKotlinUtils.isKotlinDeclaringClass;
import static org.springdoc.core.utils.SpringDocKotlinUtils.kotlinConstructorParamIsOptional;
import static org.springdoc.core.utils.SpringDocKotlinUtils.kotlinNullability;

/**
 * The type Validation utils.
 *
 * @author dyun
 */
public class SchemaUtils {

	/**
	 * The constant JAVA_FIELD_NULLABLE_DEFAULT.
	 */
	public static final Boolean JAVA_FIELD_NULLABLE_DEFAULT = true;

	/**
	 * The constant ANNOTATIONS_FOR_REQUIRED.
	 */
	// using string litterals to support both validation-api v1 and v2
	public static final List<String> ANNOTATIONS_FOR_REQUIRED = Arrays.asList("NotNull", "NonNull", "NotBlank",
			"NotEmpty");

	/**
	 * The constant ANNOTATIONS_FOR_NULLABLE.
	 */
	public static final List<String> ANNOTATIONS_FOR_NULLABLE =
			Arrays.asList("Nullable");
	/**
	 * The constant OPTIONAL_TYPES.
	 */
	private static final Set<Class<?>> OPTIONAL_TYPES = new HashSet<>();

	static {
		OPTIONAL_TYPES.add(Optional.class);
		OPTIONAL_TYPES.add(OptionalInt.class);
		OPTIONAL_TYPES.add(OptionalLong.class);
		OPTIONAL_TYPES.add(OptionalDouble.class);
	}

	/**
	 * The Kotlin utils optional.
	 */
	private final Optional<SpringDocKotlinUtils> kotlinUtilsOptional;

	/**
	 * The constructor.
	 *
	 * @param kotlinUtilsOptional the kotlin utils optional
	 */
	public SchemaUtils(Optional<SpringDocKotlinUtils> kotlinUtilsOptional) {
		this.kotlinUtilsOptional = kotlinUtilsOptional;
	}

	/**
	 * Is swagger visible.
	 *
	 * @param schema    the schema
	 * @param parameter the parameter
	 * @return the boolean
	 */
	public static boolean swaggerVisible(@Nullable io.swagger.v3.oas.annotations.media.Schema schema,
			@Nullable Parameter parameter) {
		if (parameter != null) {
			return !parameter.hidden();
		}
		if (schema != null) {
			return !schema.hidden();
		}
		return true;
	}

	/**
	 * Is swagger required. it may return {@code null} if not specified require value or
	 * mode
	 *
	 * @param schema    the schema
	 * @param parameter the parameter
	 * @return the boolean or {@code null}
	 * @see Parameter#required()
	 * @see io.swagger.v3.oas.annotations.media.Schema#required()
	 * @see io.swagger.v3.oas.annotations.media.Schema#requiredMode()
	 */
	@Nullable
	public static Boolean swaggerRequired(@Nullable io.swagger.v3.oas.annotations.media.Schema schema,
			@Nullable Parameter parameter) {
		if (parameter != null && parameter.required()) {
			return true;
		}
		if (schema != null) {
			if (schema.required()
					|| schema.requiredMode() == RequiredMode.REQUIRED) {
				return true;
			}
			if (schema.requiredMode() == RequiredMode.NOT_REQUIRED) {
				return false;
			}
		}
		return null;
	}

	/**
	 * Is annotated notnull.
	 *
	 * @param annotations the field annotations
	 * @return the boolean
	 */
	public static boolean annotatedNotNull(List<Annotation> annotations) {
		Collection<String> annotationSimpleNames = annotations.stream()
				.map(annotation -> annotation.annotationType().getSimpleName())
				.collect(Collectors.toSet());
		return ANNOTATIONS_FOR_REQUIRED.stream().anyMatch(annotationSimpleNames::contains);
	}

	/**
	 * Is field nullable. java default is nullable
	 * {@link SchemaUtils#JAVA_FIELD_NULLABLE_DEFAULT}
	 *
	 * @param field the field
	 * @return the boolean
	 */
	public boolean fieldNullable(Field field) {
		// primitives cannot be null
		if (field.getType().isPrimitive()) return false;

		// Optional-like wrappers
		if (OPTIONAL_TYPES.stream().anyMatch(c -> c.isAssignableFrom(field.getType()))) {
			return true;
		}

		// @Nullable/@NotNull
		Boolean ann = nullableFromAnnotations(field);
		if (ann != null) return ann;

		// Kotlin nullability
		if (kotlinUtilsOptional.isPresent() && isKotlinDeclaringClass(field)) {
			Boolean kotlin = kotlinNullability(field);
			if (kotlin != null) return kotlin;
		}

		return JAVA_FIELD_NULLABLE_DEFAULT;
	}

	/**
	 * Is field required.
	 *
	 * @param field     the field
	 * @param schema    the schema
	 * @param parameter the parameter
	 * @return the boolean
	 * @see Parameter#required()
	 * @see io.swagger.v3.oas.annotations.media.Schema#required()
	 * @see io.swagger.v3.oas.annotations.media.Schema#requiredMode()
	 */
	public boolean fieldRequired(Field field, io.swagger.v3.oas.annotations.media.Schema schema, Parameter parameter) {
		Boolean swagger = swaggerRequired(schema, parameter);
		if (swagger != null) return swagger;

		// Optional-like wrapper → not required
		if (OPTIONAL_TYPES.stream().anyMatch(c -> c.isAssignableFrom(field.getType()))) return false;

		// @Nullable/@NotNull
		if (hasNullableAnnotation(field) || hasNullableOnGetter(field) || hasNullableOnCtorParam(field)) {
			return false;
		}
		if (hasNotNullAnnotation(field) || hasNotNullOnGetter(field) || hasNotNullOnCtorParam(field)) {
			return true;
		}

		// Kotlin logic
		if (kotlinUtilsOptional.isPresent()  && isKotlinDeclaringClass(field)) {
			if (fieldNullable(field)) return false;
			Boolean hasDefault = kotlinConstructorParamIsOptional(field);
			if (Boolean.TRUE.equals(hasDefault)) return false;
			return true;
		}

		// Jackson @JsonProperty(required = true)
		JsonProperty jp = getJsonProperty(field);
		if (jp != null && jp.required()) return true;

		return false;
	}

	/**
	 * Apply validations to schema. the annotation order effects the result of the
	 * validation.
	 *
	 * @param schema         the schema
	 * @param annotations    the annotations
	 * @param openapiVersion the openapi version
	 */
	public static void applyValidationsToSchema(Schema<?> schema, List<Annotation> annotations, String openapiVersion) {
		annotations.forEach(anno -> {
			String annotationName = anno.annotationType().getSimpleName();
			if (annotationName.equals(Positive.class.getSimpleName())) {
				if (OpenApiVersion.OPENAPI_3_1.getVersion().equals(openapiVersion)) {
					schema.setExclusiveMinimumValue(BigDecimal.ZERO);
				}
				else {
					schema.setMinimum(BigDecimal.ZERO);
					schema.setExclusiveMinimum(true);
				}
			}
			if (annotationName.equals(PositiveOrZero.class.getSimpleName())) {
				schema.setMinimum(BigDecimal.ZERO);
			}
			if (annotationName.equals(NegativeOrZero.class.getSimpleName())) {
				schema.setMaximum(BigDecimal.ZERO);
			}
			if (annotationName.equals(Negative.class.getSimpleName())) {
				if (OpenApiVersion.OPENAPI_3_1.getVersion().equals(openapiVersion)) {
					schema.setExclusiveMaximumValue(BigDecimal.ZERO);
				}
				else {
					schema.setMaximum(BigDecimal.ZERO);
					schema.setExclusiveMaximum(true);
				}
			}
			if (annotationName.equals(Min.class.getSimpleName())) {
				schema.setMinimum(BigDecimal.valueOf(((Min) anno).value()));
			}
			if (annotationName.equals(Max.class.getSimpleName())) {
				schema.setMaximum(BigDecimal.valueOf(((Max) anno).value()));
			}
			if (annotationName.equals(DecimalMin.class.getSimpleName())) {
				DecimalMin min = (DecimalMin) anno;
				if (min.inclusive()) {
					schema.setMinimum(BigDecimal.valueOf(Double.parseDouble(min.value())));
				}
				else {
					schema.setExclusiveMinimum(true);
				}
			}
			if (annotationName.equals(DecimalMax.class.getSimpleName())) {
				DecimalMax max = (DecimalMax) anno;
				if (max.inclusive()) {
					schema.setMaximum(BigDecimal.valueOf(Double.parseDouble(max.value())));
				}
				else {
					schema.setExclusiveMaximum(true);
				}
			}
			if (annotationName.equals(Size.class.getSimpleName())) {
				String type = schema.getType();
				if (type == null && schema.getTypes() != null && schema.getTypes().size() == 1)
					type = schema.getTypes().iterator().next();
				if (OPENAPI_ARRAY_TYPE.equals(type)) {
					schema.setMinItems(((Size) anno).min());
					schema.setMaxItems(((Size) anno).max());
				}
				else if (OPENAPI_STRING_TYPE.equals(type)) {
					schema.setMinLength(((Size) anno).min());
					schema.setMaxLength(((Size) anno).max());
				}
			}
			if (annotationName.equals(Pattern.class.getSimpleName())) {
				schema.setPattern(((Pattern) anno).regexp());
			}
		});
		if (schema!=null && annotatedNotNull(annotations)) {
			String specVersion = schema.getSpecVersion().name();
			if (!"V30".equals(specVersion)) {
				schema.setNullable(false);
			}
		}
	}

	/**
	 * Nullable from annotations boolean.
	 *
	 * @param field the field
	 * @return the boolean
	 */
	private static Boolean nullableFromAnnotations(Field field) {
		if (hasNullableAnnotation(field)) return true;
		if (hasNotNullAnnotation(field)) return false;
		Method getter = findGetter(field);
		if (getter != null) {
			if (hasNullableAnnotation(getter)) return true;
			if (hasNotNullAnnotation(getter)) return false;
		}
		return null;
	}

	/**
	 * Has nullable annotation boolean.
	 *
	 * @param el the el
	 * @return the boolean
	 */
	private static boolean hasNullableAnnotation(AnnotatedElement el) {
		if (el == null) return false;
		for (Annotation ann : el.getAnnotations()) {
			if (ANNOTATIONS_FOR_NULLABLE.contains(ann.annotationType().getSimpleName())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Has not null annotation boolean.
	 *
	 * @param el the el
	 * @return the boolean
	 */
	private static boolean hasNotNullAnnotation(AnnotatedElement el) {
		if (el == null) {
			return false;
		}
		for (Annotation ann : el.getAnnotations()) {
			String simpleName = ann.annotationType().getSimpleName();
			if (ANNOTATIONS_FOR_REQUIRED.contains(simpleName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Has nullable on getter boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	private static boolean hasNullableOnGetter(Field f) {
		Method g = findGetter(f);
		return g != null && hasNullableAnnotation(g);
	}

	/**
	 * Has not null on getter boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	private static boolean hasNotNullOnGetter(Field f) {
		Method g = findGetter(f);
		return g != null && hasNotNullAnnotation(g);
	}

	/**
	 * Has nullable on ctor param boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	private static boolean hasNullableOnCtorParam(Field f) {
		return ctorParamHasAnyAnnotationSimpleName(f, ANNOTATIONS_FOR_NULLABLE);
	}

	/**
	 * Has not null on ctor param boolean.
	 *
	 * @param f the f
	 * @return the boolean
	 */
	private static boolean hasNotNullOnCtorParam(Field f) {
		return ctorParamHasAnyAnnotationSimpleName(f, ANNOTATIONS_FOR_REQUIRED);
	}

	/**
	 * Ctor param has any annotation simple name boolean.
	 *
	 * @param f           the f
	 * @param simpleNames the simple names
	 * @return the boolean
	 */
	private static boolean ctorParamHasAnyAnnotationSimpleName(Field f, Collection<String> simpleNames) {
		if (f == null || simpleNames == null || simpleNames.isEmpty()) return false;

		final String fieldName = f.getName();
		final Class<?> declaring = f.getDeclaringClass();

		try {
			for (Constructor<?> ctor : declaring.getDeclaredConstructors()) {
				java.lang.reflect.Parameter[] params = ctor.getParameters();
				for (java.lang.reflect.Parameter p : params) {
					// A) compiled with -parameters
					if (fieldName.equals(p.getName()) && paramHasAnyAnnotationSimpleName(p, simpleNames)) {
						return true;
					}
					// B) fallback: @JsonProperty("fieldName") on the parameter
					if (hasJsonPropertyName(p, fieldName) && paramHasAnyAnnotationSimpleName(p, simpleNames)) {
						return true;
					}
				}
			}
		} catch (Throwable ignored) {
			// best-effort only
		}
		return false;
	}

	/**
	 * Param has any annotation simple name boolean.
	 *
	 * @param p           the p
	 * @param simpleNames the simple names
	 * @return the boolean
	 */
	private static boolean paramHasAnyAnnotationSimpleName(java.lang.reflect.Parameter p, Collection<String> simpleNames) {
		for (Annotation ann : p.getAnnotations()) {
			if (simpleNames.contains(ann.annotationType().getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Has json property name boolean.
	 *
	 * @param p        the p
	 * @param expected the expected
	 * @return the boolean
	 */
	private static boolean hasJsonPropertyName(java.lang.reflect.Parameter p, String expected) {
		try {
			JsonProperty jp = p.getAnnotation(JsonProperty.class);
			return jp != null && expected.equals(jp.value());
		} catch (Throwable ignored) {
			return false;
		}
	}

	/**
	 * Find getter method.
	 *
	 * @param f the f
	 * @return the method
	 */
	private static Method findGetter(Field f) {
		String n = f.getName();
		String cap = Character.toUpperCase(n.charAt(0)) + n.substring(1);
		String[] names = (f.getType() == boolean.class || f.getType() == Boolean.class)
				? new String[] { "is" + cap, "get" + cap }
				: new String[] { "get" + cap };
		for (String m : names) {
			try {
				return f.getDeclaringClass().getMethod(m);
			} catch (NoSuchMethodException ignored) {}
		}
		return null;
	}

	/**
	 * Gets json property.
	 *
	 * @param f the f
	 * @return the json property
	 */
	private static JsonProperty getJsonProperty(Field f) {
		JsonProperty jp = f.getAnnotation(JsonProperty.class);
		if (jp != null) return jp;
		Method g = findGetter(f);
		if (g != null) return g.getAnnotation(JsonProperty.class);
		return null;
	}
}
