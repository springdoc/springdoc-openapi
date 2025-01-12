/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.providers.ObjectMapperProvider;

import static org.springdoc.core.utils.SpringDocUtils.handleSchemaTypes;

/**
 * The type Additional models converter.
 *
 * @author bnasslahsen
 */
public class AdditionalModelsConverter implements ModelConverter {

	/**
	 * The constant modelToClassMap.
	 */
	private static final Map<Class, Class> modelToClassMap = new HashMap<>();

	/**
	 * The constant modelToSchemaMap.
	 */
	private static final Map<Class, Schema> modelToSchemaMap = new HashMap<>();

	/**
	 * The constant paramObjectReplacementMap.
	 */
	private static final Map<Class, Class> paramObjectReplacementMap = new HashMap<>();

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalModelsConverter.class);

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Additional models converter.
	 *
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public AdditionalModelsConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	/**
	 * Replace with class.
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void replaceWithClass(Class source, Class target) {
		modelToClassMap.put(source, target);
	}

	/**
	 * Replace with schema.
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void replaceWithSchema(Class source, Schema target) {
		modelToSchemaMap.put(source, target);
	}

	/**
	 * Replace ParameterObject with class.
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void replaceParameterObjectWithClass(Class source, Class target) {
		paramObjectReplacementMap.put(source, target);
	}

	/**
	 * Gets replacement.
	 *
	 * @param clazz the clazz
	 * @return the replacement
	 */
	public static Class getParameterObjectReplacement(Class clazz) {
		return paramObjectReplacementMap.getOrDefault(clazz, clazz);
	}

	/**
	 * Disable replacement.
	 *
	 * @param clazz the clazz
	 */
	public static void disableReplacement(Class clazz) {
		modelToClassMap.remove(clazz);
	}

	/**
	 * Resolve schema.
	 *
	 * @param type the type
	 * @param context the context
	 * @param chain the chain
	 * @return the schema
	 */
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (modelToSchemaMap.containsKey(cls))
				try {
					Schema schema = modelToSchemaMap.get(cls);
					if(springDocObjectMapper.isOpenapi31())
						handleSchemaTypes(schema);
					return springDocObjectMapper.jsonMapper()
							.readValue(springDocObjectMapper.jsonMapper().writeValueAsString(schema), new TypeReference<Schema>() {});
				}
				catch (JsonProcessingException e) {
					LOGGER.warn("Json Processing Exception occurred: {}", e.getMessage());
				}
			if (modelToClassMap.containsKey(cls))
				type = new AnnotatedType(modelToClassMap.get(cls)).resolveAsRef(true);
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

	/**
	 * Remove from schema map.
	 *
	 * @param clazz the clazz
	 */
	public static void removeFromSchemaMap(Class clazz) {
		modelToSchemaMap.remove(clazz);
	}

	/**
	 * Remove from class map.
	 *
	 * @param clazz the clazz
	 */
	public static void removeFromClassMap(Class clazz) {
		modelToClassMap.remove(clazz);
	}
}