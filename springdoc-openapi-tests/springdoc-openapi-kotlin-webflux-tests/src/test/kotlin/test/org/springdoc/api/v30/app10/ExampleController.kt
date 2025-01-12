package test.org.springdoc.api.v30.app10

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import test.org.springdoc.api.v30.app8.Greeting

data class Greeting(val greeting: String)

@RestController
class ExampleController {
	@GetMapping("/")
	fun greet(@RequestParam name: String?) = Greeting("Hello ${name ?: "world"}")

	@GetMapping("/test")
	fun test(@RequestParam name: String) = Greeting("Hello $name")

	@GetMapping("/test-with-default")
	fun testWithDefault(@RequestParam(defaultValue = "world") name: String) = Greeting("Hello $name")
}