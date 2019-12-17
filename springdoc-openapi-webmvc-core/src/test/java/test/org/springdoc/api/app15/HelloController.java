package test.org.springdoc.api.app15;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/persons")
    @Operation(description = "${springdoc.operation-descriptions.myOperation}", responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))))
    public JSONObject persons() {
        return new JSONObject();
    }

}
