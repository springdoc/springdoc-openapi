package test.org.springdoc.api.app108;

/**
 * The type Action result.
 *
 * @param <T>  the type parameter
 */
class ActionResult<T> {

	/**
	 * The Value.
	 */
	protected T value;

	/**
	 * The Success.
	 */
	protected boolean success;

	/**
	 * The Error code.
	 */
	protected String errorCode;

	/**
	 * The Message.
	 */
	protected String message;

	/**
	 * The Error value.
	 */
	protected Object errorValue;

	/**
	 * The Target url.
	 */
	protected String targetUrl;

	/**
	 * Gets value.
	 *
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets value.
	 *
	 * @param value the value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Is success boolean.
	 *
	 * @return the boolean
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets success.
	 *
	 * @param success the success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Gets error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets error code.
	 *
	 * @param errorCode the error code
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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

	/**
	 * Gets error value.
	 *
	 * @return the error value
	 */
	public Object getErrorValue() {
		return errorValue;
	}

	/**
	 * Sets error value.
	 *
	 * @param errorValue the error value
	 */
	public void setErrorValue(Object errorValue) {
		this.errorValue = errorValue;
	}

	/**
	 * Gets target url.
	 *
	 * @return the target url
	 */
	public String getTargetUrl() {
		return targetUrl;
	}

	/**
	 * Sets target url.
	 *
	 * @param targetUrl the target url
	 */
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
