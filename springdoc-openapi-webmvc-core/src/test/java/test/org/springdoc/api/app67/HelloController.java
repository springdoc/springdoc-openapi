package test.org.springdoc.api.app67;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@RestController
@RequestMapping(path = "/demo",
        produces = MediaType.TEXT_PLAIN_VALUE)
public class HelloController {

    @GetMapping("operation1")
    @Operation(summary = "Operation 1 (expected result - no parameters)")
    public String operation1() {
        return "operation1";
    }

    @GetMapping("operation2")
    @Operation(summary = "Operation 2 (expected result - 3 parameters)", parameters = {
            @Parameter(name = "pageNumber", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    public String operation2() {
        return "operation2";
    }

    @GetMapping("operation3")
    @Operation(summary = "Operation 3 (expected result - 3 parameters)",parameters = {
            @Parameter(name = "pageNumber", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    public String operation3() {
        return "operation3";
    }

    @GetMapping("operation4")
    @Operation(summary = "Operation 4 (expected result - 3 parameters)")
    @QueryPaging
    @QuerySort
    public String operation4() {
        return "operation4";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ METHOD })
    @Parameters({
            @Parameter(name = "pageNumber", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    })
    public @interface QueryPaging {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ METHOD })
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    public @interface QuerySort {

    }
}