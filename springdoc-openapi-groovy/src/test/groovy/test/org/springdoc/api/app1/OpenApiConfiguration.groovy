package test.org.springdoc.api.app1

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition
@Configuration
class OpenApiConfiguration {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(
						new Info()
								.title('Issue OpenAPI with Groovy')
								.description('Special Groovy Metaclass Test Issue')
				)
	}
}
