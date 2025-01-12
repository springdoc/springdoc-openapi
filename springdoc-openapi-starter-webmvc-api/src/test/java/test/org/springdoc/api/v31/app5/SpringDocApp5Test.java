package test.org.springdoc.api.v31.app5;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.RouterOperationCustomizer;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpringDocApp5Test extends AbstractSpringDocTest {

	@Test
	void testAddRouterOperationCustomizerBean() throws Exception {
		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		MvcResult mockMvcResult =
				mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
						.andExpect(jsonPath("$.openapi", is("3.1.0"))).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		String expected = getContent("results/3.1.0/app" + testNumber + ".json");
		assertEquals(expected, result, true);
	}

	@SpringBootApplication
	@ComponentScan
	static class SpringDocTestApp {

		@Bean
		public RouterOperationCustomizer addRouterOperationCustomizer() {
			return (routerOperation, handlerMethod) -> {
				if (routerOperation.getParams().length > 0) {
					routerOperation.setPath(routerOperation.getPath() + "?" + String.join("&", routerOperation.getParams()));
				}
				return routerOperation;
			};
		}
	}

}
