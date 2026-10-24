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

package org.springdoc.core.providers;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.MapperCustomizer;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.mixins.SchemaTypeMixin;
import org.springdoc.core.mixins.SortedOpenAPIMixin;
import org.springdoc.core.mixins.SortedOpenAPIMixin31;
import org.springdoc.core.mixins.SortedSchemaMixin;
import org.springdoc.core.mixins.SortedSchemaMixin31;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

/**
 * The type Spring doc object mapper provider.
 * <p>
 * Jackson 3 mappers are immutable: the mappers of swagger-core are derived, instead of being modified, and the
 * derived mappers are rebuilt when the swagger-core mapper configuration changes (see
 * {@link ObjectMapperFactory#generation()}).
 *
 * @author bnasslahsen
 */
public class ObjectMapperProvider extends ObjectMapperFactory {

	/**
	 * The swagger-core customizers registering the modules, by module registration id.
	 */
	private static final Map<Object, MapperCustomizer> REGISTERED_MODULES = new ConcurrentHashMap<>();

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The swagger-core mapper configuration generation the mappers were built for.
	 */
	private long generation = -1;

	/**
	 * The Json mapper.
	 */
	private ObjectMapper jsonMapper;

	/**
	 * The Yaml mapper.
	 */
	private ObjectMapper yamlMapper;

	/**
	 * The Json mapper used to write the OpenAPI description.
	 */
	private ObjectMapper jsonWriterMapper;

	/**
	 * The Yaml mapper used to write the OpenAPI description.
	 */
	private ObjectMapper yamlWriterMapper;

	/**
	 * Instantiates a new Spring doc object mapper.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public ObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
		if (springDocConfigProperties.getApiDocs().getVersion() == OpenApiVersion.OPENAPI_3_1) {
			if (springDocConfigProperties.isUseArbitrarySchemas()) {
				System.setProperty(Schema.USE_ARBITRARY_SCHEMA_PROPERTY, "true");
			}
			if (springDocConfigProperties.isExplicitObjectSchema()) {
				System.setProperty(Schema.EXPLICIT_OBJECT_SCHEMA_PROPERTY, "true");
			}
		}
	}

	/**
	 * Create json object mapper.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the object mapper
	 */
	public static ObjectMapper createJson(SpringDocConfigProperties springDocConfigProperties) {
		OpenApiVersion openApiVersion = springDocConfigProperties.getApiDocs().getVersion();
		ObjectMapper objectMapper;
		if (openApiVersion == OpenApiVersion.OPENAPI_3_1)
			objectMapper = ObjectMapperFactory.createJson31().rebuild().addMixIn(Schema.class, SchemaTypeMixin.class).build();
		else
			objectMapper = ObjectMapperFactory.createJson();
		if (springDocConfigProperties.isWriterWithOrderByKeys())
			objectMapper = sortOutput(objectMapper, springDocConfigProperties);
		return objectMapper;
	}

	/**
	 * Registers modules with every mapper built by swagger-core, including the ones swagger-core uses to introspect
	 * the models, and the mappers of this provider. Each module is registered once.
	 *
	 * @param modules the modules
	 */
	public static void registerModules(JacksonModule... modules) {
		for (JacksonModule module : modules) {
			REGISTERED_MODULES.computeIfAbsent(module.getRegistrationId(), registrationId -> {
				MapperCustomizer customizer = (builder, target) -> builder.addModule(module);
				ObjectMapperFactory.addCustomizer(customizer);
				return customizer;
			});
		}
	}

	/**
	 * Unregisters modules previously registered with {@link #registerModules(JacksonModule...)}.
	 *
	 * @param modules the modules
	 * @return {@code true} if at least one of the modules was registered
	 */
	public static boolean unregisterModules(JacksonModule... modules) {
		boolean unregistered = false;
		for (JacksonModule module : modules) {
			MapperCustomizer customizer = REGISTERED_MODULES.remove(module.getRegistrationId());
			if (customizer != null)
				unregistered |= ObjectMapperFactory.removeCustomizer(customizer);
		}
		return unregistered;
	}

	/**
	 * Sort output.
	 *
	 * @param objectMapper              the object mapper
	 * @param springDocConfigProperties the spring doc config properties
	 * @return a copy of the object mapper, sorting the properties and map entries
	 */
	public static ObjectMapper sortOutput(ObjectMapper objectMapper, SpringDocConfigProperties springDocConfigProperties) {
		boolean openapi31 = OpenApiVersion.OPENAPI_3_1 == springDocConfigProperties.getApiDocs().getVersion();
		return objectMapper.rebuild()
				.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
				.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
				.addMixIn(OpenAPI.class, openapi31 ? SortedOpenAPIMixin31.class : SortedOpenAPIMixin.class)
				.addMixIn(Schema.class, openapi31 ? SortedSchemaMixin31.class : SortedSchemaMixin.class)
				.build();
	}

	/**
	 * Json mapper object mapper.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper jsonMapper() {
		refresh();
		return jsonMapper;
	}

	/**
	 * Yaml mapper object mapper.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper yamlMapper() {
		refresh();
		return yamlMapper;
	}

	/**
	 * Json mapper used to write the OpenAPI description, following the springdoc writer properties.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper jsonWriterMapper() {
		refresh();
		return jsonWriterMapper;
	}

	/**
	 * Yaml mapper used to write the OpenAPI description, following the springdoc writer properties.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper yamlWriterMapper() {
		refresh();
		return yamlWriterMapper;
	}

	/**
	 * Is openapi 31 boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOpenapi31() {
		return springDocConfigProperties.isOpenapi31();
	}

	/**
	 * Builds the mappers again when the swagger-core mapper configuration changed, for example after a module was
	 * registered with {@link ObjectMapperFactory#addModule}.
	 */
	private synchronized void refresh() {
		long currentGeneration = ObjectMapperFactory.generation();
		if (currentGeneration == generation)
			return;
		if (springDocConfigProperties.getApiDocs().getVersion() == OpenApiVersion.OPENAPI_3_1) {
			jsonMapper = Json31.mapper().rebuild().addMixIn(Schema.class, SchemaTypeMixin.class).build();
			yamlMapper = Yaml31.mapper();
		}
		else {
			jsonMapper = Json.mapper();
			yamlMapper = Yaml.mapper();
		}
		ObjectMapper yamlWriter = ((YAMLMapper) yamlMapper).rebuild().disable(YAMLWriteFeature.USE_NATIVE_TYPE_ID).build();
		if (springDocConfigProperties.isWriterWithOrderByKeys()) {
			jsonWriterMapper = sortOutput(jsonMapper, springDocConfigProperties);
			yamlWriterMapper = sortOutput(yamlWriter, springDocConfigProperties);
		}
		else {
			jsonWriterMapper = jsonMapper;
			yamlWriterMapper = yamlWriter;
		}
		generation = currentGeneration;
	}
}
