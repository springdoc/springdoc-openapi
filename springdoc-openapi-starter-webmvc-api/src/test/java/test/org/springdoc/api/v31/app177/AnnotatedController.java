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

package test.org.springdoc.api.v31.app177;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotatedController {

	@Group1
	@GetMapping("/annotated")
	public String annotatedGet() {
		return "annotated";
	}

	@Group1
	@Group3
	@PostMapping("/annotated")
	public String annotatedPost() {
		return "annotated";
	}

	@Group2
	@Group3
	@PutMapping("/annotated")
	public String annotatedPut() {
		return "annotated";
	}

	@PostMapping("/notAnnotated")
	public String notAnnotatedPost() {
		return "annotated";
	}

	@Bean
	public List<GroupedOpenApi> apis() {
		GroupedOpenApi group1OpenApi = GroupedOpenApi.builder()
				.group("annotatedGroup1")
				.addOpenApiMethodFilter(method -> method.isAnnotationPresent(Group1.class))
				.build();

		GroupedOpenApi group2OpenApi = GroupedOpenApi.builder()
				.group("annotatedGroup2")
				.addOpenApiMethodFilter(method -> method.isAnnotationPresent(Group2.class))
				.build();

		GroupedOpenApi group3OpenApi = GroupedOpenApi.builder()
				.group("annotatedCombinedGroup")
				.addOpenApiMethodFilter(method -> method.isAnnotationPresent(Group1.class) || method.isAnnotationPresent(Group2.class))
				.build();

		return Arrays.asList(group1OpenApi, group2OpenApi, group3OpenApi);
	}

	@Bean
	public OpenApiMethodFilter methodFilter() {
		return method -> method.isAnnotationPresent(Group3.class);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group1 {}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group2 {}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group3 {}
}
