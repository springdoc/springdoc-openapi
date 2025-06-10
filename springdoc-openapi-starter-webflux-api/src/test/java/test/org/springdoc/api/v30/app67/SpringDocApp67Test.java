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

package test.org.springdoc.api.v30.app67;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class SpringDocApp67Test extends AbstractSpringDocTest {

	@Test
	void testApp() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + groupName).exchange().expectStatus().isOk().expectBody()
				.jsonPath("$.openapi").isEqualTo("3.0.1")
				.jsonPath("$.paths./api.get.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.get.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.post.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.post.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.put.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.put.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.patch.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.patch.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.delete.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.delete.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.options.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.options.responses.200.content.['*/*'].schema.type").isEqualTo("string")
				.jsonPath("$.paths./api.head.tags[0]").isEqualTo("hello-controller")
				.jsonPath("$.paths./api.head.responses.200.content.['*/*'].schema.type").isEqualTo("string");
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v30.app67" })
	static class SpringDocTestApp {}
}
