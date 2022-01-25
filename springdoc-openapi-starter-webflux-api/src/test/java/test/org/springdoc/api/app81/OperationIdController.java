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

package test.org.springdoc.api.app81;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationIdController {

    @GetMapping(path = "/test_0") // gets operationId opIdTest_3
    public Mono<String> opIdTest() {
        return null;
    }

    @GetMapping(path = "/test_1") // gets operationId opIdTest_2
    public Mono<String> opIdTest(@RequestParam String param) {
        return null;
    }

    @GetMapping(path = "/test_2") // gets operationId opIdTest_1
    public Mono<String> opIdTest(@RequestParam Integer param) {
        return null;
    }

    @GetMapping(path = "/test_3") // gets operationId opIdTest
    public Mono<String> opIdTest(@RequestParam Boolean param) {
        return null;
    }

}
