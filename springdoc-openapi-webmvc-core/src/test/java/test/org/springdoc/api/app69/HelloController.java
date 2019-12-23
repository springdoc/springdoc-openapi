package test.org.springdoc.api.app69;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import test.org.springdoc.api.app69.customizer.CustomizedOperation;
import test.org.springdoc.api.app69.customizer.CustomizedParameter;
import test.org.springdoc.api.app69.model.ApiType;

@RestController
public class HelloController {

		@CustomizedOperation
		@Operation(description = "Some operation")
    @GetMapping("/example/{test}")
    public ApiType test(@PathVariable @CustomizedParameter @Parameter(description = "Parameter description") String test) {
    	return new ApiType();
    }
}
