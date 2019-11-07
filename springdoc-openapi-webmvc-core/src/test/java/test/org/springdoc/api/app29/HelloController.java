package test.org.springdoc.api.app29;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @PostMapping(value = "/post-entity")
    @Operation(description = "Post entity",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrackerData.class))),
            responses =
                    {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrackerData.class))))})
    List<TrackerData> postEntity(@RequestBody TrackerData postEntity) {
        return null;
    }

}
