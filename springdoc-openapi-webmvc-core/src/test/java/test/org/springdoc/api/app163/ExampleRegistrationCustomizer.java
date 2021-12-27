package test.org.springdoc.api.app163;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.OpenApiCustomiser;

import org.springframework.stereotype.Component;

@Component
public class ExampleRegistrationCustomizer implements OpenApiCustomiser {

  private final List<Map.Entry<String, Example>> examplesToRegister;

  public ExampleRegistrationCustomizer(List<Map.Entry<String, Example>> examplesToRegister) {
    this.examplesToRegister = examplesToRegister;
  }

  @Override
  public void customise(OpenAPI openApi) {
    examplesToRegister.forEach(entry -> openApi.getComponents().addExamples(entry.getKey(), entry.getValue()));
  }
}
