package test.org.springdoc.api.app2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;

@SpringBootApplication
public class SpringDocTestApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocTestApp.class, args);
    }

    @Bean
    EvoInflectorLinkRelationProvider relProvider() {
        return new EvoInflectorLinkRelationProvider();
    }

}
