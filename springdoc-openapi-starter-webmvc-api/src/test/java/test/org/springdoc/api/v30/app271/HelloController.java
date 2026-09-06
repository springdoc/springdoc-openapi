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

package test.org.springdoc.api.v30.app271;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A merge-patch model with optional, nullable and explicitly required properties.
 *
 * @author dpkass
 */
@RestController
public class HelloController {

	@PatchMapping("/example")
	public Patch patch(@RequestBody Patch patch) {
		return patch;
	}

	@PatchMapping("/bean")
	public Bean patchBean(@RequestBody Bean bean) {
		return bean;
	}

	public record Patch(
			JsonNullable<String> nullable,
			@NotNull JsonNullable<String> nonNull,
			JsonNullable<@NotNull String> innerNonNull,
			JsonNullable<@NotBlank @Size(max = 20) String> innerNonBlank,
			@jakarta.annotation.Nullable JsonNullable<@NotNull String> outerNullable,
			@NotNull JsonNullable<@Nullable String> innerNullable,
			@NonNull JsonNullable<String> nonNullAlias,
			@NotBlank @Size(max = 20) JsonNullable<String> nonBlank,
			@NotEmpty JsonNullable<List<String>> nonEmpty,
			JsonNullable<List<String>> list,
			JsonNullable<Map<String, String>> map,
			JsonNullable<Child> child,
			@NotNull JsonNullable<Child> nonNullChild,
			@JsonProperty("renamed") @NotNull JsonNullable<String> original,
			@Schema(requiredMode = Schema.RequiredMode.REQUIRED) JsonNullable<String> required,
			@NotNull String ordinary) {}

	public record Child(@NotNull String name) {}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface NonNull {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE_USE)
	public @interface Nullable {}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Bean {
		public JsonNullable<@NotNull String> fieldValue;

		private JsonNullable<String> setterValue;

		private JsonNullable<String> getterValue;

		public JsonNullable<String> getSetterValue() {
			return setterValue;
		}

		@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
		public void setSetterValue(JsonNullable<@NotNull String> value) {
			setterValue = value;
		}

		public JsonNullable<@NotNull String> getGetterValue() {
			return getterValue;
		}

		public void setGetterValue(JsonNullable<String> value) {
			getterValue = value;
		}
	}
}
