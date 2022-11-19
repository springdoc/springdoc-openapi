package test.org.springdoc.api.app1;

import groovy.lang.MetaClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springdoc.core.utils.SpringDocUtils;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "springdoc.enable-groovy=false")
public class SpringDocApp1Test extends AbstractSpringDocTest {
	@BeforeAll
	public static void init() {
		SpringDocUtils.getConfig().removeRequestWrapperToIgnore(MetaClass.class);
	}

	@AfterAll
	public static void clean() {
		SpringDocUtils.getConfig().addRequestWrapperToIgnore(MetaClass.class);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
