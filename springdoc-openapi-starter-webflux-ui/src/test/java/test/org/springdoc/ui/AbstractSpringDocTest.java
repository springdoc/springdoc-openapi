/*
 *
 *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.ui;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.utils.Constants;
import org.springdoc.webflux.core.configuration.SpringDocWebFluxConfiguration;
import org.springdoc.webflux.ui.SwaggerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WebFluxTest
@ContextConfiguration(classes = { SpringDocConfiguration.class, SpringDocConfigProperties.class,
		SpringDocWebFluxConfiguration.class, SwaggerUiConfigParameters.class, SwaggerUiConfigProperties.class,
		SwaggerConfig.class, SwaggerUiOAuthProperties.class })
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

	private static final String DEFAULT_SWAGGER_INITIALIZER_URL = Constants.SWAGGER_INITIALIZER_URL;

	@Autowired
	protected WebTestClient webTestClient;

	protected void checkJS(String fileName, String uri) {
		EntityExchangeResult<byte[]> getResult = webTestClient.get().uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBody().returnResult();
		checkJSResult(fileName, new String(getResult.getResponseBody()));
	}

	protected void checkJSResult(String fileName, String result) {
		assertTrue(result.contains("window.ui"));
		String expected = getContent("results/" + fileName);
		assertEquals(expected, result.replace("\r", ""));
	}

	protected void checkJS(String fileName) {
		checkJS(fileName, DEFAULT_SWAGGER_INITIALIZER_URL);
	}
}
