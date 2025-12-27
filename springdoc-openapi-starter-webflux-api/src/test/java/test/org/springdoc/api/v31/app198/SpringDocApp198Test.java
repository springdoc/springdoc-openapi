package test.org.springdoc.api.v31.app198;

import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringDocApp198Test {
     private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
            .withUserConfiguration(TestApp.class);

    @Test
    void configurations_successfully_loaded_without_embedded_server() {
        // Exclude WebServers AutoConfiguration to simulate WAR deployment
        contextRunner
                .withPropertyValues(
                        "spring.autoconfigure.exclude=" +
                                "org.springframework.boot.tomcat.autoconfigure.reactive.TomcatReactiveWebServerAutoConfiguration," +
                                "org.springframework.boot.reactor.netty.autoconfigure.NettyReactiveWebServerAutoConfiguration",
                        "springdoc.show-actuator=true"
                )
                .run(context -> assertThat(context)
                        .hasNotFailed()
                        .hasBean("openApiResource")
                        .hasBean("actuatorProvider")
                        .hasBean("multipleOpenApiResource")
                );
    }

    @SpringBootApplication
	static class TestApp {
		@Bean
        GroupedOpenApi testGroupedOpenApi() {
			return GroupedOpenApi.builder()
					.group("test-group")
					.packagesToScan("org.test")
					.build();
		}
	}
}
