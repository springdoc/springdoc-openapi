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

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.providers.ObjectMapperProvider;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * The type Polymorphic model converter.
 *
 * @author bnasslahsen
 */
public class PolymorphicModelConverter implements ModelConverter {

	private static final String LINKS = "_links";
	/**
	 * The default suffix used when emitting oneOf unions as component refs.
	 */
	public static final String DEFAULT_ONE_OF_REF_SUFFIX = "Variant";

	/**
	 * The constant PARENT_TYPES_TO_IGNORE.
	 */
	private static final Set<String> PARENT_TYPES_TO_IGNORE = new CopyOnWriteArraySet<>();

	/**
	 * The constant TYPES_TO_SKIP.
	 */
	private static final Set<String> TYPES_TO_SKIP = new CopyOnWriteArraySet<>();

	static {
		PARENT_TYPES_TO_IGNORE.add("JsonSchema");
		PARENT_TYPES_TO_IGNORE.add("Pageable");
		PARENT_TYPES_TO_IGNORE.add("EntityModel");
	}

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Whether discovered polymorphic oneOf unions should be materialized as component refs.
	 */
	private final boolean oneOfAsRef;

	/**
	 * The suffix appended to the base schema name for generated oneOf component refs.
	 */
	private final String oneOfRefSuffix;

	/**
	 * Instantiates a new Polymorphic model converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public PolymorphicModelConverter(ObjectMapperProvider springDocObjectMapper) {
		this(springDocObjectMapper, false);
	}

	/**
	 * Instantiates a new Polymorphic model converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 * @param oneOfAsRef            whether oneOf unions should be emitted as component refs
	 */
	public PolymorphicModelConverter(ObjectMapperProvider springDocObjectMapper, boolean oneOfAsRef) {
		this(springDocObjectMapper, oneOfAsRef, DEFAULT_ONE_OF_REF_SUFFIX);
	}

	/**
	 * Instantiates a new Polymorphic model converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 * @param oneOfAsRef            whether oneOf unions should be emitted as component refs
	 * @param oneOfRefSuffix        the suffix for generated oneOf component names
	 */
	public PolymorphicModelConverter(ObjectMapperProvider springDocObjectMapper, boolean oneOfAsRef, String oneOfRefSuffix) {
		this.springDocObjectMapper = springDocObjectMapper;
		this.oneOfAsRef = oneOfAsRef;
		this.oneOfRefSuffix = (oneOfRefSuffix == null || oneOfRefSuffix.isBlank()) ? DEFAULT_ONE_OF_REF_SUFFIX : oneOfRefSuffix.trim();
	}

	/**
	 * Add parent type.
	 *
	 * @param parentTypes the parent types
	 */
	public static void addParentType(String... parentTypes) {
		PARENT_TYPES_TO_IGNORE.addAll(List.of(parentTypes));
	}

	/**
	 * Gets resolved schema.
	 *
	 * @param javaType       the java type
	 * @param resolvedSchema the resolved schema
	 * @return the resolved schema
	 */
	private Schema<?> getResolvedSchema(JavaType javaType, Schema<?> resolvedSchema) {
		if (resolvedSchema instanceof ObjectSchema && resolvedSchema.getProperties() != null) {
			Schema<?> found = resolvedSchema.getProperties().get(javaType.getRawClass().getName());
			if (found != null) {
				resolvedSchema = found;
			}
			else {
				found = resolvedSchema.getProperties().get(javaType.getRawClass().getSimpleName());
				if (found != null) {
					resolvedSchema = found;
				}
			}
		}
		return resolvedSchema;
	}

