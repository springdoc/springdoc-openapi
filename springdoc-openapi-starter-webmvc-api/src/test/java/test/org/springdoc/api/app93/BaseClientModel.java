package test.org.springdoc.api.app93;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseClientModel {
	@JsonProperty("id")
	int id;
}
