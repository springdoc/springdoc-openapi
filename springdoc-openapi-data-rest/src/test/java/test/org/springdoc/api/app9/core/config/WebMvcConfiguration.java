package test.org.springdoc.api.app9.core.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //
				.allowedOrigins("http://localhost") //
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //
				.allowedHeaders("*") //
				.allowCredentials(true) //
				.maxAge(MAX_AGE_SECS);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		ApplicationConversionService.configure(registry);
	}

}
