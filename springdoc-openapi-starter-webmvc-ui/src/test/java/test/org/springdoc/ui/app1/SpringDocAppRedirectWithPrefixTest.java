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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.ui.AbstractSpringDocTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.path=/documentation/swagger-ui.html",
		"springdoc.api-docs.path=/documentation/v3/api-docs"
})
public class SpringDocAppRedirectWithPrefixTest extends AbstractSpringDocTest {

	@Test
	void shouldRedirectWithPrefix() throws Exception {

		mockMvc.perform(get("/documentation/v3/api-docs/swagger-config"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("validatorUrl", equalTo("")))
				.andExpect(jsonPath("oauth2RedirectUrl", equalTo("http://localhost/documentation/swagger-ui/oauth2-redirect.html")));

		super.checkJS("results/app1-prefix", "/documentation" + Constants.SWAGGER_INITIALIZER_URL, StringUtils.EMPTY);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}