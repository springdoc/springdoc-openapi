package test.org.springdoc.api.app161;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SpringDocApp161Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	public void testApp() throws Exception {
		Locale.setDefault(Locale.US);
		super.testApp();
	}

}
