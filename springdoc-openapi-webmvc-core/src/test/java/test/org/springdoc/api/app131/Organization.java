package test.org.springdoc.api.app131;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description =
				"This is the description being overwritten")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name"})
public class Organization {

	@Schema(
			description =
					"The Universally Unique Identifier (UUID) uniquely identifying the organization",
			required = true)
	@JsonProperty(required = true)
	private UUID id;

	@Schema(description = "The name of the organization", required = true)
	@JsonProperty(required = true)
	private String name;

	public Organization() {}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}