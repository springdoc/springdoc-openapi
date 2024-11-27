package test.org.springdoc.api.app152;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * The type Hello controller.
 */
@RestController
@RequestMapping("/api")
class HelloController {

	/**
	 * Hello world string.
	 *
	 * @return the string
	 */
	@GetMapping
	public String helloWorld() {
		return "ok";
	}

	/**
	 * The type Web config.
	 */
	@Configuration
	class WebConfig implements WebMvcConfigurer {

		/**
		 * Configure path match.
		 *
		 * @param configurer the configurer
		 */
		@Override
		public void configurePathMatch(PathMatchConfigurer configurer) {
			configurer.setPatternParser(new PathPatternParser());
		}

	}

}
