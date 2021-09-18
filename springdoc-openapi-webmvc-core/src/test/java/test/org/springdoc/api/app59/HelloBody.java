package test.org.springdoc.api.app59;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HelloBody {

    @NotNull
	@JsonProperty
    private String helloValue;


}
