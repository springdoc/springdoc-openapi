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
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app9;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FooConfiguration {
	@Bean
	OpenAPI customOpenApi() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearerScheme",
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT"))
						.addSchemas("FeedResponse", feedResponseSchema()))
				.info(new Info()
						.title("Response API")
						.description("API for some response")
						.version("0.0.1")
						.contact(new Contact()
								.name("EAlf91")))
				.tags(List.of(new Tag()
								.name("ResponseTag")
								.description("ResponseTag for API"),
						new Tag()
								.name("ResponseData")
								.description("Version 2 ResponseApi")));

	}

	private Schema<?> feedResponseSchema() {
		Schema<?> schema = new Schema<>();
		schema.addProperty("_links", linkSchema());
		schema.addProperty("data", new ArraySchema().items(new Schema<>().$ref("#/components/schemas/ResponseData")));
		return schema;
	}

	private Schema<?> linkSchema() {
		Schema<?> linkSchema = new Schema<>();
		linkSchema.addProperty("next", new Schema<>().$ref("#/components/schemas/Link").example("http://localhost:8080/some-link"));
		linkSchema.addProperty("self", new Schema<>().$ref("#/components/schemas/Link").example("http://localhost:8080/some-other-link"));
		return linkSchema;
	}

}
