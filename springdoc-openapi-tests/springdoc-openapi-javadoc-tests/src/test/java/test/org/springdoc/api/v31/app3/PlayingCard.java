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

package test.org.springdoc.api.v31.app3;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type Playing card.
 */
class PlayingCard {
	/**
	 * The Suit.
	 */
	private Suit suit;

	/**
	 * The Value.
	 */
	private Integer value;

	/**
	 * The Toto.
	 */
	private Date toto;

	/**
	 * Gets toto.
	 *
	 * @return the toto
	 */
	public Date getToto() {
		return toto;
	}

	/**
	 * Sets toto.
	 *
	 * @param toto the toto
	 */
	public void setToto(Date toto) {
		this.toto = toto;
	}

	/**
	 * Gets suit.
	 *
	 * @return the suit
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Sets suit.
	 *
	 * @param suit the suit
	 */
	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * Sets value.
	 *
	 * @param value the value
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * The enum Suit.
	 */
	public enum Suit {
		/**
		 * Hearts suit.
		 */
		HEARTS("Hearts"),
		/**
		 * Diamonds suit.
		 */
		DIAMONDS("Diamonds"),
		/**
		 * Clubs suit.
		 */
		CLUBS("Clubs"),
		/**
		 * Spades suit.
		 */
		SPADES("Spades");

		/**
		 * The Json value.
		 */
		private final String jsonValue;

		/**
		 * Instantiates a new Suit.
		 *
		 * @param jsonValue the json value
		 */
		Suit(String jsonValue) {
			this.jsonValue = jsonValue;
		}

		/**
		 * Gets json value.
		 *
		 * @return the json value
		 */
		@JsonValue
		public String getJsonValue() {
			return jsonValue;
		}
	}
}