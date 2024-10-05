package test.org.springdoc.api.app162;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

/**
 * The type Spring doc app 162 test.
 */
@TestPropertySource(properties = "springdoc.override-with-generic-response=true")
class SpringDocApp162Test extends AbstractSpringDocTest {

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {
		/**
		 * Custom open api open api.
		 *
		 * @return the open api
		 */
		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(new Info()
							.title("SpringShop API")
							.version("v1")
							.description("The description of the api"));
		}

	}

}
