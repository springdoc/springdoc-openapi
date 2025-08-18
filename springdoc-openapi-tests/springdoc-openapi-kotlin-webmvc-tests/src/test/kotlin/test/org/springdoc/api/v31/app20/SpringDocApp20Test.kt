/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.v31.app20

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.springdoc.core.annotations.ParameterObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest


class SpringDocApp20Test : AbstractKotlinSpringDocMVCTest() {
	@SpringBootApplication
	class DemoApplication
}
@RestController("/")
class FooController {

	@GetMapping("/foo")
	fun getFoo(@Valid fooParameters: FooParameters): String {
		return "Ok"
	}
}

@ParameterObject
data class FooParameters(
	@RequestParam(name = "bar", required = false, defaultValue = DEFAULT_BAR.toString())
	@field:Parameter(schema = Schema(
		minimum = "1",
		type = "integer",
		defaultValue = DEFAULT_BAR.toString()
	)
	)
	@field:Min(1)
	val bar: Int = DEFAULT_BAR,
	@RequestParam(name = "baz", required = false, defaultValue = DEFAULT_BAZ.toString())
	@field:Parameter(schema = Schema(minimum = "1", maximum = "200", type = "integer", defaultValue = DEFAULT_BAZ.toString()))
	@field:Min(1)
	@field:Max(200)
	val baz: Int = DEFAULT_BAZ
) {
	companion object {
		const val DEFAULT_BAR = 1
		const val DEFAULT_BAZ = 20
	}
}