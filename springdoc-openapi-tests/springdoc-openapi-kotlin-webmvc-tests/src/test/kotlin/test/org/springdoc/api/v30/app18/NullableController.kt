package test.org.springdoc.api.v30.app18

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class NullableFieldsResponse(
	val requiredField: String,
	val nullableString: String? = null,
	val nullableInt: Int? = null,
	val nullableNested: NestedObject? = null,
)

data class NestedObject(
	val name: String,
	val description: String? = null,
)

@RestController
class NullableController {
	@GetMapping("/nullable")
	fun getNullableFields(): NullableFieldsResponse =
		NullableFieldsResponse(requiredField = "hello")
}
