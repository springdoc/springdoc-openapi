package test.org.springdoc.api.v31.app1

import org.springframework.boot.autoconfigure.SpringBootApplication
import test.org.springdoc.api.v31.AbstractKotlinDataRestTest

/**
 * The properties an entity inherits from a Kotlin `@Embeddable` have to be described,
 * see https://github.com/springdoc/springdoc-openapi/issues/3332
 */
class SpringDocApp1Test : AbstractKotlinDataRestTest() {

	@SpringBootApplication
	class SpringDocTestApp
}
