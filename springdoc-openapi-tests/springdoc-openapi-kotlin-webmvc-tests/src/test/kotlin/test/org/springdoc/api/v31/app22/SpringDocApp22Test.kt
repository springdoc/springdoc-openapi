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

package test.org.springdoc.api.v31.app22

import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

class SpringDocApp22Test : AbstractKotlinSpringDocMVCTest() {
	@SpringBootApplication
	class DemoApplication
}



@RestController
@RequestMapping("/test")
@SpringBootApplication
class DemoApp {
	@GetMapping
	fun getTestData(): Foo = Foo(bar = Bar(baz = "test"))
}

data class Foo(
	@field:JsonUnwrapped
	val bar: Bar,
)

data class Bar(
	@Deprecated("This is deprecated")
	val baz: String,
)
