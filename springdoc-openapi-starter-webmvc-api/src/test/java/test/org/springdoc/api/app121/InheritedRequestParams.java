package test.org.springdoc.api.app121;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

public class InheritedRequestParams extends RequestParams {

	@Parameter(description = "parameter from child of RequestParams")
	@NotBlank
	private String childParam;

	public String getChildParam() {
		return childParam;
	}

	public void setChildParam(String childParam) {
		this.childParam = childParam;
	}
}
