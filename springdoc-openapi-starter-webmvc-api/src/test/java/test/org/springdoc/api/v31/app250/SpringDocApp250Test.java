package test.org.springdoc.api.v31.app250;

import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class SpringDocApp250Test extends AbstractSpringDocTest {

	@SpringBootApplication
	@ComponentScan(basePackages = "test.org.springdoc.api.v31.app250")
	static class SpringDocTestApp {}
}
