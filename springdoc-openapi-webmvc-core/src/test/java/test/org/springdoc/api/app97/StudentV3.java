package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentV3 {

	@JsonProperty("name")
	private String name;

	public StudentV3(String name) {
		this.name = name;
	}
}
