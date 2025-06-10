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

package test.org.springdoc.api.v31.app107;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets entity b.
	 *
	 * @return the entity b
	 */
	@GetMapping(path = "/entity-b", produces = { "application/json", "application/xml" })
	public EntityB getEntityB() {
		return new EntityB();
	}

	/**
	 * The type Entity b.
	 */
	class EntityB {

		/**
		 * The Field b.
		 */
		@Schema(required = true)
		@JsonProperty("fieldB")
		private String fieldB;

		/**
		 * The Entity a.
		 */
		@Schema(required = true)
		@JsonProperty("entityA")
		private EntityA entityA;
		//Getters and setters...
	}

	/**
	 * The type Entity a.
	 */
	class EntityA {
		/**
		 * The Field a.
		 */
		@Schema(required = true)
		@JsonProperty("fieldA")
		private String fieldA;
		//Getters and setters...
	}
}

