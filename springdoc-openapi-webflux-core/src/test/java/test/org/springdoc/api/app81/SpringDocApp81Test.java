/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 *
 */
package test.org.springdoc.api.app81;

import org.junit.jupiter.api.RepeatedTest;
import org.springdoc.webflux.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springdoc.core.Constants.SPRINGDOC_CACHE_DISABLED;
import static test.org.springdoc.api.AbstractSpringDocTest.getContent;


/**
 * Tests deterministic creation of operationIds
 */
@AutoConfigureWebTestClient(timeout = "3600000")
@WebFluxTest(properties = SPRINGDOC_CACHE_DISABLED + "=true")
@ActiveProfiles("test")
public class SpringDocApp81Test {

    @SpringBootApplication
    @ComponentScan(basePackages = {"org.springdoc", "test.org.springdoc.api.app81"})
    static class SpringDocTestApp {
    }

    @Autowired
    OpenApiResource resource;

    @Autowired
    RequestMappingInfoHandlerMapping mappingInfoHandlerMapping;

    @RepeatedTest(10)
    public void shouldGenerateOperationIdsDeterministically() throws Exception {
        shuffleSpringHandlerMethods();

        ServerHttpRequest request = mock(ServerHttpRequest.class);
        when(request.getURI()).thenReturn(URI.create("http://localhost"));

        String expected = getContent("results/app81.json");
        String openApi = resource.openapiJson(request, "").block();
        assertEquals(expected, openApi, true);
    }

    private void shuffleSpringHandlerMethods() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingInfoHandlerMapping.getHandlerMethods();
        List<Map.Entry<RequestMappingInfo, HandlerMethod>> collect = new ArrayList<>(handlerMethods.entrySet());
        collect.sort(Comparator.comparing(a -> ThreadLocalRandom.current().nextBoolean() ? -1 : 1));

        collect.forEach(e -> mappingInfoHandlerMapping.unregisterMapping(e.getKey()));
        collect.forEach(e -> mappingInfoHandlerMapping.registerMapping(e.getKey(), e.getValue().getBean(), e.getValue().getMethod()));
    }

}
