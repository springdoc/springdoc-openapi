/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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
 *
 */
package test.org.springdoc.api.app126;

import java.util.List;
import java.util.Map;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import io.swagger.v3.oas.models.responses.ApiResponse;
import test.org.springdoc.api.AbstractSpringDocTest;


/**
 * Tests Spring meta-annotations as method parameters
 */
public class SpringDocApp126Test extends AbstractSpringDocTest {

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		public OpenApiCustomiser responseRegistrationCustomizer(List<Map.Entry<String, ApiResponse>> responsesToRegister) {
			ResolvedSchema resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(Problem.class));
			return openApi -> {
				openApi.getComponents().addSchemas("Problem", resolvedSchema.schema);
				responsesToRegister.forEach(entry -> openApi.getComponents().addResponses(entry.getKey(), entry.getValue()));
			};
		}
		
	}
}
