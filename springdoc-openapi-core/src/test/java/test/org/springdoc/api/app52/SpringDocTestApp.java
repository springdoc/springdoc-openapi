package test.org.springdoc.api.app52;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication
public class SpringDocTestApp {

	public static void main(String[] args) {
        SpringApplication.run(SpringDocTestApp.class, args);
    }

	@Configuration
	class OpenApiConfiguration {
		@Bean
		public OpenAPI defineOpenApi() {
			OpenAPI api = new OpenAPI();
			api.components(new Components().addResponses("Unauthorized",
					new ApiResponse().description("Unauthorized")
							.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
									new io.swagger.v3.oas.models.media.MediaType().schema(new StringSchema())))));
			return api;
		}
	}

}
