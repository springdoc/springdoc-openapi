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

package org.springdoc.core.mixins;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import org.springdoc.core.deserializers.TypeSetDeserializer;

/**
 * The type Schema type mixin. Makes the OpenAPI 3.1 {@code type} readable back into the
 * {@code types} set, whichever of the two serialized forms it takes.
 * <p>
 * It extends the swagger-core mixin so that it can be registered on {@code Schema} itself
 * without losing the serialization it declares, which is what makes the deserializer apply
 * to every schema subclass rather than to {@code JsonSchema} alone.
 *
 * @author Mattias-Sehlstedt
 */
public abstract class SchemaTypeMixin extends Schema31Mixin {

	/**
	 * Sets types.
	 *
	 * @param types the types
	 */
	@JsonProperty("type")
	@JsonDeserialize(using = TypeSetDeserializer.class)
	public abstract void setTypes(Set<String> types);

}
