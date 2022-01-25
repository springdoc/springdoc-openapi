package test.org.springdoc.api.app160;

import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "springdoc.api-docs.resolve-schema-properties=true")
public class SpringDocApp160Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}


}
