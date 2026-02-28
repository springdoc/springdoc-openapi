/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.v31.app21

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

class SpringDocApp21Test : AbstractKotlinSpringDocMVCTest() {
	@SpringBootApplication
	class DemoApplication
}


@RestController
class HelloController {
	@GetMapping("/hello")
	fun hello(@Valid baseSearchParameters: BaseSearchParameters) =
		ResponseEntity.ok("Hello, World!")
}

@ParameterObject
data class BaseSearchParameters(
	val filters: Filters = Filters()
)

@ParameterObject
data class Filters(
	@field:Parameter(
		description = "Description .",
		explode = Explode.FALSE,
		array = ArraySchema(schema = Schema(type = "string"))
	)
	var brand: Set<String> = emptySet(),

	@field:Parameter(
		description = "Description ..",
		explode = Explode.FALSE,
		array = ArraySchema(schema = Schema(type = "string"))
	)
	var service: Set<String> = emptySet(),

	@field:Parameter(
		description = "Description ...",
		explode = Explode.FALSE,
		array = ArraySchema(schema = Schema(type = "string"))
	)
	var productGroup: Set<String> = emptySet(),
)