/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app209;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.Constants;
import org.springdoc.core.utils.PropertyResolverUtils;
import test.org.springdoc.api.AbstractCommonTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"springdoc.pre-loading-enabled=true",
		"springdoc.pre-loading-locales=ja",
		"springdoc.api-docs.version=openapi_3_0"
})
public class SpringDocApp209Test extends AbstractCommonTest {
	public static String className;

	static {
		System.setProperty("user.country", "JP");
		System.setProperty("user.language", "ja");
	}

	@Autowired
	private OpenAPIServiceMock openAPIService;

	@Test
	void shouldOnlyByCalledOnce() throws Exception {
		//	assertEquals(1, openAPIService.getNumberOfTimesCalculatePathWasCalled());

		className = getClass().getSimpleName();
		String testNumber = className.replaceAll("[^0-9]", "");
		MvcResult mockMvcResult = mockMvc
				.perform(get(Constants.DEFAULT_API_DOCS_URL).header(HttpHeaders.ACCEPT_LANGUAGE, "ja"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.0.1"))).andReturn();
		String result = mockMvcResult.getResponse().getContentAsString();
		String expected = getContent("results/3.0.1/app" + testNumber + ".json");
		JSONAssert.assertEquals(expected, result, true);

		assertEquals(1, openAPIService.getNumberOfTimesCalculatePathWasCalled());
	}

	@SpringBootApplication
	static class SpringDocTestApp {
		@Bean("openAPIService")
		public OpenAPIServiceMock openAPIService(Optional<OpenAPI> openAPI, SecurityService securityParser, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils, Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers, Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers, Optional<JavadocProvider> javadocProvider) {
			return new OpenAPIServiceMock(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
		}
	}

	public static class OpenAPIServiceMock extends OpenAPIService {
		private int numberOfTimesCalculatePathWasCalled;

		public OpenAPIServiceMock(Optional<OpenAPI> openAPI, SecurityService securityParser, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils, Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers, Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers, Optional<JavadocProvider> javadocProvider) {
			super(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
		}

		@Override
		public void setCachedOpenAPI(OpenAPI cachedOpenAPI, Locale locale) {
			numberOfTimesCalculatePathWasCalled++;
			super.setCachedOpenAPI(cachedOpenAPI, locale);
		}

		public int getNumberOfTimesCalculatePathWasCalled() {
			return numberOfTimesCalculatePathWasCalled;
		}
	}
}
