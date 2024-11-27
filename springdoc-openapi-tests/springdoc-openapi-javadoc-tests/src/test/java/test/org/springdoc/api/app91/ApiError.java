package test.org.springdoc.api.app91;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Api error.
 */
@Schema(
		type = "object",
		name = "ApiError",
		title = "ApiError",
		description = "A consistent response object for sending errors over the wire.")
class ApiError {

	/**
	 * The Status.
	 */
	@Schema(name = "status", description = "The Http Status value", type = "int", nullable = true)
	@JsonProperty("status")
	private int status;

	/**
	 * The Error code.
	 */
	@Schema(
			name = "errorCode",
			description = "An Error Code which can help with identifying issues.",
			type = "string",
			nullable = true)
	@JsonProperty("errorCode")
	private String errorCode;

	/**
	 * The Message.
	 */
	@Schema(name = "message", description = "The Error Message.", type = "string", nullable = false)
	@JsonProperty("message")
	private String message;

	/**
	 * Instantiates a new Api error.
	 *
	 * @param status the status 
	 * @param errorCode the error code 
	 * @param message the message
	 */
	public ApiError(int status, String errorCode, String message) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}
}
