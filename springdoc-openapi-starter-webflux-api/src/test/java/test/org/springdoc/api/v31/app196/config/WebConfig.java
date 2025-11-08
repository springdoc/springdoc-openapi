package test.org.springdoc.api.v31.app196.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ApiVersionConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer
				.setVersionRequired(false)
				.addSupportedVersions("1.0","v2")
				.setDefaultVersion("1.0")
				.useQueryParam("version")
				.setVersionParser(new ApiVersionParser());
	}

}
