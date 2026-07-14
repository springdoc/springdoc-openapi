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
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.converters;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

/**
 * Repairs schema property names that swagger-core leaves in their raw (untranslated) form
 * when a Jackson {@code PropertyNamingStrategy} (for example {@code SNAKE_CASE}) is in
 * effect.
 *
 * <p>
 * swagger-core's {@code ModelResolver} carries a long-standing workaround
 * (<a href="https://github.com/swagger-api/swagger-core/issues/415">swagger-core#415</a>)
 * that overwrites the Jackson-translated property name with the raw member name whenever
 * that member name starts with {@code get}/{@code is} followed by a lower-case character.
 * For Java records the accessor is named exactly like the component (e.g.
 * {@code issuanceDate()}), so a component such as {@code issuanceDate} is clobbered back
 * to camelCase while sibling properties like {@code familyName} convert correctly. This
 * converter restores the translated name.
 * </p>
 *
 * <p>
 * The mapper used by the active {@code ModelResolver} is consulted so that the fix is
 * self-consistent: it only renames a property when Jackson actually maps its internal
 * name to a different external name and swagger-core emitted the internal name.
 * </p>
 *
 * See: <a href=
 * "https://github.com/springdoc/springdoc-openapi/issues/3293">springdoc-openapi#3293</a>
 *
 * @author bnasslahsen
 */
public class PropertyNamingStrategyConverter implements ModelConverter {

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Property naming strategy converter.
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public PropertyNamingStrategyConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (!chain.hasNext())
			return null;
		Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);

		ObjectMapper mapper = resolverObjectMapper();
		if (mapper == null)
			return resolvedSchema;

		Schema<?> targetSchema = resolvedSchema;
		if (resolvedSchema != null && resolvedSchema.get$ref() != null)
			targetSchema = context.getDefinedModels()
				.get(resolvedSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length()));

		if (targetSchema == null || targetSchema.getProperties() == null)
			return resolvedSchema;

		JavaType javaType = mapper.constructType(type.getType());
		if (javaType == null || javaType.getRawClass().getPackageName().startsWith("java."))
			return resolvedSchema;

		renameClobberedProperties(targetSchema, mapper, javaType);
		return resolvedSchema;
	}

	/**
	 * Renames properties that swagger-core emitted under their raw member name back to
	 * the name Jackson computed from the active naming strategy, preserving property
	 * order and the required list.
	 * @param schema the schema to fix
	 * @param mapper the object mapper used by the active model resolver
	 * @param javaType the resolved java type
	 */
	private void renameClobberedProperties(Schema<?> schema, ObjectMapper mapper, JavaType javaType) {
		BeanDescription beanDescription;
		try {
			beanDescription = mapper.getSerializationConfig().introspect(javaType);
		}
		catch (Exception e) {
			return;
		}

		Map<String, String> renames = new LinkedHashMap<>();
		for (BeanPropertyDefinition property : beanDescription.findProperties()) {
			String externalName = property.getName();
			String internalName = property.getInternalName();
			if (externalName != null && !externalName.equals(internalName)
					&& schema.getProperties().containsKey(internalName)
					&& !schema.getProperties().containsKey(externalName)) {
				renames.put(internalName, externalName);
			}
		}
		if (renames.isEmpty())
			return;

		Map<String, Schema> renamedProperties = new LinkedHashMap<>();
		schema.getProperties().forEach((name, value) -> renamedProperties.put(renames.getOrDefault(name, name), value));
		schema.setProperties(renamedProperties);

		List<String> required = schema.getRequired();
		if (required != null)
			required.replaceAll(name -> renames.getOrDefault(name, name));
	}

	/**
	 * Returns the {@link ObjectMapper} of the active swagger-core {@code ModelResolver},
	 * which is the mapper that applied (or did not apply) the naming strategy while
	 * generating the schema.
	 * @return the object mapper, or {@code null} if none could be located
	 */
	private ObjectMapper resolverObjectMapper() {
		for (ModelConverter converter : ModelConverters.getInstance(springDocObjectMapper.isOpenapi31())
			.getConverters()) {
			if (converter instanceof ModelResolver modelResolver)
				return modelResolver.objectMapper();
		}
		return null;
	}

}
