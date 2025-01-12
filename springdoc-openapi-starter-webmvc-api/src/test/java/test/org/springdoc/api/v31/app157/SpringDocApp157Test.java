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

package test.org.springdoc.api.v31.app157;

import java.util.ArrayList;

import io.swagger.v3.core.converter.ModelConverters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * The Converters.
	 */
	private final ModelConverters converters = ModelConverters.getInstance(true);

	@Autowired
	private StringyConverter stringyConverter;

	/**
	 * Unregister converter.
	 */
	@AfterEach
	public void unregisterConverter() {
		converters.removeConverter(stringyConverter);
	}
	
	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(jsonPath("$.components.schemas.Foo.required", is(new ArrayList<String>() {{
					add("stringy");
				}})))
				.andExpect(jsonPath("$.components.schemas.Bar", not(hasProperty("required"))));
	}
	
	@SpringBootApplication
	static class SpringBootApp {}
}
