/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v30.app2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

public class Order {

	@Schema(description = "")
	private Long id = null;

	@Schema(description = "")
	private Long petId = null;

	@Schema(description = "")
	private Integer quantity = null;

	@Schema(description = "")
	private Date shipDate = null;

	@Schema(description = "Order Status")
	/**
	 * Order Status
	 **/
	private StatusEnum status = null;

	@Schema(description = "")
	private Boolean complete = false;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private static String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	/**
	 * Get id
	 *
	 * @return id
	 **/
	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get petId
	 *
	 * @return petId
	 **/
	@JsonProperty("petId")
	public Long getPetId() {
		return petId;
	}

	public void setPetId(Long petId) {
		this.petId = petId;
	}

	public Order petId(Long petId) {
		this.petId = petId;
		return this;
	}

	/**
	 * Get quantity
	 *
	 * @return quantity
	 **/
	@JsonProperty("quantity")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Order quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	/**
	 * Get shipDate
	 *
	 * @return shipDate
	 **/
	@JsonProperty("shipDate")
	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public Order shipDate(Date shipDate) {
		this.shipDate = shipDate;
		return this;
	}

	/**
	 * Order Status
	 *
	 * @return status
	 **/
	@JsonProperty("status")
	public String getStatus() {
		if (status == null) {
			return null;
		}
		return status.getValue();
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Order status(StatusEnum status) {
		this.status = status;
		return this;
	}

	/**
	 * Get complete
	 *
	 * @return complete
	 **/
	@JsonProperty("complete")
	public Boolean isisComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Order complete(Boolean complete) {
		this.complete = complete;
		return this;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Order {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    petId: ").append(toIndentedString(petId)).append("\n");
		sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
		sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	public enum StatusEnum {
		PLACED("placed"),
		APPROVED("approved"),
		DELIVERED("delivered");

		private final String value;

		StatusEnum(String value) {
			this.value = value;
		}

		@JsonCreator
		public static StatusEnum fromValue(String text) {
			for (StatusEnum b : StatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
}
