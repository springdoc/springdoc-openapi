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

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.models.media.Schema;

/**
 * The interface Sorted schema mixin.
 *
 * @author bnasslashen
 */
@JsonPropertyOrder(value = { "type", "format" }, alphabetic = true)
public interface SortedSchemaMixin {

	/**
	 * Gets extensions.
	 *
	 * @return the extensions
	 */
	@JsonAnyGetter
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
	@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
	Object getExample();

	/**
	 * Gets json schema.
	 *
	 * @return the json schema
	 */
	@JsonIgnore
	Map<String, Object> getJsonSchema();

	/**
	 * Gets exclusive minimum value.
	 *
	 * @return the exclusive minimum value
	 */
	@JsonIgnore
	BigDecimal getExclusiveMinimumValue();

	/**
	 * Gets exclusive maximum value.
	 *
	 * @return the exclusive maximum value
	 */
	@JsonIgnore
	BigDecimal getExclusiveMaximumValue();

	/**
	 * Gets pattern properties.
	 *
	 * @return the pattern properties
	 */
	@JsonIgnore
	Map<String, Schema> getPatternProperties();

	/**
	 * Gets contains.
	 *
	 * @return the contains
	 */
	@JsonIgnore
	Schema getContains();

	/**
	 * Get id string.
	 *
	 * @return the string
	 */
	@JsonIgnore
	String get$id();

	/**
	 * Get anchor string.
	 *
	 * @return the string
	 */
	@JsonIgnore
	String get$anchor();

	/**
	 * Get schema string.
	 *
	 * @return the string
	 */
	@JsonIgnore
	String get$schema();

	/**
	 * Gets types.
	 *
	 * @return the types
	 */
	@JsonIgnore
	Set<String> getTypes();

	/**
	 * Gets json schema.
	 *
	 * @return the json schema
	 */
	@JsonIgnore
	Object getJsonSchemaImpl();

}