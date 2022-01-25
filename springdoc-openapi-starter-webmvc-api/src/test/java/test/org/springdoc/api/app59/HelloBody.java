package test.org.springdoc.api.app59;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class HelloBody {

    @NotNull
	@JsonProperty
    private String helloValue;


}
