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

package test.org.springdoc.ui.app4;


import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDocTestApp {

	@Bean
	public GroupedOpenApi storeOpenApi() {
		String[] paths = { "/store/**" };
		return GroupedOpenApi.builder()
				.group("stores")
				.pathsToMatch(paths)
				.build();
	}

	@Bean
	public GroupedOpenApi groupOpenApi() {
		String[] paths = { "/pet/**" };
		return GroupedOpenApi.builder()
				.group("pets")
				.displayName("zpets")
				.pathsToMatch(paths)
				.build();
	}

}