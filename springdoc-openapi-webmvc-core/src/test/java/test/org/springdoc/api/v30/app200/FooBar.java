package test.org.springdoc.api.v30.app200;


import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public enum FooBar {
	FOO("foo"),
	BAR("bar");

	private String value;

	FooBar(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static FooBar fromValue(String value) {
		for (FooBar b : FooBar.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}