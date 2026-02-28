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

package test.org.springdoc.webmvc.scalar.app18;

import java.util.Map;

import org.junit.jupiter.api.Test;
import test.org.springdoc.webmvc.scalar.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.org.springdoc.webmvc.scalar.app18.SpringDocApp18Test.SCALAR_PATH;

@TestPropertySource(properties = {
		"server.forward-headers-strategy=framework",
		"scalar.path="+SCALAR_PATH,
		"springdoc.api-docs.path=/bar/openapi/v3"
})
public class SpringDocApp18Test extends AbstractCommonTest {

	private static final String X_FORWARD_PREFIX = "/path/prefix";

	private static final String API_DOCS_URL = "/bar/openapi/v3";

	static final String SCALAR_PATH = "/foo/documentation/scalar";

	@Test
	void checkContent() throws Exception {
		checkContent(SCALAR_PATH);
	}
	
	@Test
	void shouldServeOpenapiJsonUnderCustomPath() throws Exception {
		mockMvc.perform(get(API_DOCS_URL)
						.header("X-Forwarded-Prefix", X_FORWARD_PREFIX))
				.andExpect(status().isOk());
	}

	@Test
	void checkContentBehindProxy() throws Exception {
		Map<String, String> headers = Map.of("X-Forwarded-Prefix", X_FORWARD_PREFIX);
		checkContent(SCALAR_PATH, headers,"results/app18-1" );
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