	/**
	 * Removes _links from allOf child schemas to prevent duplication.
	 * In allOf composition, child schemas (allOf[1+]) should not redefine
	 * inherited properties like _links that come from the parent (allOf[0]).
	 *
	 * @param composedSchema the composed schema with allOf structure
	 */
	private void removeLinksFromAllOfChild(ComposedSchema composedSchema) {
		List<Schema> allOf = composedSchema.getAllOf();
		if (allOf != null && allOf.size() > 1) {
			// allOf[0] is the parent schema (first element in allOf)
			// allOf[1+] are the child's own properties (second element onwards in allOf)
			for (int i = 1; i < allOf.size(); i++) {
				Schema childSchema = allOf.get(i);
				if (childSchema != null && childSchema.getProperties() != null) {
					// Remove _links (inherited from parent)
					childSchema.getProperties().remove(LINKS);
				}
			}
		}
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			for (BeanPropertyBiDefinition propertyDef : introspectBeanProperties(javaType)) {
				if (propertyDef.isAnyAnnotated(JsonUnwrapped.class)) {
					if (!TypeNameResolver.std.getUseFqn())
						PARENT_TYPES_TO_IGNORE.add(javaType.getRawClass().getSimpleName());
					else
						PARENT_TYPES_TO_IGNORE.add(javaType.getRawClass().getName());
				}
				else {
					io.swagger.v3.oas.annotations.media.Schema declaredSchema = propertyDef.getAnyAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
					if (declaredSchema != null &&
							(ArrayUtils.isNotEmpty(declaredSchema.oneOf()) || ArrayUtils.isNotEmpty(declaredSchema.allOf())) &&
							propertyDef.getPrimaryType() != null &&
							propertyDef.getPrimaryType().getRawClass() != null) {
						TYPES_TO_SKIP.add(propertyDef.getPrimaryType().getRawClass().getSimpleName());
					}
				}
			}
			if (chain.hasNext()) {
				if (!type.isResolveAsRef() && type.getParent() != null
						&& PARENT_TYPES_TO_IGNORE.stream().noneMatch(ignore -> type.getParent().getName().startsWith(ignore)))
					type.resolveAsRef(true);
				Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
				resolvedSchema = getResolvedSchema(javaType, resolvedSchema);

                if (resolvedSchema instanceof ComposedSchema composedSchema && hasAllOf(composedSchema)) {
                    removeLinksFromAllOfChild(composedSchema);
                }

                if (resolvedSchema == null || resolvedSchema.get$ref() == null) {
					return resolvedSchema;
				}
				if (resolvedSchema.get$ref().contains(Components.COMPONENTS_SCHEMAS_REF)) {
					String schemaName = resolvedSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
					Schema existingSchema = context.getDefinedModels().get(schemaName);
					if (existingSchema != null && (existingSchema.getOneOf() != null || existingSchema.getAllOf() != null)) {
						return resolvedSchema;
					}
				}
				return composePolymorphicSchema(type, resolvedSchema, context);
			}
		}
		return null;
	}

	/**
	 * Compose polymorphic schema.
	 *
	 * @param type    the type
	 * @param schema  the schema
	 * @param context the context
	 * @return the schema
	 */
	private Schema composePolymorphicSchema(AnnotatedType type, Schema schema, ModelConverterContext context) {
		Map<String, Schema> schemas = context.getDefinedModels();
		String ref = schema.get$ref();
		List<Schema> composedSchemas = findComposedSchemas(ref, schemas.values());
		if (composedSchemas.isEmpty()) {
			return schema;
		}
		ComposedSchema result = new ComposedSchema();
		if (isConcreteClass(type)) {
			result.addOneOfItem(schema);
		}
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		Class<?> clazz = javaType.getRawClass();
		if (TYPES_TO_SKIP.stream().noneMatch(typeToSkip -> typeToSkip.equals(clazz.getSimpleName()))) {
			composedSchemas.forEach(result::addOneOfItem);
		}

		if (oneOfAsRef && result.getOneOf() != null && !result.getOneOf().isEmpty()) {
			return createOneOfComponentRef(schema, result.getOneOf(), schemas, context);
		}

		removePotentialLinkDuplications(result);

		return result;
	}

	private void removePotentialLinkDuplications(ComposedSchema result) {
		if (result.getOneOf() != null) {
			result.getOneOf().stream()
					.filter(s -> s.getProperties() != null)
					.forEach(s -> s.getProperties().remove(LINKS));
		}
	}

	/**
	 * Creates or reuses a named component schema for oneOf unions and returns a ref to it.
	 */
	private Schema createOneOfComponentRef(Schema schema, List<Schema> oneOfSchemas, Map<String, Schema> schemas, ModelConverterContext context) {
		List<Schema> normalizedOneOf = normalizeOneOfSchemas(oneOfSchemas);
		if (normalizedOneOf.isEmpty()) {
			return schema;
		}

		String signature = buildOneOfSignature(normalizedOneOf);
		String existingSchemaName = findExistingOneOfSchemaName(signature, schemas);
		if (existingSchemaName != null) {
			return new Schema<>().$ref(Components.COMPONENTS_SCHEMAS_REF + existingSchemaName);
		}

		String baseSchemaName = extractSchemaName(schema.get$ref());
		String oneOfSchemaName = resolveOneOfSchemaName(baseSchemaName + oneOfRefSuffix, schemas);

		ComposedSchema oneOfComponentSchema = new ComposedSchema();
		normalizedOneOf.forEach(oneOfComponentSchema::addOneOfItem);
		oneOfComponentSchema.setName(oneOfSchemaName);
		context.defineModel(oneOfSchemaName, oneOfComponentSchema);

		return new Schema<>().$ref(Components.COMPONENTS_SCHEMAS_REF + oneOfSchemaName);
	}

	private List<Schema> normalizeOneOfSchemas(List<Schema> oneOfSchemas) {
		LinkedHashSet<String> distinctRefs = oneOfSchemas.stream()
				.map(Schema::get$ref)
				.filter(Objects::nonNull)
				.collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

		return distinctRefs.stream()
				.map(ref -> new Schema<>().$ref(ref))
				.toList();
	}

	private String findExistingOneOfSchemaName(String expectedSignature, Map<String, Schema> schemas) {
		for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
			Schema value = entry.getValue();
			if (value == null || value.getOneOf() == null || value.getOneOf().isEmpty()) {
				continue;
			}
			if (expectedSignature.equals(buildOneOfSignature(value.getOneOf()))) {
				return entry.getKey();
			}
		}
		return null;
	}

	private String buildOneOfSignature(List<Schema> oneOfSchemas) {
		return oneOfSchemas.stream()
				.map(Schema::get$ref)
				.filter(Objects::nonNull)
				.reduce((left, right) -> left + "|" + right)
				.orElse("");
	}

	private String extractSchemaName(String ref) {
		if (ref != null && ref.startsWith(Components.COMPONENTS_SCHEMAS_REF)) {
			return ref.substring(Components.COMPONENTS_SCHEMAS_REF.length());
		}
		return "Polymorphic";
	}

	private String resolveOneOfSchemaName(String candidateName, Map<String, Schema> schemas) {
		String resolvedName = candidateName;
		int suffix = 2;
		while (schemas.containsKey(resolvedName)) {
			resolvedName = candidateName + suffix++;
		}
		return resolvedName;
	}

	/**
	 * Find composed schemas recursively.
	 *
	 * @param ref     the reference of the schema
	 * @param schemas the collection of schemas to search in
	 * @return the list of composed schemas
	 */
	private List<Schema> findComposedSchemas(String ref, Collection<Schema> schemas) {
		List<Schema> composedSchemas = schemas.stream()
				.filter(ComposedSchema.class::isInstance)
				.map(ComposedSchema.class::cast)
				.filter(s -> s.getAllOf() != null)
				.filter(s -> s.getAllOf().stream().anyMatch(s2 -> ref.equals(s2.get$ref())))
				.map(s -> new Schema().$ref(AnnotationsUtils.COMPONENTS_REF + s.getName()))
				.toList();

		List<Schema> resultSchemas = new ArrayList<>(composedSchemas);

		for (Schema childSchema : composedSchemas) {
			String childSchemaRef = childSchema.get$ref();
			resultSchemas.addAll(findComposedSchemas(childSchemaRef, schemas));
		}

		return resultSchemas;
	}

	/**
	 * Is concrete class boolean.
	 *
	 * @param type the type
	 * @return the boolean
	 */
	private boolean isConcreteClass(AnnotatedType type) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		Class<?> clazz = javaType.getRawClass();
		return !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface();
	}

	/**
	 * Introspects the properties of the given Java type based on serialization and deserialization configurations.
	 * This method identifies properties present in both JSON serialization and deserialization views,
	 * and pairs them into a list of {@code BeanPropertyBiDefinition}.
	 */
	private List<BeanPropertyBiDefinition> introspectBeanProperties(JavaType javaType) {
		Map<String, BeanPropertyDefinition> forSerializationProps =
				springDocObjectMapper.jsonMapper()
						.getSerializationConfig()
						.introspect(javaType)
						.findProperties()
						.stream()
						.collect(toMap(BeanPropertyDefinition::getName, identity()));
		Map<String, BeanPropertyDefinition> forDeserializationProps =
				springDocObjectMapper.jsonMapper()
						.getDeserializationConfig()
						.introspect(javaType)
						.findProperties()
						.stream()
						.collect(toMap(BeanPropertyDefinition::getName, identity()));

		return forSerializationProps.keySet().stream()
				.map(key -> new BeanPropertyBiDefinition(forSerializationProps.get(key), forDeserializationProps.get(key)))
				.toList();
	}

	/**
	 * A record representing the bi-definition of a bean property, combining both
	 * serialization and deserialization property views.
	 */
	private record BeanPropertyBiDefinition(
			BeanPropertyDefinition forSerialization,
			BeanPropertyDefinition forDeserialization
	) {

		/**
		 * Retrieves an annotation of the specified type from either the serialization or
		 * deserialization property definition (field, getter, setter), returning the first available match.
		 *
		 * @param <A>  the type parameter
		 * @param acls the acls
		 * @return the any annotation
		 */
		public <A extends Annotation> A getAnyAnnotation(Class<A> acls) {
			A anyForSerializationAnnotation = getAnyAnnotation(forSerialization, acls);
			A anyForDeserializationAnnotation = getAnyAnnotation(forDeserialization, acls);

			return anyForSerializationAnnotation != null ? anyForSerializationAnnotation : anyForDeserializationAnnotation;
		}

		/**
		 * Checks if any annotation of the specified type exists across serialization
		 * or deserialization property definitions.
		 *
		 * @param <A>  the type parameter
		 * @param acls the acls
		 * @return the boolean
		 */
		public <A extends Annotation> boolean isAnyAnnotated(Class<A> acls) {
			return getAnyAnnotation(acls) != null;
		}

		/**
		 * Type determined from the primary member for the property being built.
		 *
		 * @return the primary type
		 */
		public JavaType getPrimaryType() {
			JavaType forSerializationType = null;
			if (forSerialization != null) {
				forSerializationType = forSerialization.getPrimaryType();
			}

			JavaType forDeserializationType = null;
			if (forDeserialization != null) {
				forDeserializationType = forDeserialization.getPrimaryType();
			}

			if (forSerializationType != null && forDeserializationType != null && forSerializationType != forDeserializationType) {
				throw new IllegalStateException("The property " + forSerialization.getName() + " has different types for serialization and deserialization: "
						+ forSerializationType + " and " + forDeserializationType);
			}

			return forSerializationType != null ? forSerializationType : forDeserializationType;
		}

		private <A extends Annotation> A getAnyAnnotation(BeanPropertyDefinition prop, Class<A> acls) {
			if (prop == null) {
				return null;
			}

			if (prop.getField() != null) {
				return prop.getField().getAnnotation(acls);
			}
			if (prop.getGetter() != null) {
				return prop.getGetter().getAnnotation(acls);
			}
			if (prop.getSetter() != null) {
				return prop.getSetter().getAnnotation(acls);
			}

			return null;
		}
	}

	private boolean hasAllOf(Schema<?> schema) {
		return schema.getAllOf() != null && !schema.getAllOf().isEmpty();
	}
}
