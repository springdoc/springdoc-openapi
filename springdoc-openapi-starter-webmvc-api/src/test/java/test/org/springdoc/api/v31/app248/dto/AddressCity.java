package test.org.springdoc.api.v31.app248.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class AddressCity {
	private final String value;

	@JsonCreator
	public AddressCity(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}