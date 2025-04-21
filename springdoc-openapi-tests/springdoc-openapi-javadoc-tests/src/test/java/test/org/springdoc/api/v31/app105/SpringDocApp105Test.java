/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app105;

import java.util.Optional;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.OAS31ModelConverter;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.is;
import static org.springdoc.core.utils.Constants.SPRINGDOC_EXPLICIT_OBJECT_SCHEMA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Spring doc app 105 test.
 */
@TestPropertySource(properties = {
		SPRINGDOC_EXPLICIT_OBJECT_SCHEMA+"=true",
		"springdoc.explicit-object-schema=true",
		"springdoc.group-configs[0].group=stores",
		"springdoc.group-configs[0].paths-to-match=/store/**",
		"springdoc.group-configs[1].group=users",
		"springdoc.group-configs[1].packages-to-scan=test.org.springdoc.api.v31.app105.api.user",
		"springdoc.group-configs[2].group=pets",
		"springdoc.group-configs[2].paths-to-match=/pet/**",
		"springdoc.group-configs[3].group=groups test",
		"springdoc.group-configs[3].paths-to-match=/v1/**",
		"springdoc.group-configs[3].paths-to-exclude=/v1/users",
		"springdoc.group-configs[3].packages-to-scan=test.org.springdoc.api.v31.app105.api.user,test.org.springdoc.api.v31.app105.api.store",
})
class SpringDocApp105Test extends AbstractSpringDocTest {

	@AfterAll
	public static void reset() {
		System.setProperty(Schema.EXPLICIT_OBJECT_SCHEMA_PROPERTY, "false");
		ModelConverters instance = ModelConverters.getInstance(true);
		Optional<ModelConverter> oas31ModelConverter =
				instance.getConverters()
						.stream().filter(modelConverter -> modelConverter instanceof OAS31ModelConverter).findAny();
		oas31ModelConverter.ifPresent(instance::removeConverter);
	}

	/**
	 * Test app.
	 *
	 * @throws Exception the exception
	 */
	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/stores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app105-1.json"), true));
	}

	/**
	 * Test app 2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void testApp2() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app105-2.json"), true));
	}

	/**
	 * Test app 3.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void testApp3() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/pets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app105-3.json"), true));
	}

	/**
	 * Test app 4.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void testApp4() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/groups test"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", is("3.1.0")))
				.andExpect(content().json(getContent("results/3.1.0/app105-4.json"), true));
	}


	/**
	 * The type Spring doc test app.
	 */
	@SpringBootApplication
	static class SpringDocTestApp {
		/**
		 * Custom open api open api.
		 *
		 * @return the open api
		 */
		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.components(new Components().addSecuritySchemes("basicScheme",
							new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
					.info(new Info().title("Petstore API").version("v0").description(
									"This is a sample server Petstore server.  You can find out more about     Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).      For this sample, you can use the api key `special-key` to test the authorization     filters.")
							.termsOfService("http://swagger.io/terms/")
							.license(new License().name("Apache 2.0").url("http://springdoc.org")));
		}
	}
}
