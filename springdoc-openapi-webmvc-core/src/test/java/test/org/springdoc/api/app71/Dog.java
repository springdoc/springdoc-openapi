package test.org.springdoc.api.app71;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "Dog")
public class Dog {

    @JsonProperty("display_name")
    @Schema(
            name = "display_name",
            description = "A name given to the Dog",
            example = "Fido"
    )
    String displayName;



}