package test.org.springdoc.api.app101;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Hello dto.
 */
@Schema(description = "${test.app101.schema.hello.description}")
class HelloDTO {

	/**
	 * The Id.
	 */
	@Schema(description = "${test.app101.schema.hello.param.id.description}")
	private String id;

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

}
