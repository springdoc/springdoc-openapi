package test.org.springdoc.api.app110;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
		"application-description=description",
		"application-version=v1" })
public class SpringDocApp110Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {

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
