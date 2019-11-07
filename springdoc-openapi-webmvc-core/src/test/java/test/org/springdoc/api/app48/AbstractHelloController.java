package test.org.springdoc.api.app48;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@ApiResponse(responseCode = "410")
@ApiResponses({
        @ApiResponse(responseCode = "411")
})
public class AbstractHelloController {

    @GetMapping(path = "/documents/{locale}")
    @ApiResponse(responseCode = "412")
    @ApiResponses({
            @ApiResponse(responseCode = "413")
    })
    public ResponseEntity<String> getDocuments() {
        return null;
    }

}
