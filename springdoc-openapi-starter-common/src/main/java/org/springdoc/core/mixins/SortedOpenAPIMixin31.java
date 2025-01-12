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

package org.springdoc.core.mixins;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.oas.models.Paths;

/**
 * The interface Sorted open api mixin 31.
 *
 * @author bnasslashen
 */
@JsonPropertyOrder(value = {"openapi", "info", "externalDocs", "servers", "security", "tags", "paths", "components", "webhooks"}, alphabetic = true)
public interface SortedOpenAPIMixin31 {

	/**
	 * Gets extensions.
	 *
	 * @return the extensions
	 */
	@JsonAnyGetter
	@JsonInclude(value = Include.ALWAYS)
	@JsonPropertyOrder(alphabetic = true)
	Map<String, Object> getExtensions();

	/**
	 * Add extension.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	@JsonAnySetter
	void addExtension(String name, Object value);

	/**
	 * Gets paths.
	 *
	 * @return the paths
	 */
	@JsonSerialize(using = PathsSerializer.class)
	Paths getPaths();
}