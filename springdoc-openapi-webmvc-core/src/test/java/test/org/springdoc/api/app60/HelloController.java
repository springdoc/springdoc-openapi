package test.org.springdoc.api.app60;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/hello1")
    @Operation(summary = "summary1")
    @Parameters({
            @Parameter(name = "page", description = "The page"),
            @Parameter(name = "size", description = "The size")
    })
    public List<?> list1(String page, String size) {
        return null;
    }

    @GetMapping("/hello2")
    @Operation(summary = "summary2")
    @QuerySort
    @QueryPaging
    public List<?> list2(String page, String size, String sort) {
        return null;
    }
}
