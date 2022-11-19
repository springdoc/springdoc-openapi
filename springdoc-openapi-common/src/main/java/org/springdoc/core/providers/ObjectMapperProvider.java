/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.providers;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.api.mixins.SortedOpenAPIMixin;
import org.springdoc.api.mixins.SortedOpenAPIMixin31;
import org.springdoc.api.mixins.SortedSchemaMixin;
import org.springdoc.api.mixins.SortedSchemaMixin31;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

/**
 * The type Spring doc object mapper provider.
 */
public class ObjectMapperProvider extends ObjectMapperFactory {

	/**
	 * The Json mapper.
	 */
	private final ObjectMapper jsonMapper;

	/**
	 * The Yaml mapper.
	 */
	private final ObjectMapper yamlMapper;

	/**
	 * Instantiates a new Spring doc object mapper.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public ObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {
		OpenApiVersion openApiVersion = springDocConfigProperties.getApiDocs().getVersion();
		if (openApiVersion == OpenApiVersion.OPENAPI_3_1) {
			jsonMapper = Json31.mapper();
			yamlMapper = Yaml31.mapper();
		}
		else {
			jsonMapper = Json.mapper();
			yamlMapper = Yaml.mapper();
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
			objectMapper = ObjectMapperFactory.createJson31();
		else
			objectMapper = ObjectMapperFactory.createJson();

		if (springDocConfigProperties.isWriterWithOrderByKeys())
			sortOutput(objectMapper, springDocConfigProperties);

		return objectMapper;
	}

	/**
	 * Sort output.
	 *
	 * @param objectMapper the object mapper
	 */
	public static void sortOutput(ObjectMapper objectMapper, SpringDocConfigProperties springDocConfigProperties) {
		objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		if (OpenApiVersion.OPENAPI_3_1 == springDocConfigProperties.getApiDocs().getVersion()) {
			objectMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin31.class);
			objectMapper.addMixIn(Schema.class, SortedSchemaMixin31.class);
		}
		else {
			objectMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin.class);
			objectMapper.addMixIn(Schema.class, SortedSchemaMixin.class);
		}
	}

	/**
	 * Mapper object mapper.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper jsonMapper() {
		return jsonMapper;
	}

	/**
	 * Yaml mapper object mapper.
	 *
	 * @return the object mapper
	 */
	public ObjectMapper yamlMapper() {
		return yamlMapper;
	}

}
