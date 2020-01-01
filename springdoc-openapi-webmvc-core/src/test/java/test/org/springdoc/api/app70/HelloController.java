package test.org.springdoc.api.app70;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import test.org.springdoc.api.app70.customizer.CustomizedOperation;
import test.org.springdoc.api.app70.customizer.CustomizedParameter;
import test.org.springdoc.api.app70.model.ApiType;

@RestController
public class HelloController {

    @CustomizedOperation
    @Operation(description = "Some operation")
    @GetMapping("/example/{test}")
    public ApiType test(@PathVariable @CustomizedParameter @Parameter(description = "Parameter description") String test) {
        return new ApiType();
    }
}
