/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2024 the original author or authors.
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
package test.org.springdoc.api.v30.app233;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterController {

	@GetMapping("/hidden-parent")
	public void nestedParameterObjectWithHiddenParentField(@ParameterObject ParameterObjectWithHiddenField parameters) {

	}

	public record ParameterObjectWithHiddenField(
			@Schema(hidden = true) NestedParameterObject schemaHiddenNestedParameterObject,
			@Parameter(hidden = true) NestedParameterObject parameterHiddenNestedParameterObject,
			NestedParameterObject visibleNestedParameterObject
	) {

	}

	public record NestedParameterObject(
			String parameterField) {
	}

	@GetMapping("/renamed-parent")
	public void nestedParameterObjectWithRenamedParentField(@ParameterObject ParameterObjectWithRenamedField parameters) {

	}

	public record ParameterObjectWithRenamedField(
			@Schema(name = "schemaRenamed") NestedParameterObject schemaRenamedNestedParameterObject,
			@Parameter(name = "parameterRenamed") NestedParameterObject parameterRenamedNestedParameterObject,
			NestedParameterObject originalNameNestedParameterObject
	) {

	}

	@GetMapping("/optional-parent")
	public void nestedParameterObjectWithOptionalParentField(@ParameterObject ParameterObjectWithOptionalField parameters) {

	}

	public record ParameterObjectWithOptionalField(
			@Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED) NestedRequiredParameterObject schemaNotRequiredNestedParameterObject,
			@Parameter NestedRequiredParameterObject parameterNotRequiredNestedParameterObject,
			@Parameter(required = true) NestedRequiredParameterObject requiredNestedParameterObject
	) {

	}

	public record NestedRequiredParameterObject(
			@Schema(requiredMode = Schema.RequiredMode.REQUIRED) @NotNull String requiredParameterField) {
	}

}
