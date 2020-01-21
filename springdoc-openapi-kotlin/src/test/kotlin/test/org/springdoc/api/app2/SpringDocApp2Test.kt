package test.org.springdoc.api.app2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.AbstractKotlinSpringDocTest

@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.app2"])
class SpringDocApp2Test : AbstractKotlinSpringDocTest() {

    @SpringBootApplication
    open class DemoApplication

}