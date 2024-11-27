package test.org.springdoc.api.app188;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "springdoc.group-configs[0].group=mygroup", "springdoc.group-configs[0].paths-to-match=/test" })
class SpringDocApp188Test extends AbstractSpringDocTest {

	@Test
	void testApp1() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL +"/mygroup").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/app188.json"), true);
	}

	@SpringBootApplication
	@Import({
			OrderDemo.Customizer1.class,
			OrderDemo.Customizer2.class,
			OrderDemo.Customizer3.class,
	})
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.app188" })
	static class SpringDocTestApp {}

}
