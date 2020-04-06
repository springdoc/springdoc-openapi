package test.org.springdoc.api.app102;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.lang.Nullable;

public class RequestParams {

	@Parameter(description = "string parameter")
	private String stringParam;

	private String stringParam1;

	@Parameter(description = "string parameter2", required = true)
	private String stringParam2;

	@Parameter(description = "int parameter")
	private int intParam;

	private Optional<String> intParam2;

	@Nullable
	private String intParam3;

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

	public Optional<String> getIntParam2() {
		return intParam2;
	}

	public void setIntParam2(Optional<String> intParam2) {
		this.intParam2 = intParam2;
	}

	@Nullable
	public String getIntParam3() {
		return intParam3;
	}

	public void setIntParam3(@Nullable String intParam3) {
		this.intParam3 = intParam3;
	}

	public String getStringParam1() {
		return stringParam1;
	}

	public void setStringParam1(String stringParam1) {
		this.stringParam1 = stringParam1;
	}

	public String getStringParam2() {
		return stringParam2;
	}

	public void setStringParam2(String stringParam2) {
		this.stringParam2 = stringParam2;
	}
}
