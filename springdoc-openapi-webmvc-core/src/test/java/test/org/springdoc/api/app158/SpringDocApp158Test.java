package test.org.springdoc.api.app158;

import java.util.List;

import io.swagger.v3.core.converter.ModelConverters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test is to make sure that a new model converter can access the parent of a type, even if
 * the type is enclosed in an ignored wrapper.  We test this by setting up a model converter which
 * adds "stringy" to the "required" property of a schema's parent, when the sub schema is a String.
 */
public class SpringDocApp157Test extends AbstractSpringDocTest {

  @SpringBootApplication
  static class SpringBootApp {}

  private StringyConverter myConverter = new StringyConverter();
  private ModelConverters converters = ModelConverters.getInstance();

  @BeforeEach
  public void registerConverter() {
    converters.addConverter(myConverter);
  }

  @AfterEach
  public void unregisterConverter() {
    converters.removeConverter(myConverter);
  }

  @Test
  public void testApp() throws Exception {
    mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.openapi", is("3.0.1")))
        .andExpect(jsonPath("$.components.schemas.Foo.required", is(List.of("stringy"))))
        .andExpect(jsonPath("$.components.schemas.Bar", not(hasProperty("required"))));
  }
=======
import SpringBootApplication;
import AbstractSpringDocTest;

public class SpringDocApp157Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

>>>>>>> 5537e88ef7529cc8bca8d4f85a36cfab6b6b5b86
}
