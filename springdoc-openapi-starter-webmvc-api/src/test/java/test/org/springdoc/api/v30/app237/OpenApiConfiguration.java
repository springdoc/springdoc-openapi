package test.org.springdoc.api.v30.app237;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

	public OpenApiConfiguration() {
		// LocalTime support
		PrimitiveType.enablePartialTime();
	}

	private OpenAPI standardOpenApi() {
		String title = getClass().getPackage().getImplementationTitle();
		String version = getClass().getPackage().getImplementationVersion();
		return new OpenAPI()
				.info(new Info()
						.title(title == null ? "DEV" : title)
						.version(version == null ? "local" : version));
	}

	@Bean
	public OpenAPI devOpenApi() {
		return standardOpenApi();
	}

}
