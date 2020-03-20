package org.springdoc.core.customisers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.querydsl.core.types.Path;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OperationCustomizer;

import org.springframework.core.MethodParameter;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.CastUtils;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.web.method.HandlerMethod;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/
public class QuerydslPredicateOperationCustomizer implements OperationCustomizer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuerydslPredicateOperationCustomizer.class);
	private static final String ACCESS_EXCEPTION_OCCURRED = "NoSuchFieldException or IllegalAccessException occurred : {}";

	private QuerydslBindingsFactory querydslBindingsFactory;

	public QuerydslPredicateOperationCustomizer(QuerydslBindingsFactory querydslBindingsFactory) {
		this.querydslBindingsFactory = querydslBindingsFactory;
	}

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getParameters() == null) {
			return operation;
		}

		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

		int parametersLength = methodParameters.length;
		List<Parameter> parametersToAddToOperation = new ArrayList<>();
		for (int i = 0; i < parametersLength; i++) {
			MethodParameter parameter = methodParameters[i];
			QuerydslPredicate predicate = parameter.getParameterAnnotation(QuerydslPredicate.class);

			if (predicate == null) {
				continue;
			}

			QuerydslBindings bindings = extractQdslBindings(predicate);

			Set<String> fieldsToAdd = Arrays.stream(predicate.root().getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());

			Map<String, Object> pathSpecMap = getPathSpec(bindings, "pathSpecs");
			//remove blacklisted fields
			Set<String> blacklist = getFieldValues(bindings, "blackList");
			fieldsToAdd.removeIf(blacklist::contains);

			Set<String> whiteList = getFieldValues(bindings, "whiteList");
			Set<String> aliases = getFieldValues(bindings, "aliases");

			fieldsToAdd.addAll(aliases);
			fieldsToAdd.addAll(whiteList);
			for (String fieldName : fieldsToAdd) {
				Type type = getFieldType(fieldName, pathSpecMap, predicate.root());
				io.swagger.v3.oas.models.parameters.Parameter newParameter = buildParam(type, fieldName);

				parametersToAddToOperation.add(newParameter);
			}
		}
		operation.getParameters().addAll(parametersToAddToOperation);
		return operation;
	}

	private QuerydslBindings extractQdslBindings(QuerydslPredicate predicate) {
		ClassTypeInformation<?> classTypeInformation = ClassTypeInformation.from(predicate.root());
		TypeInformation<?> domainType = classTypeInformation.getRequiredActualType();

		Optional<Class<? extends QuerydslBinderCustomizer<?>>> bindingsAnnotation = Optional.of(predicate)
				.map(QuerydslPredicate::bindings)
				.map(CastUtils::cast);

		return bindingsAnnotation
				.map(it -> querydslBindingsFactory.createBindingsFor(domainType, it))
				.orElseGet(() -> querydslBindingsFactory.createBindingsFor(domainType));
	}

	private Set<String> getFieldValues(QuerydslBindings instance, String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			if (Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
			}
			return (Set<String>) field.get(instance);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.warn(ACCESS_EXCEPTION_OCCURRED, e.getMessage());
		}
		return Collections.emptySet();
	}

	private Map<String, Object> getPathSpec(QuerydslBindings instance, String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			if (Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
			}
			return (Map<String, Object>) field.get(instance);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.warn(ACCESS_EXCEPTION_OCCURRED, e.getMessage());
		}
		return Collections.emptyMap();
	}

	private Optional<Path<?>> getPathFromPathSpec(Object instance) {
		try {
			if (instance == null) {
				return Optional.empty();
			}
			Field field = instance.getClass().getDeclaredField("path");
			if (Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
			}
			return (Optional<Path<?>>) field.get(instance);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.warn(ACCESS_EXCEPTION_OCCURRED, e.getMessage());
		}
		return Optional.empty();
	}

	/***
	 * Tries to figure out the Type of the field. It first checks the Qdsl pathSpecMap before checking the root class. Defaults to String.class
	 * @param fieldName The name of the field used as reference to get the type
	 * @param pathSpecMap The Qdsl path specifications as defined in the resolved bindings
	 * @param root The root type where the paths are gotten
	 * @return The type of the field. Returns
	 */
	private Type getFieldType(String fieldName, Map<String, Object> pathSpecMap, Class<?> root) {
		try {
			Object pathAndBinding = pathSpecMap.get(fieldName);
			Optional<Path<?>> path = getPathFromPathSpec(pathAndBinding);

			Type genericType;
			Field declaredField = null;
			if (path.isPresent()) {
				genericType = path.get().getType();
			} else {
				declaredField = root.getDeclaredField(fieldName);
				genericType = declaredField.getGenericType();
			}
			if (genericType != null) {
				return genericType;
			}
		} catch (NoSuchFieldException e) {
			LOGGER.warn("Field {} not found on {} : {}", fieldName, root.getName(), e.getMessage());
		}
		return String.class;
	}

	/***
	 * Constructs the parameter
	 * @param type The type of the parameter
	 * @param name The name of the parameter
	 * @return The swagger parameter
	 */
	private io.swagger.v3.oas.models.parameters.Parameter buildParam(Type type, String name) {
		io.swagger.v3.oas.models.parameters.Parameter parameter = new io.swagger.v3.oas.models.parameters.Parameter();

		if (StringUtils.isBlank(parameter.getName())) {
			parameter.setName(name);
		}

		if (StringUtils.isBlank(parameter.getIn())) {
			parameter.setIn("query");
		}

		if (parameter.getSchema() == null) {
			Schema<?> schema = null;
			PrimitiveType primitiveType = PrimitiveType.fromType(type);
			if (primitiveType != null) {
				schema = primitiveType.createProperty();
			} else {
				ResolvedSchema resolvedSchema = ModelConverters.getInstance()
						.resolveAsResolvedSchema(
								new io.swagger.v3.core.converter.AnnotatedType(type).resolveAsRef(true));
				// could not resolve the schema or this schema references other schema
				// we dont want this since there's no reference to the components in order to register a new schema if it doesnt already exist
				// defaulting to string
				if (resolvedSchema == null || !resolvedSchema.referencedSchemas.isEmpty()) {
					schema = PrimitiveType.fromType(String.class).createProperty();
				} else {
					schema = resolvedSchema.schema;
				}
			}
			parameter.setSchema(schema);
		}
		return parameter;
	}
}