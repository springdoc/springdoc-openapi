package test.org.springdoc.api.app1;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TrackerData")
public class TrackerData {

	@Schema(name = "trackerId", type = "string", required = true, example = "the-tracker-id")
	@JsonProperty("trackerId")
	String trackerId;

	@Schema(name = "timestamp", type = "string", format = "date-time", required = true, example = "2018-01-01T00:00:00Z")
    @JsonProperty("timestamp")
	Instant timestamp;
	@Schema(name = "value", type = "number", format = "double", description = "The data value", required = true, example = "19.0")
	@JsonProperty("value")
	Double value;

}
