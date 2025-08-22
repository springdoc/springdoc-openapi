package test.org.springdoc.api.v31.app248;


import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import test.org.springdoc.api.AbstractCommonTest;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"logging.level.io.swagger=DEBUG"
})
public class SpringDocApp248Test extends AbstractCommonTest {

	public static String className;

	@Test
	void performanceTest() {
		className = getClass().getSimpleName();
		assertTimeout(Duration.ofSeconds(2), () -> {
			mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
					.andExpect(jsonPath("$.openapi", is("3.1.0"))).andReturn();
		});
	}

	@SpringBootApplication
	static class SpringDocTestApp {

	}
}