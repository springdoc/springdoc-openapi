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

package test.org.springdoc.api.v31.app187;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.RouterOperationCustomizer;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

class SpringDocApp187Test extends AbstractSpringDocTest {

	@Test
	void testAddRouterOperationCustomizerBean() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app187.json"), true);
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v31.app187" })
	static class SpringDocTestApp {

		@Bean
		public RouterOperationCustomizer addRouterOperationCustomizer() {
			return (routerOperation, handlerMethod) -> {
				if (routerOperation.getParams().length > 0) {
					routerOperation.setPath(routerOperation.getPath() + "?" + String.join("&", routerOperation.getParams()));
				}
				return routerOperation;
			};
		}
	}

}
