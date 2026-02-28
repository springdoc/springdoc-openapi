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

package test.org.springdoc.api.v31.app90;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * User
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-11-30T09:49:26.034469-01:00[Atlantic/Azores]")
class User {

	/**
	 * The Id.
	 */
	@JsonProperty("id")

	private Long id;


	/**
	 * The Username.
	 */
	@JsonProperty("username")

	private String username;


	/**
	 * The First name.
	 */
	@JsonProperty("firstName")

	private String firstName;


	/**
	 * The Last name.
	 */
	@JsonProperty("lastName")

	private String lastName;


	/**
	 * The Email.
	 */
	@JsonProperty("email")

	private String email;


	/**
	 * The Password.
	 */
	@JsonProperty("password")

	private String password;


	/**
	 * The Phone.
	 */
	@JsonProperty("phone")

	private String phone;


	/**
	 * The User status.
	 */
	@JsonProperty("userStatus")

	private Integer userStatus;


	/**
	 * Id user.
	 *
	 * @param id the id
	 * @return the user
	 */
	public User id(Long id) {
		this.id = id;
		return this;
	}


	/**
	 * Get id
	 *
	 * @return id id
	 */
	@Schema(example = "10", description = "")


	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * Username user.
	 *
	 * @param username the username
	 * @return the user
	 */
	public User username(String username) {
		this.username = username;
		return this;
	}


	/**
	 * Get username
	 *
	 * @return username username
	 */
	@Schema(example = "theUser", description = "")


	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * First name user.
	 *
	 * @param firstName the first name
	 * @return the user
	 */
	public User firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}


	/**
	 * Get firstName
	 *
	 * @return firstName first name
	 */
	@Schema(example = "John", description = "")


	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * Last name user.
	 *
	 * @param lastName the last name
	 * @return the user
	 */
	public User lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}


	/**
	 * Get lastName
	 *
	 * @return lastName last name
	 */
	@Schema(example = "James", description = "")


	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * Email user.
	 *
	 * @param email the email
	 * @return the user
	 */
	public User email(String email) {
		this.email = email;
		return this;
	}


	/**
	 * Get email
	 *
	 * @return email email
	 */
	@Schema(example = "john@email.com", description = "")


	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * Password user.
	 *
	 * @param password the password
	 * @return the user
	 */
	public User password(String password) {
		this.password = password;
		return this;
	}


	/**
	 * Get password
	 *
	 * @return password password
	 */
	@Schema(example = "12345", description = "")


	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Phone user.
	 *
	 * @param phone the phone
	 * @return the user
	 */
	public User phone(String phone) {
		this.phone = phone;
		return this;
	}


	/**
	 * Get phone
	 *
	 * @return phone phone
	 */
	@Schema(example = "12345", description = "")


	public String getPhone() {
		return phone;
	}

	/**
	 * Sets phone.
	 *
	 * @param phone the phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}


	/**
	 * User status user.
	 *
	 * @param userStatus the user status
	 * @return the user
	 */
	public User userStatus(Integer userStatus) {
		this.userStatus = userStatus;
		return this;
	}


	/**
	 * User Status
	 *
	 * @return userStatus user status
	 */
	@Schema(example = "1", description = "User Status")


	public Integer getUserStatus() {
		return userStatus;
	}

	/**
	 * Sets user status.
	 *
	 * @param userStatus the user status
	 */
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}


	/**
	 * Equals boolean.
	 *
	 * @param o the o
	 * @return the boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(this.id, user.id) &&
				Objects.equals(this.username, user.username) &&
				Objects.equals(this.firstName, user.firstName) &&
				Objects.equals(this.lastName, user.lastName) &&
				Objects.equals(this.email, user.email) &&
				Objects.equals(this.password, user.password) &&
				Objects.equals(this.phone, user.phone) &&
				Objects.equals(this.userStatus, user.userStatus);
	}

	/**
	 * Hash code int.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, username, firstName, lastName, email, password, phone, userStatus);
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class User {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    username: ").append(toIndentedString(username)).append("\n");
		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    password: ").append(toIndentedString(password)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
		sb.append("    userStatus: ").append(toIndentedString(userStatus)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 *
	 * @param o the o
	 * @return the string
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

