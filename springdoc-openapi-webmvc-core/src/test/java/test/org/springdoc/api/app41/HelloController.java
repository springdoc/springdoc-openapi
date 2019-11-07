package test.org.springdoc.api.app41;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController("/api")
public class HelloController {

    @Operation(description = "Download file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File resource", content = @Content(schema = @Schema(implementation = java.io.File.class))),
            @ApiResponse(responseCode = "400", description = "Wrong request", content = @Content(schema = @Schema(implementation = java.lang.Error.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = java.lang.Error.class)))})
    @GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<java.io.File> getFile(
            @NotNull @Parameter(description = "File path", required = true) @Valid @RequestParam(value = "path") String path) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}