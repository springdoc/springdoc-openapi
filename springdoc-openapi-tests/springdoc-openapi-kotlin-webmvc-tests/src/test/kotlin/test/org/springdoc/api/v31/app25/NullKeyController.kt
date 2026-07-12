package test.org.springdoc.api.v31.app25

import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Reproduces the swagger-core `@JsonUnwrapped` null-property-key issue: when the unwrapped
 * member resolves to a `$ref` (a named component model), swagger-core adds a `null`-keyed
 * entry to the enclosing schema properties, which breaks JSON serialization of the document.
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
