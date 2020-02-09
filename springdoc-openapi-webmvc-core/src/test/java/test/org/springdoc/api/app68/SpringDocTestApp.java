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

package test.org.springdoc.api.app68;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDocTestApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringDocTestApp.class, args);
	}

	@Bean
	public GroupedOpenApi storeOpenApi() {
		return GroupedOpenApi.builder()
				.setGroup("stores")
				.pathsToMatch("/store/**")
				.build();
	}

	@Bean
	public GroupedOpenApi userOpenApi() {
		return GroupedOpenApi.builder()
				.setGroup("users")
				.packagesToScan("test.org.springdoc.api.app68.api.user")
				.build();
	}

	@Bean
	public GroupedOpenApi petOpenApi() {
		return GroupedOpenApi.builder()
				.setGroup("pets")
				.pathsToMatch("/pet/**")
				.build();
	}

	@Bean
	public GroupedOpenApi groupOpenApi() {
		return GroupedOpenApi.builder()
				.setGroup("groups test")
				.pathsToMatch("/v1/**").pathsToExclude("/v1/users")
				.packagesToScan("test.org.springdoc.api.app68.api.user", "test.org.springdoc.api.app68.api.store")
				.build();
	}


	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicScheme",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.info(new Info().title("Petstore API").version("v0").description(
						"This is a sample server Petstore server.  You can find out more about     Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).      For this sample, you can use the api key `special-key` to test the authorization     filters.")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
