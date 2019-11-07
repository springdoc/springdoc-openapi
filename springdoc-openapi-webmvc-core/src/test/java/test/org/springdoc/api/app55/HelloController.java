package test.org.springdoc.api.app55;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Tag(name = "health")
public class HelloController {

    /**
     * Ping endpoint used for health checks.
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @Operation(summary = "Simple health check")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OK")})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Boolean ping() {
        return true;
    }
}