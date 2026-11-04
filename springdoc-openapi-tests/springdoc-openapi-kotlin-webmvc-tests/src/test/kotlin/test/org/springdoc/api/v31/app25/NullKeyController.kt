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

package test.org.springdoc.api.v31.app25

import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Reproduces the swagger-core `@JsonUnwrapped` null-property-key issue: when the unwrapped
 * member resolves to a `$ref` (a named component model), swagger-core adds a `null`-keyed
 * entry to the enclosing schema properties, which breaks JSON serialization of the document.
 *
 * Fixed upstream by <a href="https://github.com/swagger-api/swagger-core/pull/5193">PR 5193</a>
 */
@RestController
class NullKeyController {
    @GetMapping("/null-key")
    fun getNullKey(): OuterResponse =
        OuterResponse(
            content = InnerContent("0x1234", ChainType.ETH),
            success = true,
        )
}

data class OuterResponse(
    @get:JsonUnwrapped
    val content: InnerContent,
    val success: Boolean,
)

data class InnerContent(
    val contractAddress: String,
    val chainType: ChainType,
    val nested: NestedObject? = null,
)

data class NestedObject(
    val name: String,
    val description: String? = null,
)

enum class ChainType {
    ETH,
}
