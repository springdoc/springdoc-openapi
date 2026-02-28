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

package test.org.springdoc.api.v30.app1;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

/**
 * InventoryItem
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
class InventoryItem {
	/**
	 * The Id.
	 */
	@JsonProperty("id")
	private UUID id = null;

	/**
	 * The Name.
	 */
	@JsonProperty("name")
	private String name = null;

	/**
	 * The Release date.
	 */
	@JsonProperty("releaseDate")
	private String releaseDate = null;

	/**
	 * The Manufacturer.
	 */
	@JsonProperty("manufacturer")
	private Manufacturer manufacturer = null;

	/**
	 * Id inventory item.
	 *
	 * @param id the id
	 * @return the inventory item
	 */
	public InventoryItem id(UUID id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 *
	 * @return id id
	 */
	@Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true)
	@NotNull

	@Valid
	public UUID getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Name inventory item.
	 *
	 * @param name the name
	 * @return the inventory item
	 */
	public InventoryItem name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name name
	 */
	@Schema(example = "Widget Adapter", required = true)
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
	 * Release date inventory item.
	 *
	 * @param releaseDate the release date
	 * @return the inventory item
	 */
	public InventoryItem releaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
		return this;
	}

	/**
	 * Get releaseDate
	 *
	 * @return releaseDate release date
	 */
	@Schema(example = "2016-08-29T09:12:33.001Z", required = true)
	@NotNull

	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets release date.
	 *
	 * @param releaseDate the release date
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Manufacturer inventory item.
	 *
	 * @param manufacturer the manufacturer
	 * @return the inventory item
	 */
	public InventoryItem manufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
		return this;
	}

	/**
	 * Get manufacturer
	 *
	 * @return manufacturer manufacturer
	 */
	@Schema(required = true)
	@NotNull

	@Valid
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * Sets manufacturer.
	 *
	 * @param manufacturer the manufacturer
	 */
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
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
		InventoryItem inventoryItem = (InventoryItem) o;
		return Objects.equals(this.id, inventoryItem.id) &&
				Objects.equals(this.name, inventoryItem.name) &&
				Objects.equals(this.releaseDate, inventoryItem.releaseDate) &&
				Objects.equals(this.manufacturer, inventoryItem.manufacturer);
	}

	/**
	 * Hash code int.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, name, releaseDate, manufacturer);
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class InventoryItem {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
		sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
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
