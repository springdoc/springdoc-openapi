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

package test.org.springdoc.api.v30.app188;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = { "springdoc.group-configs[0].group=mygroup", "springdoc.group-configs[0].paths-to-match=/test" })
class SpringDocApp188Test extends AbstractSpringDocTest {

	@Test
	void testApp1() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL +"/mygroup").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.0.1/app188.json"), true);
	}

	@SpringBootApplication
	@Import({
			OrderDemo.Customizer1.class,
			OrderDemo.Customizer2.class,
			OrderDemo.Customizer3.class,
	})
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v30.app188" })
	static class SpringDocTestApp {}

}
