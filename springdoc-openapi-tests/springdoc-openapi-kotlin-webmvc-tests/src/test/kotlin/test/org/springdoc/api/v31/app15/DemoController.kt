package test.org.springdoc.api.v31.app15

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

/**
field following test cases:
```
| case required         | 11true | 10true | 01false | 00false | N1false | N0true |
| :-------------------- | :----- | :----- | :------ | :------ | :----- | :------ |
| schema required value | true   | true   | false   | false   | none   | none    |
| field nullable        | true   | false  | true    | false   | true   | false   |
```
 */
class DemoRequest(

    @field:Schema(required = true)
    val field11true: String?,

    @field:Schema(required = true)
    val field10true: String,

    @field:Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    val field01false: String?,

    @field:Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    val field00false: String,

    @field:Schema
    val fieldN1false: String?,

    @field:Schema
    val fieldN0true: String,
)
