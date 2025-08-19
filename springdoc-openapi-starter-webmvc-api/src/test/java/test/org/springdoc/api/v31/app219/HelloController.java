/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app219;

import test.org.springdoc.api.v30.app219.TestObject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = {"application/xml"}, consumes = {"application/json"})
public class HelloController {

    @RequestMapping(value = "/testpost", method = RequestMethod.POST, produces = {"application/json"},
            consumes = {"application/json;charset=UTF-8", "application/json; charset=UTF-8"})
    public ResponseEntity<TestObject> postWithProducesAndConsumes(@RequestBody TestObject dto) {
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/testpost2", method = RequestMethod.POST, consumes = {"application/json;charset=UTF-8"})
    public ResponseEntity<TestObject> postWithConsumes(@RequestBody TestObject dto) {
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/testpost3", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<TestObject> postWithProduces(@RequestBody TestObject dto) {
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/testpost4", method = RequestMethod.POST)
    public ResponseEntity<TestObject> post(@RequestBody TestObject dto) {
        return ResponseEntity.ok(dto);
    }
}
