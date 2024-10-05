package test.org.springdoc.api.app129;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Operation response.
 *
 * @param <T>  the type parameter
 */
class OperationResponse<T> {

	/**
	 * The Operation result.
	 */
	@JsonProperty
	String operationResult;
}
