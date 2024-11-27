package test.org.springdoc.api.app158;

/**
 * The type Error dto.
 */
class ErrorDTO {
	/**
	 * The Message.
	 */
	private String message;

	/**
	 * Instantiates a new Error dto.
	 */
	public ErrorDTO() {
	}

	/**
	 * Instantiates a new Error dto.
	 *
	 * @param message the message
	 */
	public ErrorDTO(String message) {
		this.message = message;
	}

	/**
	 * Gets message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message.
	 *
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
