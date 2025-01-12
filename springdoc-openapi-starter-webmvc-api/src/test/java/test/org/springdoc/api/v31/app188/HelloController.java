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

package test.org.springdoc.api.v31.app188;


import java.time.Instant;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

@RestController
public class HelloController {

	@GetMapping("/test")
	public void test() {
	}

	@GetMapping(value = "/example/{fooBar}")
	public String getFooBar(@PathVariable FooBar fooBar) {
		return fooBar.name();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().components(new Components());
	}

	@Bean
	public OperationCustomizer operationCustomizer(OpenAPI api) {
		io.swagger.v3.oas.models.media.Schema errorResponseSchema = SpringDocAnnotationsUtils.extractSchema(
				api.getComponents(),
				ErrorResponse.class,
				null,
				null, SpecVersion.V31
		);

		ApiResponse errorApiResponse = new ApiResponse().content(new Content().addMediaType(
				MediaType.APPLICATION_JSON_VALUE,
				new io.swagger.v3.oas.models.media.MediaType().schema(errorResponseSchema)
		));

		return (Operation operation, HandlerMethod handlerMethod) -> {
			operation.getResponses().addApiResponse("5xx", errorApiResponse);
			return operation;
		};
	}

	public class ErrorResponse {
		@Schema(example = "2022-05-09T00:00:00.000Z")
		Instant timestamp;

		@Schema(example = "{\"param1\":\"val1\",\"param2\":\"val2\"}")
		Map<String, Object> data;

		public Instant getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Instant timestamp) {
			this.timestamp = timestamp;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}
	}
}