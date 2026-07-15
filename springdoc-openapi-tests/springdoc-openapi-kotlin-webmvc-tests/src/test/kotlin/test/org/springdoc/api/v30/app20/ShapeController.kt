package test.org.springdoc.api.v30.app20

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Schema(
	discriminatorProperty = "type",
	subTypes = [Circle::class, Square::class]
)
abstract class Shape {
	abstract val label: String?
}

data class Circle(
	override val label: String? = null,
	val radius: Int,
) : Shape()

data class Square(
	override val label: String? = null,
	val side: Int,
) : Shape()

@RestController
class ShapeController {
	@GetMapping("/shape")
	fun getShape(): Shape = Circle(radius = 1)
}
