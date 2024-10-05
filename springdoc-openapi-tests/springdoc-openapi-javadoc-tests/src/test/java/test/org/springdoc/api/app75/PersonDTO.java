/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.app75;

/**
 * The type Person dto.
 */
class PersonDTO {
	/**
	 * The Email.
	 */
	private String email;

	/**
	 * The First name.
	 */
	private String firstName;

	/**
	 * The Last name.
	 */
	private String lastName;

	/**
	 * Instantiates a new Person dto.
	 */
	public PersonDTO() {
	}

	/**
	 * Instantiates a new Person dto.
	 *
	 * @param email the email 
	 * @param firstName the first name 
	 * @param lastName the last name
	 */
	public PersonDTO(final String email, final String firstName, final String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
}
