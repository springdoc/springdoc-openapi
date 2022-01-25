package test.org.springdoc.api.app93;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Specific client model.
 */
public class SpecificClientModel extends BaseClientModel {
	/**
	 * The Name.
	 */
	@JsonProperty("name")
	String name;
}