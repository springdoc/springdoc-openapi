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

package test.org.springdoc.api.app39;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The type Spring doc test app.
 */
@SpringBootApplication
class SpringDocTestApp {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringDocTestApp.class, args);
	}

	/**
	 * Custom open api open api.
	 *
	 * @return the open api
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		StringSchema schema = new StringSchema();
		return new OpenAPI()
				.components(new Components().addParameters("myGlobalHeader", new HeaderParameter().required(true).name("My-Global-Header").description("My Global Header").schema(schema)));
	}

	/**
	 * Customer global header open api customiser open api customiser.
	 *
	 * @return the open api customiser
	 */
	@Bean
	public OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
		return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> operation.addParametersItem(new HeaderParameter().$ref("#/components/parameters/myGlobalHeader")));
	}
}
