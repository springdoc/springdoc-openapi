package test.org.springdoc.api.v31.app249;

import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class SpringDocApp249Test extends AbstractSpringDocTest {

	@SpringBootApplication
	@ComponentScan(basePackages = "test.org.springdoc.api.v31.app249")
	static class SpringDocTestApp {}
}
