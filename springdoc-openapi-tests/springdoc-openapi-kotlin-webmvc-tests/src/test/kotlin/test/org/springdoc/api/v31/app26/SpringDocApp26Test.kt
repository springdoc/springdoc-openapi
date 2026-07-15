package test.org.springdoc.api.v31.app26

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v31.AbstractKotlinSpringDocMVCTest

class SpringDocApp26Test : AbstractKotlinSpringDocMVCTest() {

	@SpringBootApplication
	@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v31.app26"])
	class DemoApplication
}
