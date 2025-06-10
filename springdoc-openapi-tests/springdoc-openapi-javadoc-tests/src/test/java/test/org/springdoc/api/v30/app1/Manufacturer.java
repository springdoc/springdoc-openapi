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

package test.org.springdoc.api.v30.app1;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;


/**
 * Manufacturer
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
class Manufacturer {
	/**
	 * The Name.
	 */
	@JsonProperty("name")
	private String name = null;

	/**
	 * The Home page.
	 */
	@JsonProperty("homePage")
	private String homePage = null;

	/**
	 * The Phone.
	 */
	@JsonProperty("phone")
	private String phone = null;

	/**
	 * Name manufacturer.
	 *
	 * @param name the name
	 * @return the manufacturer
	 */
	public Manufacturer name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name name
	 */
	@Schema(example = "ACME Corporation", required = true)
	@NotNull

	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Home page manufacturer.
	 *
	 * @param homePage the home page
	 * @return the manufacturer
	 */
	public Manufacturer homePage(String homePage) {
		this.homePage = homePage;
		return this;
	}

	/**
	 * Get homePage
	 *
	 * @return homePage home page
	 */
	@Schema(example = "https://www.acme-corp.com")

	public String getHomePage() {
		return homePage;
	}

	/**
	 * Sets home page.
	 *
	 * @param homePage the home page
	 */
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	/**
	 * Phone manufacturer.
	 *
	 * @param phone the phone
	 * @return the manufacturer
	 */
	public Manufacturer phone(String phone) {
		this.phone = phone;
		return this;
	}

	/**
	 * Get phone
	 *
	 * @return phone phone
	 */
	@Schema(example = "408-867-5309")
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
		Manufacturer manufacturer = (Manufacturer) o;
		return Objects.equals(this.name, manufacturer.name) &&
				Objects.equals(this.homePage, manufacturer.homePage) &&
				Objects.equals(this.phone, manufacturer.phone);
	}

	/**
	 * Hash code int.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, homePage, phone);
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Manufacturer {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    homePage: ").append(toIndentedString(homePage)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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
