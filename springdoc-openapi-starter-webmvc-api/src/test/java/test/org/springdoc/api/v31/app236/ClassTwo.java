package test.org.springdoc.api.v31.app236;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassTwo {

    @NotNull
    private String name;

	public @NotNull String getName() {
		return name;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}
}