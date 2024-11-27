package test.org.springdoc.api.app91;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Advice.
 */
@RestControllerAdvice
class Advice {

	/**
	 * Bad request response entity.
	 *
	 * @param req the req 
	 * @param exception the exception 
	 * @return the response entity
	 */
	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(
			responseCode = "400",
			description = "Bad Request",
			content =
			@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ApiError.class),
					examples = {
							@ExampleObject(
									name = "Service-400",
									summary = "400 from the service directly",
									value =
											"{\"status\": 400,"
													+ "\"errorCode\": \"ERROR_001\","
													+ "\"message\": \"An example message...\""
													+ "}")
					}))
	public ResponseEntity<ApiError> badRequest(HttpServletRequest req, Exception exception) {
		ApiError erroObj = new ApiError(400, "A code", "A message");
		return new ResponseEntity<>(erroObj, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Internal server error response entity.
	 *
	 * @param req the req 
	 * @param exception the exception 
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ApiResponse(
			responseCode = "500",
			description = "Internal Server Error",
			content =
			@Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = ApiError.class),
					examples = {
							@ExampleObject(
									name = "Service-500",
									summary = "500 from the service directly",
									value =
											"{\"status\": 500,"
													+ "\"errorCode\": \"ERROR_002\","
													+ "\"message\": \"Another example message...\""
													+ "}")
					}))
	public ResponseEntity<ApiError> internalServerError(HttpServletRequest req, Exception exception) {
		ApiError erroObj = new ApiError(500, "A  different code", "A  different message");
		return new ResponseEntity<>(erroObj, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
