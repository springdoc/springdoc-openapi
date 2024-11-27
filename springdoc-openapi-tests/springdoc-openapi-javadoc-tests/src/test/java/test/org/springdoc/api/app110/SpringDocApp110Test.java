package test.org.springdoc.api.app110;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

/**
 * The type Spring doc app 110 test.
 */
@TestPropertySource(properties = {
		"application-description=description",
		"application-version=v1" })
class SpringDocApp110Test extends AbstractSpringDocTest {

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {

		/**
		 * Custom open api open api.
		 *
		 * @param appDesciption the app desciption 
		 * @param appVersion the app version 
		 * @return the open api
		 */
		@Bean
		public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {

			return new OpenAPI()
					.info(new Info()
							.title("sample application API")
							.version(appVersion)
							.description(appDesciption)
							.termsOfService("http://swagger.io/terms/")
							.license(new License().name("Apache 2.0").url("http://springdoc.org")));
		}
	}
}
