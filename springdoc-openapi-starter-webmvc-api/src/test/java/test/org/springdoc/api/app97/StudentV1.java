package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentV1 {

	@JsonProperty("name")
	private String name;

	public StudentV1(String name) {
		this.name = name;
	}
}
