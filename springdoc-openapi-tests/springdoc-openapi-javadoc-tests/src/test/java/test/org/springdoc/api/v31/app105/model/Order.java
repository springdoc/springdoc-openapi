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

package test.org.springdoc.api.v31.app105.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Order.
 */
public class Order {

	/**
	 * The Id.
	 */
	@Schema(description = "")
	private Long id = null;

	/**
	 * The Pet id.
	 */
	@Schema(description = "")
	private Long petId = null;

	/**
	 * The Quantity.
	 */
	@Schema(description = "")
	private Integer quantity = null;

	/**
	 * The Ship date.
	 */
	@Schema(description = "")
	private Date shipDate = null;

	/**
	 * The Status.
	 */
	@Schema(description = "Order Status")
	/**
	 * Order Status
	 **/
	private StatusEnum status = null;

	/**
	 * The Complete.
	 */
	@Schema(description = "")
	private Boolean complete = false;

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
	 * Id order.
	 *
	 * @param id the id
	 * @return the order
	 */
	public Order id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get petId
	 *
	 * @return petId pet id
	 */
	@JsonProperty("petId")
	public Long getPetId() {
		return petId;
	}

	/**
	 * Sets pet id.
	 *
	 * @param petId the pet id
	 */
	public void setPetId(Long petId) {
		this.petId = petId;
	}

	/**
	 * Pet id order.
	 *
	 * @param petId the pet id
	 * @return the order
	 */
	public Order petId(Long petId) {
		this.petId = petId;
		return this;
	}

	/**
	 * Get quantity
	 *
	 * @return quantity quantity
	 */
	@JsonProperty("quantity")
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * Sets quantity.
	 *
	 * @param quantity the quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * Quantity order.
	 *
	 * @param quantity the quantity
	 * @return the order
	 */
	public Order quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	/**
	 * Get shipDate
	 *
	 * @return shipDate ship date
	 */
	@JsonProperty("shipDate")
	public Date getShipDate() {
		return shipDate;
	}

	/**
	 * Sets ship date.
	 *
	 * @param shipDate the ship date
	 */
	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	/**
	 * Ship date order.
	 *
	 * @param shipDate the ship date
	 * @return the order
	 */
	public Order shipDate(Date shipDate) {
		this.shipDate = shipDate;
		return this;
	}

	/**
	 * Order Status
	 *
	 * @return status status
	 */
	@JsonProperty("status")
	public String getStatus() {
		if (status == null) {
			return null;
		}
		return status.getValue();
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	/**
	 * Status order.
	 *
	 * @param status the status
	 * @return the order
	 */
	public Order status(StatusEnum status) {
		this.status = status;
		return this;
	}

	/**
	 * Get complete
	 *
	 * @return complete boolean
	 */
	@JsonProperty("complete")
	public Boolean isisComplete() {
		return complete;
	}

	/**
	 * Sets complete.
	 *
	 * @param complete the complete
	 */
	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	/**
	 * Complete order.
	 *
	 * @param complete the complete
	 * @return the order
	 */
	public Order complete(Boolean complete) {
		this.complete = complete;
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

	/**
	 * The enum Status enum.
	 */
	public enum StatusEnum {
		/**
		 * Placed status enum.
		 */
		PLACED("placed"),
		/**
		 * Approved status enum.
		 */
		APPROVED("approved"),
		/**
		 * Delivered status enum.
		 */
		DELIVERED("delivered");

		/**
		 * The Value.
		 */
		private String value;

		/**
		 * Instantiates a new Status enum.
		 *
		 * @param value the value
		 */
		StatusEnum(String value) {
			this.value = value;
		}

		/**
		 * From value status enum.
		 *
		 * @param text the text
		 * @return the status enum
		 */
		@JsonCreator
		public static StatusEnum fromValue(String text) {
			for (StatusEnum b : StatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		@JsonValue
		public String getValue() {
			return value;
		}

		/**
		 * To string string.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
}
