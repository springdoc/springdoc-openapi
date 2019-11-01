package test.org.springdoc.api.app42;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;

@SpringBootApplication
public class SpringDocTestApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringDocTestApp.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components().addSchemas("TweetId", new StringSchema()));
	}
}
