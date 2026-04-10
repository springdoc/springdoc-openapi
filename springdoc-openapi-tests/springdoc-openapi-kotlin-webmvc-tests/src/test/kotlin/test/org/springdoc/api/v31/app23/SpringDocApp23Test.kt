package test.org.springdoc.api.v31.app23

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

class SpringDocApp23Test : AbstractKotlinSpringDocMVCTest() {

	@SpringBootApplication
	@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app23"])
	class DemoApplication
}
