package org.springdoc.api;

/**
 * The type Open api resource not found exception.
 * @author bnasslahsen
 */
public class OpenApiResourceNotFoundException extends RuntimeException {

	/**
	 * Instantiates a new Open api resource not found exception.
	 *
	 * @param message the message
	 */
	public OpenApiResourceNotFoundException(String message) {
		super(message);
	}
}
