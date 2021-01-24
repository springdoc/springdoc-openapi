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

package test.org.springdoc.ui.app1;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpringDocRedirectOriginalQueryParamsTest extends AbstractSpringDocTest {

    @Test
    public void shouldRedirectWithOriginalQueryParams() throws Exception {
        mockMvc.perform(get("/swagger-ui.html").queryParam("paramA", "123").queryParam("paramB", "e n c o d e d ! % &"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location",
                        "/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config&paramA=123&paramB=e%20n%20c%20o%20d%20e%20d%20!%20%25%20%26"));
    }

    @Test
    public void shouldRedirectWithOriginalQueryParamsHavingMultipleValues() throws Exception {
        mockMvc.perform(get("/swagger-ui.html").queryParam("paramA", "1", "2", "3"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location",
                        "/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config&paramA=1&paramA=2&paramA=3"));
    }

    @SpringBootApplication
    static class SpringDocTestApp {
    }

}