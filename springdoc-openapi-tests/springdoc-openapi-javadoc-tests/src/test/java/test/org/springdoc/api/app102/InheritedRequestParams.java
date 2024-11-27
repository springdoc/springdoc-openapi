package test.org.springdoc.api.app102;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

/**
 * The type Inherited request params.
 */
class InheritedRequestParams extends RequestParams {
	/**
	 * The Child param.
	 */
	@Parameter(description = "parameter from child of RequestParams")
	@NotBlank
	private String childParam;

	/**
	 * Gets child param.
	 *
	 * @return the child param
	 */
	public String getChildParam() {
		return childParam;
	}

	/**
	 * Sets child param.
	 *
	 * @param childParam the child param
	 */
	public void setChildParam(String childParam) {
		this.childParam = childParam;
	}
}
