package test.org.springdoc.api.app9.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ResponseData(
        @JsonProperty(value = "DATA_ID", required = true)
        @NotNull
        UUID dataId,
        @JsonProperty(value = "DATE", required = true)
        @NotNull
        @Schema(example = "2024-03-27", format = "date")
        LocalDate date
) {}
