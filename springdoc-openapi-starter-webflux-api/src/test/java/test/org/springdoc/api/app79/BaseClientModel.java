package test.org.springdoc.api.app79;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseClientModel {
	@JsonProperty("id")
	int id;
}
