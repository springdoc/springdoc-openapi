package test.org.springdoc.api.app170;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiCustomizer;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


public class SpringDocApp170Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {
		@Bean
		public OpenApiCustomizer openApiCustomiser() {
			return openApi -> openApi.getServers().forEach(s -> s.url("URL"));
		}
	}

	@Test
	public void testApp1() throws Exception {
		this.testApp();
	}

}
