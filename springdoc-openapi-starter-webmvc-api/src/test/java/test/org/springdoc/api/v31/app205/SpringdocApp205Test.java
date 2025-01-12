package test.org.springdoc.api.v31.app205;

/**
 * @author bnasslahsen
 */

import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

/**
 * Fix regression in #2031
 */
@TestPropertySource(properties = { "springdoc.group-configs[0].group=mygroup", "springdoc.group-configs[0].paths-to-match=/test" })
public class SpringdocApp205Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

}