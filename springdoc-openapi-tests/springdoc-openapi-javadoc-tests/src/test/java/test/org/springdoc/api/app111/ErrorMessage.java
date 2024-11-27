package test.org.springdoc.api.app111;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * The type Error message.
 */
class ErrorMessage {

	/**
	 * The Errors.
	 */
	private List<String> errors;

	/**
	 * Instantiates a new Error message.
	 */
	public ErrorMessage() {
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param errors the errors
	 */
	public ErrorMessage(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param error the error
	 */
	public ErrorMessage(String error) {
		this(Collections.singletonList(error));
	}

	/**
	 * Instantiates a new Error message.
	 *
	 * @param errors the errors
	 */
	public ErrorMessage(String... errors) {
		this(Arrays.asList(errors));
	}

	/**
	 * Gets errors.
	 *
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Sets errors.
	 *
	 * @param errors the errors
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}