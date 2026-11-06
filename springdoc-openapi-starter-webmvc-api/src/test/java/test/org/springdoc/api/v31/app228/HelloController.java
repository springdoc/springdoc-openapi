/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app228;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {


	@PostMapping("swaggerTest")
	public String swaggerTest(@RequestBody MyRequest myRequest) {
		return null;
	}

	public class MyRequest {
		@Schema(oneOf = { Child1.class, Child2.class })
		@JsonProperty
		private Parent parent;
	}

	@JsonSubTypes({
			@JsonSubTypes.Type(value = Child1.class),
			@JsonSubTypes.Type(value = Child2.class),
			@JsonSubTypes.Type(value = Child3.class),
	})
	public abstract class Parent {
		@JsonProperty
		private String parentProperty;
	}

	public class Child1 extends Parent {
		@JsonProperty
		private String childProperty1;
	}

	public class Child2 extends Parent {
		@JsonProperty
		private String childProperty2;
	}

	public class Child3 extends Parent {
		@JsonProperty
		private String childProperty3;
	}

}