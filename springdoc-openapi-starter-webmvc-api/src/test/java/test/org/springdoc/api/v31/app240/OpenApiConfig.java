package test.org.springdoc.api.v31.app240;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenApiCustomizer openApiCustomiser() {
    return openApi -> {

    };
  }

  @Bean
  ServerBaseUrlCustomizer serverBaseUrlCustomizer() {
    return new ServerBaseUrlCustomizer() {

      @Override
      public String customize(final String serverBaseUrl,
          final HttpRequest request) {
        // TODO Auto-generated method stub
        return null;
      }
      
    };
  }
}