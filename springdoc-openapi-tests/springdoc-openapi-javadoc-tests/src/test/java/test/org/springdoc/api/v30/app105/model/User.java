/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v30.app105.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type User.
 */
public class User {

	/**
	 * The Id.
	 */
	@Schema(description = "")
	private Long id = null;

	/**
	 * The Username.
	 */
	@Schema(description = "")
	private String username = null;

	/**
	 * The First name.
	 */
	@Schema(description = "")
	private String firstName = null;

	/**
	 * The Last name.
	 */
	@Schema(description = "")
	private String lastName = null;

	/**
	 * The Email.
	 */
	@Schema(description = "")
	private String email = null;

	/**
	 * The Password.
	 */
	@Schema(description = "")
	private String password = null;

	/**
	 * The Phone.
	 */
	@Schema(description = "")
	private String phone = null;

	/**
	 * The User status.
	 */
	@Schema(description = "User Status")
	/**
	 * User Status
	 **/
	private Integer userStatus = null;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 *
	 * @param o the o
	 * @return the string
	 */
	private static String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	/**
	 * Get id
	 *
	 * @return id id
	 */
	@JsonProperty("id")
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
	 * Get username
	 *
	 * @return username username
	 */
	@JsonProperty("username")
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
	 * Get firstName
	 *
	 * @return firstName first name
	 */
	@JsonProperty("firstName")
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
	 * Get lastName
	 *
	 * @return lastName last name
	 */
	@JsonProperty("lastName")
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
	 * Get email
	 *
	 * @return email email
	 */
	@JsonProperty("email")
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
	 * Get password
	 *
	 * @return password password
	 */
	@JsonProperty("password")
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
	 * Get phone
	 *
	 * @return phone phone
	 */
	@JsonProperty("phone")
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
	 * User Status
	 *
	 * @return userStatus user status
	 */
	@JsonProperty("userStatus")
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
}
