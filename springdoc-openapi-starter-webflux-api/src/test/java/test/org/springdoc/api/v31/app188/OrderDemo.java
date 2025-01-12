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

package test.org.springdoc.api.v31.app188;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


public class OrderDemo {

	public static class Customizer2 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("2"));
		}
	}

	public static class Customizer3 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("3"));
		}
	}
	
	public static class Customizer1 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("1"));
		}
	}

	@RestController
	public static class MyController {

		@GetMapping("/test")
		public String testingMethod() {
			return "foo";
		}
	}

}