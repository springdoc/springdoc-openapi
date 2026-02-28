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

package test.org.springdoc.api.v31.app190;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractCommonTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;

import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLE_DEFAULT_API_DOCS;

@WebFluxTest(properties = SPRINGDOC_ENABLE_DEFAULT_API_DOCS + "=false")
public class SpringDocApp190Test extends AbstractCommonTest {

	@Test
	void test_disable_default_api_docs() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus().isNotFound();
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v31.app190" })
	static class SpringDocTestApp {}
}