package test.org.springdoc.ui.app6;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDocTestApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocTestApp.class, args);
    }

    @Bean
    public GroupedOpenApi storeOpenApi() {
        String paths[] = {"/store/**"};
        return GroupedOpenApi.builder()
                .setGroup("stores")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi groupOpenApi() {
        String paths[] = {"/pet/**"};
        return GroupedOpenApi.builder()
                .setGroup("pets")
                .pathsToMatch(paths)
                .build();
    }

}