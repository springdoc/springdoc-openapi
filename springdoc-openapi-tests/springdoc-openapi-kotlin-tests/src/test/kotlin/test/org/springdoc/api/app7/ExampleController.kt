package test.org.springdoc.api.app7

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class Greeting(val greeting: String)

@RestController
interface ExampleController {
    @GetMapping("/")
    fun greet(@RequestParam name: String?): Greeting
}