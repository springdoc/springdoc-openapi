package test.org.springdoc.api.v31.app231;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationsRestController {

    @Operation(summary = "Parameter object")
    @PostMapping("/parameter-object")
    public Application createWithParameterObject(
            @RequestBody @ParameterObject SubClass request
    ) {
        return new Application();
    }

}
