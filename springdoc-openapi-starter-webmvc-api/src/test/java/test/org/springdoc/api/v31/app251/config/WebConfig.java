package test.org.springdoc.api.v31.app251.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
