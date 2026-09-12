package test.org.springdoc.api.v30.app3354.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer.detectSupportedVersions(false)
			.addSupportedVersions("2.0.0", "3.0.0")
			.setDefaultVersion("2.0.0")
			.setVersionRequired(false)
			.useRequestHeader("X-API-Version")
			.setVersionParser(new ApiVersionParser());
	}

}
