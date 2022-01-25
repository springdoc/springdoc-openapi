package test.org.springdoc.ui.app24;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

	@Bean
	SpringDocConfiguration springDocConfiguration(){
		return new SpringDocConfiguration();
	}

	@Bean
	public SpringDocConfigProperties springDocConfigProperties() {
		return new SpringDocConfigProperties();
	}
}
