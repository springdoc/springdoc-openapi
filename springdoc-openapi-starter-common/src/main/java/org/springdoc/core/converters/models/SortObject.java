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
package org.springdoc.core.converters.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Sort response.
 *
 * @author bnasslahsen
 */
@ArraySchema(arraySchema = @Schema(implementation = SortObject.class))
public class SortObject {

	/**
	 * The Direction.
	 */
	@JsonProperty
	private String direction;

	/**
	 * The Null handling.
	 */
	@JsonProperty
	private String nullHandling;

	/**
	 * The Ascending.
	 */
	@JsonProperty
	private boolean ascending;

	/**
	 * The Property.
	 */
	@JsonProperty
	private String property;

	/**
	 * The Ignore case.
	 */
	@JsonProperty
	private boolean ignoreCase;

}
