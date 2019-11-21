package test.org.springdoc.api.app1;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.springdoc", "test.org.springdoc.api.app1"})
public class SpringDocTestApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringDocTestApp.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Kotlin API").version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
