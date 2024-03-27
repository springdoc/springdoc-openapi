package test.org.springdoc.api.app11

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
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
    @GetMapping("/foo/remove-kotlin-indent")
    fun readFoo(@RequestParam name: String?) = FooResponse("Hello ${name ?: "world"}")

}

data class FooResponse(
    private val name: String,
)