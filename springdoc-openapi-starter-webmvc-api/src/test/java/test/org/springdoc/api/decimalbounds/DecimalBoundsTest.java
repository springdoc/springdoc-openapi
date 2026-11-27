/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.decimalbounds;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.AbstractCommonTest;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Checks that generated request parameter bounds preserve the annotation's decimal value.
 *
 * @author 6londe
 */
class DecimalBoundsTest {

	private static final String PRECISE_MINIMUM = "0.10000000000000000001";

	private static final String PRECISE_MAXIMUM = "100000000000000000000.00000000000000000001";

	@SpringBootApplication
	static class TestApplication {
	}

	@RestController
	static class BoundsController {

		@GetMapping("/precise-bounds")
		String precise(@RequestParam @DecimalMin(PRECISE_MINIMUM) @DecimalMax(PRECISE_MAXIMUM) BigDecimal amount) {
			return amount.toPlainString();
		}

		@GetMapping("/ordinary-bounds")
		String ordinary(@RequestParam @DecimalMin("1.25") @DecimalMax("2.5") BigDecimal amount) {
			return amount.toPlainString();
		}
	}

	@Nested
	@TestPropertySource(properties = "springdoc.api-docs.version=openapi_3_0")
	class OpenApi30 extends BoundsTests {

		@Override
		String openApiVersion() {
			return "3.0.1";
		}
	}

	@Nested
	@TestPropertySource(properties = "springdoc.api-docs.version=openapi_3_1")
	class OpenApi31 extends BoundsTests {

		@Override
		String openApiVersion() {
			return "3.1.0";
		}
	}

	@ContextConfiguration(classes = TestApplication.class)
	abstract static class BoundsTests extends AbstractCommonTest {

		private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
				.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).build();

		abstract String openApiVersion();

		@Test
		void preservesPreciseDecimalBounds() throws Exception {
			assertBounds("precise-bounds", PRECISE_MINIMUM, PRECISE_MAXIMUM);
		}

		@Test
		void preservesOrdinaryDecimalBounds() throws Exception {
			assertBounds("ordinary-bounds", "1.25", "2.5");
		}

		private void assertBounds(String path, String minimum, String maximum) throws Exception {
			String content = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
					.andReturn().getResponse().getContentAsString();
			JsonNode document = JSON_MAPPER.readTree(content);
			assertThat(document.required("openapi").asString()).isEqualTo(openApiVersion());
			JsonNode schema = document.at("/paths/~1" + path + "/get/parameters/0/schema");
			assertAll(
					() -> assertThat(schema.required("minimum").decimalValue()).isEqualByComparingTo(minimum),
					() -> assertThat(schema.required("maximum").decimalValue()).isEqualByComparingTo(maximum));
		}
	}
}
