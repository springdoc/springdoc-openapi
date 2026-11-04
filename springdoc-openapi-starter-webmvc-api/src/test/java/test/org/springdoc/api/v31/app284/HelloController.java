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
package test.org.springdoc.api.v31.app284;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping("/outer")
	public void outer(@RequestBody Outer body) {
	}

	@PostMapping("/both")
	public void both(@RequestBody BothHolder body) {
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
	@JsonSubTypes({ @JsonSubTypes.Type(value = Circle.class, name = "CIRCLE"),
			@JsonSubTypes.Type(value = Square.class, name = "SQUARE") })
	public abstract static class Shape {

	}

	public static class Circle extends Shape {

		public double radius;

	}

	public static class Square extends Shape {

		public double side;

	}

	public static class NarrowedHolder {

		@Schema(oneOf = Circle.class)
		public Shape shape;

	}

	public static class PlainHolder {

		public Shape shape;

	}

	public static class Outer {

		public NarrowedHolder first;

		public PlainHolder second;

	}

	public static class BothHolder {

		@Schema(oneOf = Circle.class)
		public Shape narrowed;

		public Shape plain;

	}

}
