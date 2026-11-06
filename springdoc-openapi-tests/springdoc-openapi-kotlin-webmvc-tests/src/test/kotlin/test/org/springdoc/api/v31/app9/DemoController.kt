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

package test.org.springdoc.api.v31.app9

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/demo")
class DocumentsApiController {

	@GetMapping
	suspend fun getDocuments(
		request: DemoRequest
	): DemoDto = DemoDto(42)
}

data class DemoDto(
	var id: Long,
)

class DemoRequest(

	@field:Schema(required = true, defaultValue = "a default value")
	val requiredNullableDefault: String?,

	@field:Schema(required = true)
	val requiredNullableNoDefault: String?,

	@field:Schema(required = true, defaultValue = "a default value")
	val requiredNoNullableDefault: String,

	@field:Schema(required = true)
	val requiredNoNullableNoDefault: String,

	@field:Schema(
		requiredMode = Schema.RequiredMode.REQUIRED,
		defaultValue = "a default value"
	)
	val requiredNullableDefault1: String?,

	@field:Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	val requiredNullableNoDefault1: String?,

	@field:Schema(
		requiredMode = Schema.RequiredMode.REQUIRED,
		defaultValue = "a default value"
	)
	val requiredNoNullableDefault1: String,

	@field:Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	val requiredNoNullableNoDefault1: String,

	@field:Schema(
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		defaultValue = "a default value"
	)
	val noRequiredNullableDefault2: String?,

	@field:Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	val noRequiredNullableNoDefault2: String?,

	@field:Schema(
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		defaultValue = "a default value"
	)
	val noRequiredNoNullableDefault2: String,

	@field:Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	val noRequiredNoNullableNoDefault2: String,

	@field:Schema(defaultValue = "a default value")
	val noRequiredNullableDefault: String?,

	@field:Schema
	val noRequiredNullableNoDefault: String?,

	@field:Schema(defaultValue = "a default value")
	val noRequiredNoNullableDefault: String,

	@field:Schema
	val noRequiredNoNullableNoDefault: String,

	)
