package test.org.springdoc.api.v31.app201;

import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author bnasslahsen
 */
@Component
public class SpringWebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(c -> c instanceof StringHttpMessageConverter);
	}
}