package test.org.springdoc.api.v31.app208;

import test.org.springdoc.api.v31.AbstractSpringDocV31Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "springdoc.default-flat-param-object=true" })
public class SpringdocApp208Test extends AbstractSpringDocV31Test {

	@SpringBootApplication
	static class SpringDocTestApp {}

}
