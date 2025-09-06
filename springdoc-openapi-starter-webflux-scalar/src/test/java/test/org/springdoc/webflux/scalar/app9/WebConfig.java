package test.org.springdoc.webflux.scalar.app9;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {
	
	static final String BASE_PATH = "/test";
	@Override
	public void configurePathMatching(PathMatchConfigurer configurer) {
		configurer.addPathPrefix(BASE_PATH, t -> true);
	}

}