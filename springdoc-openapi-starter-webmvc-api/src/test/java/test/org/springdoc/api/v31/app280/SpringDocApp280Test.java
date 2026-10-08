package test.org.springdoc.api.v31.app280;

import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

@TestPropertySource(properties = {
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ENABLED + "=true",
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ONE_OF_AS_REF + "=true"
})
public class SpringDocApp280Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {
	}
}
