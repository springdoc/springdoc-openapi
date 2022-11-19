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

package org.springdoc.api.mixins;

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
 * The interface Sorted schema mixin 31.
 * @author bnasslashen
 */
@JsonPropertyOrder(value = { "type", "format" }, alphabetic = true)
public interface SortedSchemaMixin31 {

	/**
	 * Gets extensions.
	 *
	 * @return the extensions
	 */
	@JsonAnyGetter
	@JsonPropertyOrder(alphabetic = true)
	Map<String, Object> getExtensions();

	/**
	 * Gets json schema.
	 *
	 * @return the json schema
	 */
	@JsonIgnore
	Map<String, Object> getJsonSchema();

	/**
	 * Gets nullable.
	 *
	 * @return the nullable
	 */
	@JsonIgnore
	Boolean getNullable();

	/**
	 * Gets exclusive minimum.
	 *
	 * @return the exclusive minimum
	 */
	@JsonIgnore
	Boolean getExclusiveMinimum();

	/**
	 * Gets exclusive maximum.
	 *
	 * @return the exclusive maximum
	 */
	@JsonIgnore
	Boolean getExclusiveMaximum();

	/**
	 * Gets exclusive minimum value.
	 *
	 * @return the exclusive minimum value
	 */
	@JsonProperty("exclusiveMinimum")
	BigDecimal getExclusiveMinimumValue();

	/**
	 * Gets exclusive maximum value.
	 *
	 * @return the exclusive maximum value
	 */
	@JsonProperty("exclusiveMaximum")
	BigDecimal getExclusiveMaximumValue();

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	@JsonIgnore
	String getType();

	/**
	 * Gets types.
	 *
	 * @return the types
	 */
	@JsonProperty("type")
	Set<String> getTypes();

	/**
	 * Add extension.
	 *
	 * @param name the name
	 * @param value the value
	 */
	@JsonAnySetter
	void addExtension(String name, Object value);

	/**
	 * Gets example set flag.
	 *
	 * @return the example set flag
	 */
	@JsonIgnore
	boolean getExampleSetFlag();

	/**
	 * Gets example.
	 *
	 * @return the example
	 */
	@JsonInclude(JsonInclude.Include.CUSTOM)
	Object getExample();

	/**
	 * Gets json schema.
	 *
	 * @return the json schema
	 */
	@JsonIgnore
	Object getJsonSchemaImpl();

}