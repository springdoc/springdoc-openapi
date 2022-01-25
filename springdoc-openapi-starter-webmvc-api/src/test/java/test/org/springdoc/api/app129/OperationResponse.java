package test.org.springdoc.api.app129;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationResponse<T> {

	@JsonProperty
	String operationResult;
}
