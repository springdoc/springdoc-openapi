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

package org.springdoc.core.mixins;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author bnasslashen
 */
@JsonPropertyOrder(value = {"type", "format"}, alphabetic = true)
public interface SortedSchemaMixin31 {

	@JsonAnyGetter
	@JsonPropertyOrder(alphabetic = true)
	Map<String, Object> getExtensions();

	@JsonIgnore
	Map<String, Object> getJsonSchema();

	@JsonIgnore
	Boolean getNullable();

	@JsonIgnore
	Boolean getExclusiveMinimum();

	@JsonIgnore
	Boolean getExclusiveMaximum();

	@JsonProperty("exclusiveMinimum")
	BigDecimal getExclusiveMinimumValue();

	@JsonProperty("exclusiveMaximum")
	BigDecimal getExclusiveMaximumValue();

	@JsonIgnore
	String getType();

	@JsonProperty("type")
	Set<String> getTypes();

	@JsonAnySetter
	void addExtension(String name, Object value);

	@JsonIgnore
	boolean getExampleSetFlag();

	@JsonInclude(JsonInclude.Include.CUSTOM)
	Object getExample();

	@JsonIgnore
	Object getJsonSchemaImpl();

}