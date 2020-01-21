package test.org.springdoc.api.app75;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/test1")
    @Operation(summary = "Example api that realize an ECHO operation",
            description = "The result of the echo is the input value of the api",
            parameters = {@Parameter(in = ParameterIn.PATH,
                    name = "uuid",
                    required = true,
                    description = "Is the identification of the document",
                    schema = @Schema(type = "string",
                            example = "uuid"))}


    )
    @ApiResponses(value = {
            @ApiResponse(description = "Successful Operation",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonDTO.class))),
            @ApiResponse(responseCode = "201",
                    description = "other possible response")
    })
    public String postMyRequestBody1() {
        return null;
    }

    @PostMapping("/test2")
    @Operation(summary = "Example api that realize an ECHO operation",
            description = "The result of the echo is the input value of the api",
            responses = {
                    @ApiResponse(description = "Successful Operation",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "201",
                            description = "other possible response")
            },
            parameters = {@Parameter(in = ParameterIn.PATH,
                    name = "uuid",
                    required = true,
                    description = "Is the identification of the document",
                    schema = @Schema(type = "string",
                            example = "uuid"))}


    )
    public String postMyRequestBody2() {
        return null;
    }

    @PostMapping("/test3")
    @Operation(summary = "Example api that realize an ECHO operation",
            description = "The result of the echo is the input value of the api",
            parameters = {@Parameter(in = ParameterIn.PATH,
                    name = "uuid",
                    required = true,
                    description = "Is the identification of the document",
                    schema = @Schema(type = "string",
                            example = "uuid"))}


    )
    @ApiResponse(responseCode = "201",
            description = "other possible response")
    public String postMyRequestBody3() {
        return null;
    }

}
