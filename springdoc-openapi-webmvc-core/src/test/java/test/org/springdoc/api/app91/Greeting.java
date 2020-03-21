package test.org.springdoc.api.app91;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
		type = "object",
		name = "Greeting",
		title = "Greeting",
		description = "An object containing a greeting message")
public class Greeting {


	@Schema(
			name = "payload",
			description = "The greeting value",
			type = "string",
			nullable = false,
			example = "sdfsdfs")
	@JsonProperty("payload")
	private String payload;

	public Greeting(String payload) {
		this.payload = payload;
	}
}
