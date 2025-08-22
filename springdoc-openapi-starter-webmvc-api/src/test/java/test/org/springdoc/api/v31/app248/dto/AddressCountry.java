package test.org.springdoc.api.v31.app248.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class AddressCountry {
	private final String value;

	@JsonCreator
	public AddressCountry(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}