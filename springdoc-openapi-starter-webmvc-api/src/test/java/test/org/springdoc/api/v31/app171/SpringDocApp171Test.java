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

package test.org.springdoc.api.v31.app171;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiLocaleCustomizer;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = Constants.SPRINGDOC_CACHE_DISABLED + "=false")
public class SpringDocApp171Test extends AbstractSpringDocTest {

	@Test
	@Override
	public void testApp() throws Exception {
		Locale.setDefault(Locale.US);
		testApp(Locale.US);
		testApp(Locale.FRANCE);
		testApp(Locale.UK);
	}

	private void testApp(Locale locale) throws Exception {
		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		MvcResult mockMvcResult =
				mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL).locale(locale).header(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag())).andExpect(status().isOk())
						.andExpect(jsonPath("$.openapi", is("3.1.0"))).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		String expected = getContent("results/3.1.0/app" + testNumber + "-" + locale.toLanguageTag() + ".json");
		assertEquals(expected, result, true);
	}

	@SpringBootApplication
	static class SpringDocTestApp {

		@Autowired
		ResourceBundleMessageSource resourceBundleMessageSource;

		@Bean
		public OpenApiLocaleCustomizer openApiLocaleCustomizer() {
			return (openAPI, locale)
					-> openAPI.getInfo().title(resourceBundleMessageSource.getMessage("test", null, locale));
		}

	}
}