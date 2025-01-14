package test.org.springdoc.api.v31.app237.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

public record Example(
        @JsonUnwrapped
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Wrapped unwrapped,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Some description")
        Integer number
) {
    public record Wrapped(
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Some description of value")
            String value
    ) {
    }
}
