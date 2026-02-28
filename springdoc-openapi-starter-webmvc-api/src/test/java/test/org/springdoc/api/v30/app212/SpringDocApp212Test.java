/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.v30.app212;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.SpecPropertiesCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Spring doc app 192 test.
 * <p>
 * A test for {@link SpecPropertiesCustomizer}
 */
@ActiveProfiles("212")
public class SpringDocApp212Test extends AbstractSpringDocV30Test {

	@Test
	void getGroupedOpenapi_shouldCustomizeFromPropertiesWithGroupNamePrefix() throws Exception {
		String result = mockMvc.perform(get("/v3/api-docs/apiGroupName"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String expected = getContent("results/3.0.1/app212-grouped.json");
		assertEquals(expected, result, true);
	}

	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		GroupedOpenApi apiGroupBeanName() {
			return GroupedOpenApi.builder()
					.group("apiGroupName")
					.packagesToScan("test.org.springdoc.api.v30.app212")
					.build();
		}
	}

}
