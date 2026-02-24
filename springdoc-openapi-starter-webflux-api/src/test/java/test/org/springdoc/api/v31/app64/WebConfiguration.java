package test.org.springdoc.api.v31.app64;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ApiVersionConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author bnasslahsen
 */
@Configuration
public class WebConfiguration implements WebFluxConfigurer {

	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		configurer.setVersionRequired(false);
		configurer.setDefaultVersion("1.0");
		configurer.useQueryParam("API-Version");
	}
}
