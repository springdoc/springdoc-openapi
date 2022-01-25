package test.org.springdoc.api.app162;

import org.junit.jupiter.api.Test;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SpringDocApp162Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	public void testApp2() throws Exception {
		super.testApp();
	}

}
