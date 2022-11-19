package test.org.springdoc.api.app153;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * The enum Order state.
 */
@Schema(type = "string", allowableValues = { "finished", "new" })
public enum OrderState {
	/**
	 *Finished order state.
	 */
	FINISHED("finished"),
	/**
	 *New order state.
	 */
	NEW("new");

	/**
	 * The Value.
	 */
	private final String value;

	/**
	 * Instantiates a new Order state.
	 *
	 * @param value the value
	 */
	OrderState(String value) {
		this.value = value;
	}

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}