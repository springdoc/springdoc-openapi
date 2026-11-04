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
