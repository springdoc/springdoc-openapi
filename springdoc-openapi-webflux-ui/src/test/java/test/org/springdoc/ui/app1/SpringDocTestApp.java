package test.org.springdoc.ui.app1;

import org.springdoc.core.SwaggerUiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SpringDocTestApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocTestApp.class, args);
    }
}
