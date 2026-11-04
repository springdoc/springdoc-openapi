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

package test.org.springdoc.api.v31.app8;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "user info")
public record UserInfo(
		@Schema(description = "The user's name", requiredMode = REQUIRED, examples = { "Madoka", "Homura" })
		String name,
		@Schema(description = "The user's age", requiredMode = REQUIRED, examples = { "114", "514" })
		int age,
		@Schema(description = "The user's fooBar", requiredMode = REQUIRED)
		FooBar fooBar
) {
}
