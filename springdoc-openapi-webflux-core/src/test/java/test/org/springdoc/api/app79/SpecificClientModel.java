package test.org.springdoc.api.app79;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class SpecificClientModel extends BaseClientModel {
	@JsonProperty("name")
	@Schema(title = "my title", pattern = "this is it", example = "this is example")
	String name;
}