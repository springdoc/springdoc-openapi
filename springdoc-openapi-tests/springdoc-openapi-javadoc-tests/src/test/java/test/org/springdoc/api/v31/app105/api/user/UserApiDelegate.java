/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app105.api.user;

import java.util.List;
import java.util.Optional;

import test.org.springdoc.api.v31.app105.api.ApiUtil;
import test.org.springdoc.api.v31.app105.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * A delegate to be called by the {@link UserApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@jakarta.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

public interface UserApiDelegate {

	/**
	 * Gets request.
	 *
	 * @return the request
	 */
	default Optional<NativeWebRequest> getRequest() {
		return Optional.empty();
	}

	/**
	 * Create user response entity.
	 *
	 * @param user the user
	 * @return the response entity
	 * @see UserApi#createUser UserApi#createUser
	 */
	default ResponseEntity<Void> createUser(User user) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Create users with array input response entity.
	 *
	 * @param user the user
	 * @return the response entity
	 * @see UserApi#createUsersWithArrayInput UserApi#createUsersWithArrayInput
	 */
	default ResponseEntity<Void> createUsersWithArrayInput(List<User> user) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Create users with list input response entity.
	 *
	 * @param user the user
	 * @return the response entity
	 * @see UserApi#createUsersWithListInput UserApi#createUsersWithListInput
	 */
	default ResponseEntity<Void> createUsersWithListInput(List<User> user) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Delete user response entity.
	 *
	 * @param username the username
	 * @return the response entity
	 * @see UserApi#deleteUser UserApi#deleteUser
	 */
	default ResponseEntity<Void> deleteUser(String username) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Gets user by name.
	 *
	 * @param username the username
	 * @return the user by name
	 * @see UserApi#getUserByName UserApi#getUserByName
	 */
	default ResponseEntity<User> getUserByName(String username) {
		getRequest().ifPresent(request -> {
			for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
				if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
					ApiUtil.setExampleResponse(request, "application/json", "{  \"firstName\" : \"firstName\",  \"lastName\" : \"lastName\",  \"password\" : \"password\",  \"userStatus\" : 6,  \"phone\" : \"phone\",  \"id\" : 0,  \"email\" : \"email\",  \"username\" : \"username\"}");
					break;
				}
				if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
					ApiUtil.setExampleResponse(request, "application/xml", "<User>  <id>123456789</id>  <username>aeiou</username>  <firstName>aeiou</firstName>  <lastName>aeiou</lastName>  <email>aeiou</email>  <password>aeiou</password>  <phone>aeiou</phone>  <userStatus>123</userStatus></User>");
					break;
				}
			}
		});
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Login user response entity.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the response entity
	 * @see UserApi#loginUser UserApi#loginUser
	 */
	default ResponseEntity<String> loginUser(String username,
			String password) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Logout user response entity.
	 *
	 * @return the response entity
	 * @see UserApi#logoutUser UserApi#logoutUser
	 */
	default ResponseEntity<Void> logoutUser() {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * Update user response entity.
	 *
	 * @param username the username
	 * @param user     the user
	 * @return the response entity
	 * @see UserApi#updateUser UserApi#updateUser
	 */
	default ResponseEntity<Void> updateUser(String username,
			User user) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

}
