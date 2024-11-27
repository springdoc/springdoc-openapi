package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Student v 3.
 */
class StudentV3 {

	/**
	 * The Name.
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * Instantiates a new Student v 3.
	 *
	 * @param name the name
	 */
	public StudentV3(String name) {
		this.name = name;
	}
}
