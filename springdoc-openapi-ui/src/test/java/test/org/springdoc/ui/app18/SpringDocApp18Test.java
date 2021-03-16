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

package test.org.springdoc.ui.app18;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import test.org.springdoc.ui.AbstractSpringDocActuatorTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"management.endpoints.web.exposure.include:*",
                "springdoc.use-management-port=true",
                "springdoc.swagger-ui.try-it-out-enabled=true",
                "management.server.port=9095",
                "management.server.base-path=/test",
                "management.endpoints.web.base-path=/application"
        })
class SpringDocApp18Test extends AbstractSpringDocActuatorTest {

    @Test
    public void testIndexSwaggerConfigTryItOutEnabledExists() throws Exception {
        String contentAsString = actuatorRestTemplate.getForObject("/test/application/swaggerui/swagger-config", String.class);
        String expected = getContent("results/app18-1.json");
        JSONAssert.assertEquals(expected, contentAsString, true);
    }

    @SpringBootApplication
    static class SpringDocTestApp {
    }

}