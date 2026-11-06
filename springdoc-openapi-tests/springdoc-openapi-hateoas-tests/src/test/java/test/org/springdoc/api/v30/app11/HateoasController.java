/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v30.app11;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "hateoas-controller", description = "Hateoas Controller")
public class HateoasController {

    @GetMapping(path = "/test-dto", produces = "application/json")
    @Operation(summary = "Get Test DTO", description = "Returns a TestDto with HATEOAS links")
    public TestDto getTestDto() {
        TestDto dto = new TestDto();
        dto.setField("test field value");
        return dto;
    }

    @GetMapping(path = "/extended-test-dto", produces = "application/json")
    @Operation(summary = "Get Extended Test DTO", description = "Returns an ExtendedTestDto with HATEOAS links")
    public ExtendedTestDto getExtendedTestDto() {
        ExtendedTestDto dto = new ExtendedTestDto();
        dto.setField("parent field value");
        dto.setOtherField("extended field value");
        return dto;
    }
}
