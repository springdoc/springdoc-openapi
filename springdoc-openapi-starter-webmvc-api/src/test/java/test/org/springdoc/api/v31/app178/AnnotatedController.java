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

package test.org.springdoc.api.v31.app178;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import org.springdoc.core.customizers.OperationCustomizer;
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
	@PostMapping("/annotated")
	public String annotatedPost() {
		return "annotated";
	}

	@Group2
	@PutMapping("/annotated")
	public String annotatedPut() {
		return "annotated";
	}


	@Bean
	public GroupedOpenApi group1OpenApi() {
		Predicate<Method> hidingCondition = method -> method.isAnnotationPresent(Group1.class);
		return GroupedOpenApi.builder()
				.group("annotatedGroup1")
				.addOperationCustomizer(getOperationCustomizer(hidingCondition))
				.build();
	}

	@Bean
	public GroupedOpenApi group2OpenApi() {
		Predicate<Method> filterCondition = method -> method.isAnnotationPresent(Group2.class);
		return GroupedOpenApi.builder()
				.group("annotatedGroup2")
				.addOperationCustomizer(getOperationCustomizer(filterCondition))
				.build();
	}

	@Bean
	public GroupedOpenApi group3OpenApi() {
		Predicate<Method> hidingCondition = method -> method.isAnnotationPresent(Group1.class) || method.isAnnotationPresent(Group2.class);
		return GroupedOpenApi.builder()
				.group("annotatedCombinedGroup")
				.addOperationCustomizer(getOperationCustomizer(hidingCondition))
				.build();
	}

	private OperationCustomizer getOperationCustomizer(Predicate<Method> filterCondition) {
		return (operation, handlerMethod) -> filterCondition.test(handlerMethod.getMethod()) ? operation : null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group1 {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group2 {

	}
}
