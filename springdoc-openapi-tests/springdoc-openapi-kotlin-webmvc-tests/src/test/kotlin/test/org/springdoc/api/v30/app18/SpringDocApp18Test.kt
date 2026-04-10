package test.org.springdoc.api.v30.app18

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v30.AbstractKotlinSpringDocMVCTest

class SpringDocApp18Test : AbstractKotlinSpringDocMVCTest() {

	@SpringBootApplication
	@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v30.app18"])
	class DemoApplication
}
