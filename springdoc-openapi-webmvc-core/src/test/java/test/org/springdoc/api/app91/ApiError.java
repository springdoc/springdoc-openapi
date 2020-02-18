package test.org.springdoc.api.app91;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    type = "object",
    name = "ApiError",
    title = "ApiError",
    description = "A consistent response object for sending errors over the wire.")
public class ApiError {

  @Schema(name = "status", description = "The Http Status value", type = "int", nullable = true)
  @JsonProperty("status")
  private int status;

  @Schema(
      name = "errorCode",
      description = "An Error Code which can help with identifying issues.",
      type = "string",
      nullable = true)
  @JsonProperty("errorCode")
  private String errorCode;

  @Schema(name = "message", description = "The Error Message.", type = "string", nullable = false)
  @JsonProperty("message")
  private String message;

	public ApiError(int status, String errorCode, String message) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}
}
