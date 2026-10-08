package test.org.springdoc.api.v30.app18

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class NullableFieldsResponse(
	val requiredField: String,
	val nullableString: String? = null,
	val nullableInt: Int? = null,
	val nullableAny: Any? = null,
	@field:Schema(description = "The nested object")
	val nullableNested: NestedObject? = null,
	val nullableComposed: ComposedObject? = null,
)

data class NestedObject(
	val name: String,
	val description: String? = null,
)

sealed class ComposedObject {
	data class First(val first: String) : ComposedObject()

	data class Second(val second: String) : ComposedObject()
}

@RestController
class NullableController {
	@GetMapping("/nullable")
	fun getNullableFields(): NullableFieldsResponse =
		NullableFieldsResponse(requiredField = "hello")
}
