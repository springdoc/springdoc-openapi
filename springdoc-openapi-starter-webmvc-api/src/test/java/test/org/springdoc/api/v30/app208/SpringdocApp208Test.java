package test.org.springdoc.api.v30.app208;

import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "springdoc.default-flat-param-object=true" })
public class SpringdocApp208Test extends AbstractSpringDocV30Test {

	@SpringBootApplication
	static class SpringDocTestApp {}

}
