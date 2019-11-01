package test.org.springdoc.api.app39;

import org.springdoc.api.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;

@SpringBootApplication
@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.app39" })
public class SpringDocTestApp {

  public static void main(String[] args) {
    SpringApplication.run(SpringDocTestApp.class, args);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    StringSchema schema = new StringSchema();
    return new OpenAPI()
        .components(new Components().addParameters("myGlobalHeader", new HeaderParameter().required(true).name("My-Global-Header").description("My Global Header").schema(schema)));
  }
  
  @Bean
  public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
    return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
        .forEach(operation -> operation.addParametersItem(new HeaderParameter().$ref("#/components/parameters/myGlobalHeader")));
  }
}
