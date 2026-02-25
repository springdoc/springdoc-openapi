package test.org.springdoc.api.v31.app257.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer.detectSupportedVersions(false)
			.addSupportedVersions("1.0", "2.0")
			.setDefaultVersion("1.0")
			.setVersionRequired(false)
			.useRequestHeader("X-API-Version")
			.setVersionParser(new ApiVersionParser());
	}

}
