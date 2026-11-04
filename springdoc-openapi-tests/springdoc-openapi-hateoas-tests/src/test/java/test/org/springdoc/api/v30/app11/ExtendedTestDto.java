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
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Extended DTO that inherits from TestDto using allOf composition.
 * This class verifies that the fix for issue #3161 works correctly,
 * ensuring _links is not duplicated in the child schema.
 */
@Schema(
        description = "Extended DTO with allOf composition",
        allOf = {TestDto.class}
)
public class ExtendedTestDto extends TestDto {

    private String otherField;

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }
}
