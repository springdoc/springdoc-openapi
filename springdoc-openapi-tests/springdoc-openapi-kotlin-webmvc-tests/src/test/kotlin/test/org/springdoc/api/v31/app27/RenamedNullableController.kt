/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app27

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

data class RenamedRequest(
	val sameName: String? = null,

	@get:JsonProperty("renamed_by_getter")
	val renamedByGetter: String? = null,

	@field:JsonProperty("renamed_by_field")
	val renamedByField: String? = null,

	@get:JsonProperty("renamed_non_null")
	val renamedNonNull: String = "",

	@get:Schema(description = "renamed and described")
	@get:JsonProperty("renamed_with_schema")
	val renamedWithSchema: Int? = null,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SnakeCaseRequest(
	val firstName: String? = null,
	val lastName: String,
)

@RestController
class RenamedNullableController {

	@PostMapping("/renamed")
	fun renamed(@RequestBody request: RenamedRequest) = Unit

	@PostMapping("/snake-case")
	fun snakeCase(@RequestBody request: SnakeCaseRequest) = Unit
}
