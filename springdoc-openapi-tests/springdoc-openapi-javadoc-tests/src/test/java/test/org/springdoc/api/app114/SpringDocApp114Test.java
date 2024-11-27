package test.org.springdoc.api.app114;

import javax.money.MonetaryAmount;

import org.springdoc.core.utils.SpringDocUtils;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The type Spring doc app 114 test.
 */
class SpringDocApp114Test extends AbstractSpringDocTest {

	static {
		SpringDocUtils.getConfig().replaceWithClass(MonetaryAmount.class, org.springdoc.core.converters.models.MonetaryAmount.class);
	}

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {}
}
