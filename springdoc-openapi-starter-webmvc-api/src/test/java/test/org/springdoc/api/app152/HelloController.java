package test.org.springdoc.api.app152;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping
	public String helloWorld() {
		return "ok";
	}

	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void configurePathMatch(PathMatchConfigurer configurer) {
			configurer.setPatternParser(new PathPatternParser());
		}

	}

}
