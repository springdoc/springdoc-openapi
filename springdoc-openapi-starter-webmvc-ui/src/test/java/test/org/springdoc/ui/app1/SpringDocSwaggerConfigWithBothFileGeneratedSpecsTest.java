/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
	Test showing how specs generated at runtime and specs configured in can work at the same time.

	The expectation is that the openapi.yml file will be shown together with the generated ones.
 */
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "spring.jackson.property-naming-strategy=UPPER_CAMEL_CASE", "springdoc.show-actuator=true",
				"management.endpoints.web.base-path=/management",
				"server.servlet.context-path=/demo/api", "management.server.port=9002", "management.server.base-path=/demo/api",
				"springdoc.swagger-ui.urls[0].url=/api-docs/xxx/v1/openapi.yml",
				"springdoc.swagger-ui.urls[0].name=toto",
})
public class SpringDocSwaggerConfigWithBothFileGeneratedSpecsTest extends AbstractSpringDocActuatorTest {

	@Test
	public void testIndexSwaggerConfig() throws Exception {
		mockMvc.perform(get("/demo/api/v3/api-docs/swagger-config").contextPath("/demo/api"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("validatorUrl", equalTo("")))
				.andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/demo/api/swagger-ui/oauth2-redirect.html")))
				.andExpect(jsonPath("url").doesNotExist())
				.andExpect(jsonPath("urls[0].url", equalTo("/demo/api/v3/api-docs/springdocDefault")))
				.andExpect(jsonPath("urls[0].name", equalTo("springdocDefault")))
				.andExpect(jsonPath("urls[1].url", equalTo("/demo/api/api-docs/xxx/v1/openapi.yml")))
				.andExpect(jsonPath("urls[1].name", equalTo("toto")))
				.andExpect(jsonPath("urls[2].url", equalTo("/demo/api/v3/api-docs/x-actuator")))
				.andExpect(jsonPath("urls[2].name", equalTo("x-actuator")));
	}


	@SpringBootApplication
	static class SpringDocTestApp {}

}