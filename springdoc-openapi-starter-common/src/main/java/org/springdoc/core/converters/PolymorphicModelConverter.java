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

package org.springdoc.core.converters;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

/**
 * The type Polymorphic model converter.
 * @author bnasslahsen
 */
public class PolymorphicModelConverter implements ModelConverter {

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Polymorphic model converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public PolymorphicModelConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	private static Schema<?> getResolvedSchema(JavaType javaType, Schema<?> resolvedSchema) {
		if (resolvedSchema instanceof ObjectSchema && resolvedSchema.getProperties() != null) {
			if (resolvedSchema.getProperties().containsKey(javaType.getRawClass().getName())){
				resolvedSchema = resolvedSchema.getProperties().get(javaType.getRawClass().getName());
			}
			else if (resolvedSchema.getProperties().containsKey(javaType.getRawClass().getSimpleName())){
				resolvedSchema = resolvedSchema.getProperties().get(javaType.getRawClass().getSimpleName());
			}
		}
		return resolvedSchema;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			if (chain.hasNext()) {
				Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
				resolvedSchema = getResolvedSchema(javaType, resolvedSchema);
				if (resolvedSchema == null || resolvedSchema.get$ref() == null)
					return resolvedSchema;
				return composePolymorphicSchema(type, resolvedSchema, context.getDefinedModels().values());
			}
		}
		return null;
	}

	/**
	 * Compose polymorphic schema.
	 *
	 * @param type the type
	 * @param schema the schema
	 * @param schemas the schemas
	 * @return the schema
	 */
	private Schema composePolymorphicSchema(AnnotatedType type, Schema schema, Collection<Schema> schemas) {
		String ref = schema.get$ref();
		List<Schema> composedSchemas = findComposedSchemas(ref, schemas);

		if (composedSchemas.isEmpty()) return schema;

		ComposedSchema result = new ComposedSchema();
		if (isConcreteClass(type)) result.addOneOfItem(schema);
		composedSchemas.forEach(result::addOneOfItem);
		return result;
	}

	/**
	 * Find composed schemas recursively.
	 *
	 * @param ref the reference of the schema
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
}
