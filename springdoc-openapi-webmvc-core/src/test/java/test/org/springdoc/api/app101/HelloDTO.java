package test.org.springdoc.api.app101;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "${test.app101.schema.hello.description}")
public class HelloDTO {

	@Schema(description = "${test.app101.schema.hello.param.id.description}")
	private String id;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
