package test.org.springdoc.api.app1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class SpringDocTestApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringDocTestApp.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicScheme",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.info(new Info().title("SpringShop API").version("v0")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
