package test.org.springdoc.api.v31.app256.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bnasslahsen
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer
				.setVersionRequired(false)
				.addSupportedVersions("1.0","2.0")
				.setDefaultVersion("1.0")
				.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
				.setVersionParser(new ApiVersionParser());
	}
}
