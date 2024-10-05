package test.org.springdoc.api.app159;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The type Foo bean.
 */
class FooBean {
	/**
	 * The Message.
	 */
	@JsonView(Views.View2.class)
	private String message;

	/**
	 * The Code.
	 */
	@JsonView(Views.View1.class)
	private int code;

	/**
	 * Instantiates a new Foo bean.
	 *
	 * @param message the message
	 * @param code the code
	 */
	public FooBean(String message, int code) {
		this.message = message;
		this.code = code;
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
	 * Gets code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets code.
	 *
	 * @param code the code
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
