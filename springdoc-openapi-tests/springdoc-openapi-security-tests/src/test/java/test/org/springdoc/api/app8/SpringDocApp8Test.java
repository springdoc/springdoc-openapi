package test.org.springdoc.api.app8;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "springdoc.show-login-endpoint=true")
public class SpringDocApp8Test extends AbstractSpringDocTest {

	@SpringBootApplication(scanBasePackages = { "test.org.springdoc.api.configuration,test.org.springdoc.api.app8" })
	static class SpringDocTestApp {
		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(new Info().title("Security API").version("v1")
							.license(new License().name("Apache 2.0").url("http://springdoc.org")));
		}
	}
}
