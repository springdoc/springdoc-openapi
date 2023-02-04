package test.org.springdoc.api.app9

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/demo")
class DocumentsApiController {

    @GetMapping
    suspend fun getDocuments( request: DemoRequest
    ): DemoDto = DemoDto(42)
}

data class DemoDto(
    var id: Long,
)

class DemoRequest {
	@field:Schema(required = false, description = "Should not be required")
	val nonNullableWithDefault: String = "a default value"

}
