package test.org.springdoc.api.v30.app205;

/**
 *
 */

import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

/**
 * Fix regression in #2031
 */
@TestPropertySource(properties = { "springdoc.group-configs[0].group=mygroup", "springdoc.group-configs[0].paths-to-match=/test" })
public class SpringdocApp205Test extends AbstractSpringDocV30Test {

	@SpringBootApplication
	static class SpringDocTestApp {}

}