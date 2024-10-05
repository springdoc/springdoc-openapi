package test.org.springdoc.api.app91;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Greeting.
 */
@Schema(
		type = "object",
		name = "Greeting",
		title = "Greeting",
		description = "An object containing a greeting message")
class Greeting {


	/**
	 * The Payload.
	 */
	@Schema(
			name = "payload",
			description = "The greeting value",
			type = "string",
			nullable = false,
			example = "sdfsdfs")
	@JsonProperty("payload")
	private String payload;

	/**
	 * Instantiates a new Greeting.
	 *
	 * @param payload the payload
	 */
	public Greeting(String payload) {
		this.payload = payload;
	}
}
