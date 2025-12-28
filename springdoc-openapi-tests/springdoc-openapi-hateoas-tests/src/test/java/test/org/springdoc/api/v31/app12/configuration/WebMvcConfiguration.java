package test.org.springdoc.api.v31.app12.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    MappingJackson2HttpMessageConverter getMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        converter.setObjectMapper(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
        );
        return converter;
    }

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, new ByteArrayHttpMessageConverter());
	}
}
