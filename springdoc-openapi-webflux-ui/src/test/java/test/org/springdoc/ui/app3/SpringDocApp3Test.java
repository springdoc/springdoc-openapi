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

package test.org.springdoc.ui.app3;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
		"springdoc.swagger-ui.path=/documentation/swagger-ui.html",
		"springdoc.api-docs.path=/documentation/v3/api-docs"
})
public class SpringDocApp3Test extends AbstractSpringDocTest {

	@Test
	public void shouldDisplaySwaggerUiPage() {
		webTestClient.get().uri("/documentation/swagger-ui.html").exchange()
				.expectStatus().isFound();
		webTestClient.get().uri("/documentation/webjars/swagger-ui/index.html").exchange()
				.expectStatus().isOk();
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}