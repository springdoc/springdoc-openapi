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

package test.org.springdoc.api.v30.app68;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.Constants;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SpringDocConfiguration {

	@Bean
	public GroupedOpenApi storeOpenApi() {
		return GroupedOpenApi.builder()
				.group("stores")
				.pathsToMatch("/store/**")
				.build();
	}

	@Bean
	public GroupedOpenApi userOpenApi() {
		return GroupedOpenApi.builder()
				.group("users")
				.packagesToScan("test.org.springdoc.api.v30.app68.api.user").addOpenApiCustomizer(serverOpenApiCustomizer1())
				.addOperationCustomizer(operationCustomizer())
				.build();
	}

	public OpenApiCustomizer serverOpenApiCustomizer1() {
		Server server = new Server().url("http://toto.v1.com").description("myserver1");
		List<Server> servers = new ArrayList<>();
		servers.add(server);
		return openApi -> openApi.setServers(servers);
	}

	public OpenApiCustomizer serverOpenApiCustomizer2() {
		Server server = new Server().url("http://toto.v2.com").description("myserver2");
		List<Server> servers = new ArrayList<>();
		servers.add(server);
		return openApi -> openApi.setServers(servers);
	}

	OperationCustomizer operationCustomizer() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			CustomizedOperation annotation = handlerMethod.getMethodAnnotation(CustomizedOperation.class);
			if (annotation != null) {
				operation.description(StringUtils.defaultIfBlank(operation.getDescription(), Constants.DEFAULT_DESCRIPTION) + ", " + annotation.addition());
			}
			return operation;
		};
	}

	@Bean
	public GroupedOpenApi petOpenApi() {
		return GroupedOpenApi.builder()
				.group("pets")
				.pathsToMatch("/pet/**").addOpenApiCustomizer(serverOpenApiCustomizer2())
				.build();
	}

	@Bean
	public GroupedOpenApi groupOpenApi() {
		return GroupedOpenApi.builder()
				.group("groups test")
				.pathsToMatch("/v1/**").pathsToExclude("/v1/users")
				.packagesToScan("test.org.springdoc.api.v30.app68.api.user", "test.org.springdoc.api.v30.app68.api.store")
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

	@SpringBootApplication
	static class SpringDocTestApp {}
}
