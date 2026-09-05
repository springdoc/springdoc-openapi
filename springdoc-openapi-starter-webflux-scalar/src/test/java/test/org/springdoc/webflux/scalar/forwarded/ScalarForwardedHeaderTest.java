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

package test.org.springdoc.webflux.scalar.forwarded;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Adding the Scalar starter must not opt the whole application into trusting the
 * {@code Forwarded} / {@code X-Forwarded-*} headers of every caller. That decision belongs
 * to {@code server.forward-headers-strategy}, which defaults to {@code none}.
 *
 * @author bnasslahsen
 */
@ActiveProfiles("test")
@SpringBootTest
class ScalarForwardedHeaderTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void testNoForwardedHeaderTransformerIsRegistered() {
		assertThat(applicationContext.getBeanNamesForType(ForwardedHeaderTransformer.class)).isEmpty();
	}

	/**
	 * Once the application does opt in, Spring Boot still registers the transformer, so the
	 * documented way of running Scalar behind a trusted proxy keeps working.
	 */
	@Nested
	@SpringBootTest(properties = "server.forward-headers-strategy=framework")
	class WhenTheApplicationOptsIn {

		@Autowired
		private ApplicationContext nestedApplicationContext;

		@Test
		void testForwardedHeaderTransformerIsRegistered() {
			assertThat(nestedApplicationContext.getBeanNamesForType(ForwardedHeaderTransformer.class)).isNotEmpty();
		}

	}

	@SpringBootApplication
	static class SpringDocTestApp {

	}

}
