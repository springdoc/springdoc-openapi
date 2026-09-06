/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app176;

import java.util.List;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies that a {@code null} type (propagated from a TYPE_USE {@code @Nullable}
 * annotation on a {@code @ParameterObject} field) is stripped when that field is reused as
 * a path parameter, while it is preserved for query parameters.
 *
 * @author tthornton3-chwy
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SpringDocApp176Test {

	private static final String PATH = "$.paths.['/clinics/{clinicId}/vets'].get.parameters";

	@Autowired
	protected MockMvc mockMvc;

	private static Object readSingle(String result, String jsonPath) {
		return ((JSONArray) JsonPath.parse(result).read(jsonPath)).get(0);
	}

	@Test
	void pathParameterIsNotNullableButQueryParameterIs() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(status().isOk()).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();

		// A path parameter is always required and can never be null.
		assertThat(readSingle(result, PATH + "[?(@.name == 'clinicId')].required"))
				.isEqualTo(Boolean.TRUE);
		assertThat(readSingle(result, PATH + "[?(@.name == 'clinicId')].schema.type"))
				.isEqualTo("string");

		// A nullable query parameter keeps its null type.
		assertThat(readSingle(result, PATH + "[?(@.name == 'name')].required"))
				.isEqualTo(Boolean.FALSE);
		assertThat(readSingle(result, PATH + "[?(@.name == 'name')].schema.type"))
				.isEqualTo(new JSONArray() {{
					addAll(List.of("string", "null"));
				}});
	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}

}
