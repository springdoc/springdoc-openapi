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
public class Manufacturer {
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("homePage")
	private String homePage = null;

	@JsonProperty("phone")
	private String phone = null;

	public Manufacturer name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name
	 **/
	@Schema(example = "ACME Corporation", required = true)
	@NotNull

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Manufacturer homePage(String homePage) {
		this.homePage = homePage;
		return this;
	}

	/**
	 * Get homePage
	 *
	 * @return homePage
	 **/
	@Schema(example = "https://www.acme-corp.com")

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public Manufacturer phone(String phone) {
		this.phone = phone;
		return this;
	}

	/**
	 * Get phone
	 *
	 * @return phone
	 **/
	@Schema(example = "408-867-5309")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Override
	public boolean equals(java.lang.Object o) {
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

	@Override
	public int hashCode() {
		return Objects.hash(name, homePage, phone);
	}

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
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
