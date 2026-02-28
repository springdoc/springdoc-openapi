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

package test.org.springdoc.api.v31.app265.user;

import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 *
 * @author bnasslahsen
 */
@Component
public class UserMapper {

	/**
	 * Map user to v1 DTO.
	 * @param user the user
	 * @return the user DTO v1
	 */
	public UserDTOv1 toV1(User user) {
		return new UserDTOv1(user.id(), user.name(), user.email());
	}

	/**
	 * Map user to v2 DTO.
	 * @param user the user
	 * @return the user DTO v2
	 */
	public UserDTOv2 toV2(User user) {
		String[] nameParts = splitName(user.name());
		return new UserDTOv2(user.id(), nameParts[0], nameParts[1], user.email());
	}

	/**
	 * Map v1 DTO to user.
	 * @param dto the dto
	 * @return the user
	 */
	public User fromV1(UserDTOv1 dto) {
		return new User(dto.id(), dto.name(), dto.email());
	}

	/**
	 * Map v2 DTO to user.
	 * @param dto the dto
	 * @return the user
	 */
	public User fromV2(UserDTOv2 dto) {
		String combinedName = combineName(dto.firstName(), dto.lastName());
		return new User(dto.id(), combinedName, dto.email());
	}

	private String[] splitName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty()) {
			return new String[] { "", "" };
		}
		String trimmed = fullName.trim();
		int lastSpaceIndex = trimmed.lastIndexOf(' ');
		if (lastSpaceIndex == -1) {
			return new String[] { trimmed, "" };
		}
		return new String[] { trimmed.substring(0, lastSpaceIndex), trimmed.substring(lastSpaceIndex + 1) };
	}

	private String combineName(String firstName, String lastName) {
		firstName = firstName != null ? firstName.trim() : "";
		lastName = lastName != null ? lastName.trim() : "";
		if (firstName.isEmpty()) {
			return lastName;
		}
		if (lastName.isEmpty()) {
			return firstName;
		}
		return firstName + " " + lastName;
	}

}
