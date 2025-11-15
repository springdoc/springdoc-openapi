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
 *  *  *  *  *  *
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *
 */

package test.org.springdoc.api.v30.app244;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/test")
    @Operation(description = "Endpoint to request test resource")
    public void test(@RequestBody TestRequest request) {
    }

    @GetMapping("/test2")
    @Operation(description = "Endpoint to request test2 resource")  // ← test2
    public void test2(TestRequest2 request) {
    }

    @GetMapping("/test3")
    @Operation(description = "Endpoint to request test3 resource")  // ← test3
    public void test3(TestRequest3 request) {
    }

    @GetMapping("/test4")
    @Operation(description = "Endpoint to request test4 resource")  // ← test4
    public void test4(TestRequest4 request) {
    }

    @PostMapping("/test5")
    @Operation(description = "Endpoint to request test5 resource")
    public void test5(@RequestBody TestRequest5 request) {
    }
}