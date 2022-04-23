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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

/**
 * The type Spring doc object mapper provider.
 */
public class ObjectMapperProvider {

	/**
	 * The Json mapper.
	 */
	private final ObjectMapper jsonMapper;

	/**
	 * The Yaml mapper.
	 */
	private final ObjectMapper yamlMapper;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;


	/**
	 * Instantiates a new Spring doc object mapper.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public ObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
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
