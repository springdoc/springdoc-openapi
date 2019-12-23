package test.org.springdoc.api.app3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
@RequestMapping(path = "/demo",
        produces = MediaType.TEXT_PLAIN_VALUE)
public class HelloController {

    @GetMapping("operation4")
    @PageableAsQueryParam
    public String operation4() {
        return "operation4";
    }


    @Parameters({
            @Parameter(in = ParameterIn.QUERY
                    , description = "Page you want to retrieve (0..N)"
                    , name = "page"
                    , content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
            @Parameter(in = ParameterIn.QUERY
                    , description = "Number of records per page."
                    , name = "size"
                    , content = @Content(schema = @Schema(type = "integer", defaultValue = "20"))),
            @Parameter(in = ParameterIn.QUERY
                    , description = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. " + "Multiple sort criteria are supported."
                    , name = "sort"
                    , content = @Content(array = @ArraySchema(schema = @Schema(type = "string"))))
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface PageableAsQueryParam {}

}