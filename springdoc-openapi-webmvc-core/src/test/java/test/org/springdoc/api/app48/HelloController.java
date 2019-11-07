package test.org.springdoc.api.app48;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@ApiResponse(responseCode = "400")
@ApiResponses({
        @ApiResponse(responseCode = "401")
})
public class HelloController extends AbstractHelloController {

    @Override
    @GetMapping(path = "/documents/{locale}")
    @ApiResponse(responseCode = "402")
    @ApiResponses({
            @ApiResponse(responseCode = "403")
    })
    public ResponseEntity<String> getDocuments() {
        return null;
    }
}
