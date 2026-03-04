package test.org.springdoc.api.v31.app251;

import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class SpringDocApp251Test extends AbstractSpringDocTest {

	@SpringBootApplication
	@ComponentScan(basePackages = "test.org.springdoc.api.v31.app251")
	static class SpringDocTestApp {}
}
