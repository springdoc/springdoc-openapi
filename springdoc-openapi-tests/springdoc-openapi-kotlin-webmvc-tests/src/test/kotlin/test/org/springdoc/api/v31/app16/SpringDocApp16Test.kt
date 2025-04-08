package test.org.springdoc.api.v31.app16

import org.springdoc.core.utils.Constants.SPRINGDOC_DEFAULT_FLAT_PARAM_OBJECT
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.test.context.TestPropertySource
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

@TestPropertySource(properties = ["$SPRINGDOC_DEFAULT_FLAT_PARAM_OBJECT=true"])
class SpringDocApp16Test : AbstractKotlinSpringDocMVCTest() {
    @SpringBootApplication
    class DemoApplication
}
