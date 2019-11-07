package test.org.springdoc.api.app25;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class HelloController {

    @GetMapping(value = "/check")
    @ResponseStatus(HttpStatus.OK)
    void check() {
    }

    @GetMapping(value = "/list")
    void list(

            @Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")) @PathVariable String trackerId,
            @Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate,
            @Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")) @RequestParam(value = "end", required = false) Instant endDate) {
    }

    @GetMapping(value = "/secondlist")
    void secondlist(
            @Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")) @PathVariable String trackerId,
            @Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate,
            @Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")) @RequestParam(value = "end", required = false) Instant endDate) {

    }

    @Operation(description = "Get last data from a tracker", parameters = {
            @Parameter(name = "trackerId", in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", example = "the-tracker-id")),
            @Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")),
            @Parameter(name = "end", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:10:00.000Z")),
            @Parameter(name = "limit", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "number", required = false, example = "10"))}, responses = {
            @ApiResponse(responseCode = "200")})

    @GetMapping(value = "/values/{trackerId}/data")
    void thirdList(@PathVariable String trackerId, @RequestParam(value = "start", required = false) Instant start,
                   @RequestParam(value = "end", required = false) Instant end,
                   @RequestParam(value = "limit", required = false) Integer limit) {

    }
}
