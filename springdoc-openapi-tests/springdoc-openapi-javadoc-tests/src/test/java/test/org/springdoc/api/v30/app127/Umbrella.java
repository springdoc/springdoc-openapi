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

package test.org.springdoc.api.v30.app127;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Umbrella.
 */
class Umbrella {

	/**
	 * The Object.
	 */
	@Schema(description = "This reference to abstract class causes weird YAML tag to be added", anyOf = ConcreteObjectA.class)
	private final AbstractObject object;

	/**
	 * Instantiates a new Umbrella.
	 *
	 * @param object the object
	 */
	public Umbrella(AbstractObject object) {
		this.object = object;
	}

	/**
	 * Gets object.
	 *
	 * @return the object
	 */
	public AbstractObject getObject() {
		return object;
	}
}
