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

package test.org.springdoc.api.v30.app134;

import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public GroupedOpenApi groupV1OpenApi() {
		return GroupedOpenApi.builder()
				.group("v1-group").producesToMatch(HelloController.VERSION_1)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV2OpenApi() {
		return GroupedOpenApi.builder()
				.group("v2-group").producesToMatch(HelloController.VERSION_2)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV3OpenApi() {
		return GroupedOpenApi.builder()
				.group("v2-consumes-group").consumesToMatch(HelloController.VERSION_2)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV4OpenApi() {
		return GroupedOpenApi.builder()
				.group("v1-headers-group").headersToMatch(HelloController.HEADER_1)
				.build();
	}

	@Bean
	public GroupedOpenApi groupV5OpenApi() {
		return GroupedOpenApi.builder()
				.group("v1-v2-headers-group").headersToMatch(HelloController.HEADER_1, HelloController.HEADER_2)
				.build();
	}
}
