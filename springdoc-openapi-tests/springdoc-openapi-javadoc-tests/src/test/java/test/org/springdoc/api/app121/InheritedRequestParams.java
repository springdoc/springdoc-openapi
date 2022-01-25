package test.org.springdoc.api.app121;

import jakarta.validation.constraints.NotBlank;

/**
 * The type Inherited request params.
 */
public class InheritedRequestParams extends RequestParams {

	/**
	 * parameter from child of RequestParams
	 */
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
