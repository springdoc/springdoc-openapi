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

package test.org.springdoc.api.v31.app280;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

public record Request(
		ChildWrapper1 childWrapper1,
		ChildWrapper2 childWrapper2) {

	public record ChildWrapper1(String wrapper1Field, Child child) {}

	public record ChildWrapper2(String wrapper2Field, Child child) {}

	@Schema(discriminatorMapping = {
			@DiscriminatorMapping(value = "childA", schema = ChildA.class),
			@DiscriminatorMapping(value = "childB", schema = ChildB.class)
	})
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
	@JsonSubTypes({
			@JsonSubTypes.Type(value = ChildA.class, name = "childA"),
			@JsonSubTypes.Type(value = ChildB.class, name = "childB")
	})
	public sealed interface Child permits ChildA, ChildB {
		String type();
	}

	public record ChildA(String fieldA) implements Child {
		@Override
		public String type() {
			return "childA";
		}
	}

	public record ChildB(String fieldB, int numberB) implements Child {
		@Override
		public String type() {
			return "childB";
		}
	}

}
