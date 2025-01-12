/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */
package test.org.springdoc.api.v31.app138;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Tests Spring meta-annotations as method parameters
 */
@TestPropertySource(properties = "springdoc.writer-with-order-by-keys=true")
public class SpringDocApp138Test extends AbstractSpringDocTest {

	@Autowired
	ObjectMapperProvider objectMapperProvider;

	private static Map<String, Object> apiExtensions() {
		Map extensions = new HashMap<String, Object>();

		Map linkedMap = new LinkedHashMap<String, String>();
		linkedMap.put("property1", "value1");
		linkedMap.put("property2", null);

		extensions.put("x-my-vendor-extensions", linkedMap);
		return extensions;
	}

	@Test
	protected void testApp() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0"))).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		String expected = getContent("results/3.1.0/app138.json");
		Assertions.assertEquals(expected, result);
	}

	@BeforeEach
	void init() throws IllegalAccessException {
		Field conField = FieldUtils.getDeclaredField(ObjectMapperProvider.class, "jsonMapper", true);
		ObjectMapper mapper = SpringDocObjectMapperFactory.createJson();
		conField.set(objectMapperProvider, mapper);
	}

	@SpringBootApplication
	static class SpringDocTestApp {
		@Bean
		public OpenAPI api() {
			return new OpenAPI()
					.extensions(apiExtensions());
		}
	}

	private static class SpringDocObjectMapperFactory extends ObjectMapperFactory {
		public static ObjectMapper createJson() {
			return ObjectMapperFactory.createJson();
		}
	}
}
