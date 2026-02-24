package test.org.springdoc.api.v31.app194.config;

import test.org.springdoc.api.v31.NullSafeApiVersionStrategyDecorator;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.accept.ApiVersionStrategy;
import org.springframework.web.reactive.config.ApiVersionConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.AbstractHandlerMapping;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .usePathSegment(1)
				.detectSupportedVersions(false)
                .addSupportedVersions("1.0","2.0")
                .setDefaultVersion("1.0")
				.setVersionRequired(false)
                .setVersionParser(new ApiVersionParser());
    }

	@Bean
	SmartInitializingSingleton nullSafeApiVersionStrategyInitializer(
			ObjectProvider<AbstractHandlerMapping> handlerMappings) {
		return () -> handlerMappings.forEach(mapping -> {
			ApiVersionStrategy strategy = mapping.getApiVersionStrategy();
			if (strategy != null && !(strategy instanceof NullSafeApiVersionStrategyDecorator)) {
				mapping.setApiVersionStrategy(new NullSafeApiVersionStrategyDecorator(strategy));
			}
		});
	}

}
