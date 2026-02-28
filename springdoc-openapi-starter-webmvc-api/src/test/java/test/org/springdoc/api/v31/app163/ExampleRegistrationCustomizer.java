/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app163;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.stereotype.Component;

@Component
public class ExampleRegistrationCustomizer implements OpenApiCustomizer {

	private final List<Map.Entry<String, Example>> examplesToRegister;

	public ExampleRegistrationCustomizer(List<Map.Entry<String, Example>> examplesToRegister) {
		this.examplesToRegister = examplesToRegister;
	}

	@Override
	public void customise(OpenAPI openApi) {
		examplesToRegister.forEach(entry -> openApi.getComponents().addExamples(entry.getKey(), entry.getValue()));
	}
}
