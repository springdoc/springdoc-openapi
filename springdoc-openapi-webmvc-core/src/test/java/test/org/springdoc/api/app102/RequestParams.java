package test.org.springdoc.api.app102;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.lang.Nullable;

public class RequestParams {
	@Nullable
	@Parameter(description = "string parameter")
	private String stringParam;

	@Parameter(description = "int parameter")
	private int intParam;

	public String getStringParam() {
		return stringParam;
	}

	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	public int getIntParam() {
		return intParam;
	}

	public void setIntParam(int intParam) {
		this.intParam = intParam;
	}
}
