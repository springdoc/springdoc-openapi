package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Student v 1.
 */
class StudentV1 {

	/**
	 * The Name.
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * Instantiates a new Student v 1.
	 *
	 * @param name the name
	 */
	public StudentV1(String name) {
		this.name = name;
	}
}
