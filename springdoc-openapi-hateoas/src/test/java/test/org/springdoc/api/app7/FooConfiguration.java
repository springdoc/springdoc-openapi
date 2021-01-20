package test.org.springdoc.api.app7;

import org.springdoc.core.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FooConfiguration {
  @Bean
  public GroupedOpenApi userOpenApi() {
    String packagesToscan[] = {"test.org.springdoc.api.app7"};
    return GroupedOpenApi.builder().group("foo-service").packagesToScan(packagesToscan)
        .build();
  }
}
