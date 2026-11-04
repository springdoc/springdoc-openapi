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

package test.org.springdoc.api.v31.app206;

import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PersonRequest {
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private long id;

	@Schema(requiredMode = RequiredMode.NOT_REQUIRED)
	private String firstName;

	@NotBlank
	@Size(max = 10)
	private String lastName;

	@Schema(requiredMode = RequiredMode.AUTO)
	private Optional<String> email;

	@Schema(required = true)
	private int age;

	public PersonRequest(long id, String firstName, String lastName, Optional<String> email, int age) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Optional<String> getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}
}
