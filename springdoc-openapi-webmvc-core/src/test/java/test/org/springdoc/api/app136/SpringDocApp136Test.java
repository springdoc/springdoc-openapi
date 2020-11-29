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
package test.org.springdoc.api.app136;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.RepeatedTest;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import test.org.springdoc.api.AbstractCommonTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springdoc.core.Constants.SPRINGDOC_CACHE_DISABLED;


/**
 * Tests deterministic creation of operationIds
 */
@SpringBootTest(properties = {SPRINGDOC_CACHE_DISABLED + "=true"})
public class SpringDocApp136Test extends AbstractCommonTest {

    @Autowired
	OpenApiWebMvcResource resource;

    @Autowired
    RequestMappingInfoHandlerMapping mappingInfoHandlerMapping;

    @SpringBootApplication
    static class SpringDocTestApp {}

    @RepeatedTest(10)
    public void shouldGenerateOperationIdsDeterministically() throws Exception {
        shuffleSpringHandlerMethods();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));

        String expected = getContent("results/app136.json");
        String openApi = resource.openapiJson(request, "");
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
