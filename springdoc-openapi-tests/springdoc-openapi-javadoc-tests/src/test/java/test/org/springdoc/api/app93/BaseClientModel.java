package test.org.springdoc.api.app93;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Base client model.
 */
public abstract class BaseClientModel {
	/**
	 * The Id.
	 */
	@JsonProperty("id")
	int id;
}
