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
package test.org.springdoc.api.v30.app81;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.RepeatedTest;
import org.springdoc.webflux.api.OpenApiWebfluxResource;
import test.org.springdoc.api.v30.AbstractCommonTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springdoc.core.utils.Constants.SPRINGDOC_CACHE_DISABLED;


/**
 * Tests deterministic creation of operationIds
 */
@WebFluxTest(properties = SPRINGDOC_CACHE_DISABLED + "=true")
public class SpringDocApp81Test extends AbstractCommonTest {

	@Autowired
	OpenApiWebfluxResource resource;

	@Autowired
	RequestMappingInfoHandlerMapping mappingInfoHandlerMapping;

	@RepeatedTest(10)
	public void shouldGenerateOperationIdsDeterministically() throws Exception {
		shuffleSpringHandlerMethods();

		ServerHttpRequest request = mock(ServerHttpRequest.class);
		when(request.getURI()).thenReturn(URI.create("http://localhost"));

		String expected = getContent("results/3.0.1/app81.json");
		byte[] openApiBytes = resource.openapiJson(request, "", Locale.US).block();
		String openApi = new String(openApiBytes, StandardCharsets.UTF_8); // for UTF-8 encoding		String openApi = resource.openapiJson(request, "", Locale.US).block();
		assertEquals(expected, openApi, true);
	}

	private void shuffleSpringHandlerMethods() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingInfoHandlerMapping.getHandlerMethods();
		List<Map.Entry<RequestMappingInfo, HandlerMethod>> collect = new ArrayList<>(handlerMethods.entrySet());
		collect.sort(Comparator.comparing(a -> ThreadLocalRandom.current().nextBoolean() ? -1 : 1));

		collect.forEach(e -> mappingInfoHandlerMapping.unregisterMapping(e.getKey()));
		collect.forEach(e -> mappingInfoHandlerMapping.registerMapping(e.getKey(), e.getValue().getBean(), e.getValue().getMethod()));
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v30.app81" })
	static class SpringDocTestApp {
	}

}
