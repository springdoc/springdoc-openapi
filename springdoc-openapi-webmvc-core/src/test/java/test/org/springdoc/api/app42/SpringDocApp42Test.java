package test.org.springdoc.api.app42;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import test.org.springdoc.api.AbstractSpringDocTest;

public class SpringDocApp42Test extends AbstractSpringDocTest {


    @SpringBootApplication
    static class SpringDocTestApp {
        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI().components(new Components().addSchemas("TweetId", new StringSchema()));
        }
    }
}