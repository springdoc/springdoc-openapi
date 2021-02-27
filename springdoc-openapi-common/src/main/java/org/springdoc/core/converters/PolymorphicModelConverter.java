/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core.converters;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;

/**
 * The type Polymorphic model converter.
 * @author bnasslahsen
 */
public class PolymorphicModelConverter implements ModelConverter {
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
			if (resolvedSchema == null || resolvedSchema.get$ref() == null) return resolvedSchema;
			return composePolymorphicSchema(type, resolvedSchema, context.getDefinedModels().values());
		}
		return null;
	}

	/**
	 * Compose polymorphic schema schema.
	 *
	 * @param type the type
	 * @param schema the schema
	 * @param schemas the schemas
	 * @return the schema
	 */
	private Schema composePolymorphicSchema(AnnotatedType type, Schema schema, Collection<Schema> schemas) {
		String ref = schema.get$ref();
		List<Schema> composedSchemas = schemas.stream()
				.filter(ComposedSchema.class::isInstance)
				.map(ComposedSchema.class::cast)
				.filter(s -> s.getAllOf() != null)
				.filter(s -> s.getAllOf().stream().anyMatch(s2 -> ref.equals(s2.get$ref())))
				.map(s -> new Schema().$ref(AnnotationsUtils.COMPONENTS_REF + s.getName()))
				.collect(Collectors.toList());
		if (composedSchemas.isEmpty()) return schema;

		ComposedSchema result = new ComposedSchema();
		if (isConcreteClass(type)) result.addOneOfItem(schema);
		composedSchemas.forEach(result::addOneOfItem);
		return result;
	}

	/**
	 * Is concrete class boolean.
	 *
	 * @param type the type
	 * @return the boolean
	 */
	private boolean isConcreteClass(AnnotatedType type) {
		JavaType javaType = Json.mapper().constructType(type.getType());
		Class<?> clazz = javaType.getRawClass();
		return !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface();
	}
}
