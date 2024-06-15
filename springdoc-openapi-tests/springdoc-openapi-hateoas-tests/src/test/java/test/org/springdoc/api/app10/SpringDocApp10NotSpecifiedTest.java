/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.app10;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import test.org.springdoc.api.AbstractSpringDocTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SpringDocApp10NotSpecifiedTest extends AbstractSpringDocTest {

	@Override
	@Test
	public void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.openapi", is("3.0.1")))
			.andExpect(content().json(getContent("results/app10-direct.json"), true));
	}

	@SpringBootApplication
	public static class SpringDocTestApp {

	}

}
