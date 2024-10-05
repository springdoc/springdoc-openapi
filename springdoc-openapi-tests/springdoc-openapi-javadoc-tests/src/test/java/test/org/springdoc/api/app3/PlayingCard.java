package test.org.springdoc.api.app3;

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
		 *Hearts suit.
		 */
		HEARTS("Hearts"),
		/**
		 *Diamonds suit.
		 */
		DIAMONDS("Diamonds"),
		/**
		 *Clubs suit.
		 */
		CLUBS("Clubs"),
		/**
		 *Spades suit.
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