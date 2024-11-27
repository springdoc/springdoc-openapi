package test.org.springdoc.api.app158;

/**
 * The type Simple dto.
 */
class SimpleDTO {

	/**
	 * The Payload.
	 */
	private String payload;

	/**
	 * Instantiates a new Simple dto.
	 */
	public SimpleDTO() {
	}

	/**
	 * Instantiates a new Simple dto.
	 *
	 * @param payload the payload
	 */
	public SimpleDTO(String payload) {
		this.payload = payload;
	}

	/**
	 * Gets payload.
	 *
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * Sets payload.
	 *
	 * @param payload the payload
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
