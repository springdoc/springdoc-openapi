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

package test.org.springdoc.api.v31.app11

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController {

	@Operation(
		summary = "foo api",
		description = """
        this api is foo
        
        #### Custom exception case
        | Http Status | Error Code  | Error Message | Error Data | Remark   |
        |-------------|-------------|--------------|------------|-----------|
        | 403         | NO_PERMISSION       |This request is only available to administrators.  |            |           |
        | 400         | STORE_NOT_FOUND       |Store not found.   |            |           |
        """
	)
	@GetMapping("/foo/trim-kotlin-indent")
	fun readFoo(@RequestParam name: String?) = FooResponse("Hello ${name ?: "world"}")

}

data class FooResponse(
	val name: String,
)

@RestController
class ExampleController2 {

	@GetMapping("/foo/trim-kotlin-indent/schema")
	fun readFoo(
		@RequestBody request: FooRequestWithSchema,
	) = FooResponseWithSchema(
		name = "Hello ${request.age ?: "world"}",
		subFoo = SubFooResponseWithSchema(subName = "sub foo name"),
	)
}

@Schema(
	name = "foo request",
	description = """
    foo request class description
    with kotlin indent
    """
)
data class FooRequestWithSchema(
	@Schema(
		name = "age",
		description = """
        foo request field with kotlin indent
        """
	)
	val age: Int,
)

@Schema(
	name = "foo response",
	description = """
    foo response class description
    with kotlin indent
    """
)
data class FooResponseWithSchema(
	@Schema(
		name = "name",
		description = """
        foo response fields with kotlin indent
        """
	)
	val name: String,
	val subFoo: SubFooResponseWithSchema
)

@Schema(
	name = "sub foo response",
	description = """
    sub foo response class description
    with kotlin indent
    """
)
data class SubFooResponseWithSchema(
	@Schema(
		name = "subName",
		description = """
        sub foo response fields with kotlin indent
        """
	)
	val subName: String,
)
