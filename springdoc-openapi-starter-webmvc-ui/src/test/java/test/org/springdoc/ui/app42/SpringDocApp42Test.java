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

package test.org.springdoc.ui.app42;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests per-request Swagger UI index transformations.
 *
 * @author limehee
 */
public class SpringDocApp42Test extends AbstractSpringDocTest {

	@Test
	void indexPageTransformerRunsForEveryRequest() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html").requestAttr("cspNonce", "nonce-a"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("nonce=\"nonce-a\"")))
				.andExpect(content().string(not(containsString("nonce=\"nonce-b\""))));

		mockMvc.perform(get("/swagger-ui/index.html").requestAttr("cspNonce", "nonce-b"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("nonce=\"nonce-b\"")))
				.andExpect(content().string(not(containsString("nonce=\"nonce-a\""))));
	}

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		SwaggerIndexTransformer swaggerIndexTransformer(SwaggerUiConfigProperties swaggerUiConfig,
				SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerWelcomeCommon swaggerWelcomeCommon,
				ObjectMapperProvider objectMapperProvider) {
			return new NonceSwaggerIndexTransformer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon,
					objectMapperProvider);
		}

	}

	static class NonceSwaggerIndexTransformer extends SwaggerIndexPageTransformer {

		NonceSwaggerIndexTransformer(SwaggerUiConfigProperties swaggerUiConfig,
				SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerWelcomeCommon swaggerWelcomeCommon,
				ObjectMapperProvider objectMapperProvider) {
			super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
		}

		@Override
		public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
				throws IOException {
			Resource transformedResource = super.transform(request, resource, transformerChain);
			if (request.getRequestURI().endsWith("/index.html")) {
				String html = new String(transformedResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
				String nonce = request.getAttribute("cspNonce").toString();
				html = html.replace("<script ", "<script nonce=\"" + nonce + "\" ");
				return new TransformedResource(resource, html.getBytes(StandardCharsets.UTF_8));
			}
			return transformedResource;
		}

	}

}
