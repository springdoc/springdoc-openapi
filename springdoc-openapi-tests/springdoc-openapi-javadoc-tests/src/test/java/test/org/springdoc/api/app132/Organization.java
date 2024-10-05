package test.org.springdoc.api.app132;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Organization.
 */
@Schema(description =
		"This is the description being overwritten")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name" })
class Organization {

	/**
	 * The Id.
	 */
	@Schema(
			description =
					"The Universally Unique Identifier (UUID) uniquely identifying the organization",
			required = true)
	@JsonProperty(required = true)
	private UUID id;

	/**
	 * The Name.
	 */
	@Schema(description = "The name of the organization", required = true)
	@JsonProperty(required = true)
	private String name;

	/**
	 * Instantiates a new Organization.
	 */
	public Organization() {
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}
}