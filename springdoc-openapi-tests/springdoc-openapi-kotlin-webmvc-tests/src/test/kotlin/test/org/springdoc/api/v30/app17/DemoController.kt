package test.org.springdoc.api.v30.app17

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/demo")
class DocumentsApiController {

    @Valid
    @GetMapping
    suspend fun validOverrideAndOrder(
        @Valid request: ChildBar
    ): DemoDto = DemoDto(42)
}

data class DemoDto(
    var id: Long,
)

@Schema(description = "ParentFoo Request")
open class ParentFoo(
    @field:NotEmpty // use flat-param-object. to remove or merge annotations by use DelegatingMethodParameterCustomizer
    @field:Size(min = 10, max = 20)
    @field:ArraySchema(schema = Schema(description = "wrong: list[minItems = 10, maxItems = 20] wrong: string[minLength = 30, maxLength = 40]"))
    open var fooList: List<@Size(min = 30, max = 40) String> = listOf()
)

@Schema(description = "ChildBar Request")
data class ChildBar(
    @field:Size(min = 11, max = 21) // should override parent
    @field:ArraySchema(schema = Schema(description = "expect: list[minItems = 11, maxItems = 21] expect: string[minLength = 31, maxLength = 41]"))
    override var fooList: List<@Size(min = 31, max = 41) String>,

    @field:Size(min = 12, max = 22) // genericType should work
    @field:ArraySchema(schema = Schema(description = "expect: list[minItems = 12, maxItems = 22] expect: string[minLength = 32, maxLength = 42]"))
    var barList: List<@Size(min = 32, max = 42) String> = listOf(),

    @field:DecimalMin("1") // will be override by min
    @field:Min(2)
    @field:Schema(description = "expect: minimum = 2")
    var validOrder1: Long,

    @field:Min(2) // will be override by DecimalMin
    @field:DecimalMin("1")
    @field:Schema(description = "expect: minimum = 1")
    var validOrder2: Long,
) : ParentFoo()
