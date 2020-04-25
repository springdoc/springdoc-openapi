package test.org.springdoc.api.app102;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Parameter;

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
