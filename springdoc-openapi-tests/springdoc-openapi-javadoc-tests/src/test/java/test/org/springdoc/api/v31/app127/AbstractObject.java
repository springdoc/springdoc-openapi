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

package test.org.springdoc.api.v31.app127;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The type Abstract object.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = ConcreteObjectA.class, name = "Type A")
})
public abstract class AbstractObject {

	/**
	 * The Type.
	 */
	private final ConcreteType type;

	/**
	 * The Name.
	 */
	private final String name;

	/**
	 * Instantiates a new Abstract object.
	 *
	 * @param type the type
	 * @param name the name
	 */
	protected AbstractObject(ConcreteType type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public ConcreteType getType() {
		return type;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
