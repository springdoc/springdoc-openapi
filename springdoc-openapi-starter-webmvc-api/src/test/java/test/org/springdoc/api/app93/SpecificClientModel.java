package test.org.springdoc.api.app93;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecificClientModel extends BaseClientModel {
	@JsonProperty("name")
	String name;
}