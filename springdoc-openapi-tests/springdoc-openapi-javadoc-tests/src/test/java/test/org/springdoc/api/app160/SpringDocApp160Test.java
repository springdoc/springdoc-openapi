package test.org.springdoc.api.app160;

import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

/**
 * The type Spring doc app 160 test.
 */
@TestPropertySource(properties = "springdoc.api-docs.resolve-schema-properties=true")
class SpringDocApp160Test extends AbstractSpringDocTest {

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {}

}
