package test.org.springdoc.api.app97;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Student v 2.
 */
class StudentV2 {

	/**
	 * The Name.
	 */
	@JsonProperty("bb")
	private String name;

	/**
	 * Instantiates a new Student v 2.
	 *
	 * @param name the name
	 */
	public StudentV2(String name) {
		this.name = name;
	}
}
