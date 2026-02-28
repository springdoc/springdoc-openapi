/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2026 the original author or authors.
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
 */

package test.org.springdoc.api.v30.app199;


import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.OperationCustomizer;
import test.org.springdoc.api.v30.app199.CustomExceptionHandler.MyInternalException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class HelloController {

	@Autowired
	ObjectMapper defaultObjectMapper;

	@GetMapping(
			value = "/first",
			produces = APPLICATION_JSON_VALUE
	)
	public void first() throws MyInternalException {
	}

	@GetMapping(
			value = "/second",
			produces = APPLICATION_JSON_VALUE
	)
	public void second() throws MyInternalException {
	}

	@Bean
	public OperationCustomizer operationCustomizer() {
		return (Operation operation, HandlerMethod handlerMethod) ->
		{
			final io.swagger.v3.oas.models.media.MediaType mediaType = operation
					.getResponses()
					.get("500")
					.getContent()
					.get(ALL_VALUE);

			mediaType.setExamples(mediaType.getExamples() != null
					? mediaType.getExamples()
					: new LinkedHashMap<>());

			final Map<String, Example> examples = mediaType.getExamples();

			switch (handlerMethod.getMethod().getName()) {

				case "first":
					examples.put(
							"First case example",
							new Example().value(
									defaultObjectMapper.valueToTree(
											new ErrorDto("An ErrorDto sample specific to /first endpoint")
									)
							)
					);
					break;

				case "second":
					examples.put(
							"Second case example",
							new Example().value(
									defaultObjectMapper.valueToTree(
											new ErrorDto("An ErrorDto sample specific to /second endpoint")
									)
							)
					);
					break;
			}

			return operation;
		};
	}
}