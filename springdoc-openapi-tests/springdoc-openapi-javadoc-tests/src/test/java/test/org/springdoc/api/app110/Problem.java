package test.org.springdoc.api.app110;

/**
 * The type Problem.
 */
class Problem {

	/**
	 * The Log ref.
	 */
	private String logRef;

	/**
	 * The Message.
	 */
	private String message;

	/**
	 * Instantiates a new Problem.
	 *
	 * @param logRef the log ref 
	 * @param message the message
	 */
	public Problem(String logRef, String message) {
		super();
		this.logRef = logRef;
		this.message = message;
	}

	/**
	 * Instantiates a new Problem.
	 */
	public Problem() {
		super();

	}

	/**
	 * Gets log ref.
	 *
	 * @return the log ref
	 */
	public String getLogRef() {
		return logRef;
	}

	/**
	 * Sets log ref.
	 *
	 * @param logRef the log ref
	 */
	public void setLogRef(String logRef) {
		this.logRef = logRef;
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
