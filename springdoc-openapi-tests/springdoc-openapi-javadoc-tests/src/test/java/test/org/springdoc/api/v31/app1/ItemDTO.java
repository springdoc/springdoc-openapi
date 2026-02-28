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

package test.org.springdoc.api.v31.app1;

import java.io.Serializable;

/**
 * The type Item dto.
 *
 * @author bnasslahsen
 */
class ItemDTO implements Serializable {

	/**
	 * serialVersionUID of type long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * itemID of type String
	 */
	private String itemID;

	/**
	 * description of type String
	 */
	private String description;

	/**
	 * price of type int
	 */
	private int price;

	/**
	 * The Deprecated price.
	 */
	@Deprecated
	private int deprecatedPrice;

	/**
	 * Instantiates a new Item dto.
	 */
	public ItemDTO() {
	}

	/**
	 * Instantiates a new Item dto.
	 *
	 * @param description description
	 * @param price       price
	 */
	public ItemDTO(final String description, final int price) {
		this.description = description;
		this.price = price;
	}

	/**
	 * Instantiates a new Item dto.
	 *
	 * @param itemID      itemID
	 * @param description description
	 * @param price       price
	 */
	public ItemDTO(final String itemID, final String description, final int price) {
		this.itemID = itemID;
		this.description = description;
		this.price = price;
	}

	/**
	 * Gets description.
	 *
	 * @return description description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Gets item id.
	 *
	 * @return item id
	 */
	public String getItemID() {
		return itemID;
	}

	/**
	 * Sets item id.
	 *
	 * @param itemID itemID
	 */
	public void setItemID(final String itemID) {
		this.itemID = itemID;
	}

	/**
	 * Gets price.
	 *
	 * @return price price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets price.
	 *
	 * @param price price
	 */
	public void setPrice(final int price) {
		this.price = price;
	}

	/**
	 * Gets deprecated price.
	 *
	 * @return the deprecated price
	 */
	public int getDeprecatedPrice() {
		return deprecatedPrice;
	}

	/**
	 * Sets deprecated price.
	 *
	 * @param deprecatedPrice the deprecated price
	 */
	public void setDeprecatedPrice(int deprecatedPrice) {
		this.deprecatedPrice = deprecatedPrice;
	}
}
