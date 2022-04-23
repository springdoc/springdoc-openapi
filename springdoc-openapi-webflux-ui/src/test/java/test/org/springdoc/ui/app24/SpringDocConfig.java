package test.org.springdoc.ui.app24;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

	@Bean
	SpringDocConfiguration springDocConfiguration(){
		return new SpringDocConfiguration();
	}

	@Bean
	SpringDocConfigProperties springDocConfigProperties() {
		return new SpringDocConfigProperties();
	}

	@Bean
	ObjectMapperProvider objectMapperProvider(SpringDocConfigProperties springDocConfigProperties){
		return new ObjectMapperProvider(springDocConfigProperties);
	}
}
