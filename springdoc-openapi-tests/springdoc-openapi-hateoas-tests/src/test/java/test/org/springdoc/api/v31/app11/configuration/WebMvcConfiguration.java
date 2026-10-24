package test.org.springdoc.api.v31.app11.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration  implements WebMvcConfigurer {

	@Bean
	JacksonJsonHttpMessageConverter getMappingJacksonHttpMessageConverter() {
		JacksonJsonHttpMessageConverter converter = new JacksonJsonHttpMessageConverter(JsonMapper.builder()
				.changeDefaultPropertyInclusion(inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
				.build());
		converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
		return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, new ByteArrayHttpMessageConverter());
	}
}
