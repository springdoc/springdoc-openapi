package test.org.springdoc.api.app187;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.RouterOperationCustomizer;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

class SpringDocApp187Test extends AbstractSpringDocTest {

	@Test
	void testAddRouterOperationCustomizerBean() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/app187.json"), true);
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.app187" })
	static class SpringDocTestApp {

		@Bean
		public RouterOperationCustomizer addRouterOperationCustomizer() {
			return (routerOperation, handlerMethod) -> {
				if (routerOperation.getParams().length > 0) {
					routerOperation.setPath(routerOperation.getPath() + "?" + String.join("&", routerOperation.getParams()));
				}
				return routerOperation;
			};
		}
	}

}
