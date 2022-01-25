package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentV2 {

	@JsonProperty("name")
	private String name;

	public StudentV2(String name) {
		this.name = name;
	}
}
