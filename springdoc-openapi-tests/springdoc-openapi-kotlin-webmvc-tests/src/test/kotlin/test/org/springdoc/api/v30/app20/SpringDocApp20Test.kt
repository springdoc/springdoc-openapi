package test.org.springdoc.api.v30.app20

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import test.org.springdoc.api.v30.AbstractKotlinSpringDocMVCTest

class SpringDocApp20Test : AbstractKotlinSpringDocMVCTest() {

	@SpringBootApplication
	@ComponentScan(basePackages = ["org.springdoc", "test.org.springdoc.api.v30.app20"])
	class DemoApplication
}
