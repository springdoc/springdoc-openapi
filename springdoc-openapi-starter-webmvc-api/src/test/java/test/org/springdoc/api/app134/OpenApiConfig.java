package test.org.springdoc.api.app134;

import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi groupV1OpenApi() {
        return GroupedOpenApi.builder()
                .group("v1-group").producesToMatch(HelloController.VERSION_1)
                .build();
    }

    @Bean
    public GroupedOpenApi groupV2OpenApi() {
        return GroupedOpenApi.builder()
                .group("v2-group").producesToMatch(HelloController.VERSION_2)
                .build();
    }

	@Bean
	public GroupedOpenApi groupV3OpenApi() {
		return GroupedOpenApi.builder()
				.group("v2-consumes-group").consumesToMatch(HelloController.VERSION_2)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV4OpenApi() {
		return GroupedOpenApi.builder()
				.group("v1-headers-group").headersToMatch(HelloController.HEADER_1)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV5OpenApi() {
		return GroupedOpenApi.builder()
				.group("v1-v2-headers-group").headersToMatch(HelloController.HEADER_1, HelloController.HEADER_2)
				.build();
	}
}
