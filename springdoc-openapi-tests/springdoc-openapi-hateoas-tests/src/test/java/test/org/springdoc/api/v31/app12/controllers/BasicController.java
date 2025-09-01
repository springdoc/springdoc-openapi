package test.org.springdoc.api.v31.app12.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import test.org.springdoc.api.v31.app12.model.Cat;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class BasicController {

    @GetMapping("/cat")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get", description = "Provides an animal.")
    public String get(Cat cat) {
        return cat != null ? cat.getName() : "";
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get", description = "Provides a response.")
    @ApiResponse(content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE,
        schema = @io.swagger.v3.oas.annotations.media.Schema(oneOf = {
                Integer.class
        })),
        responseCode = "200")
    public Object get() {
        return 1;
    }

    // dummy
    public static class Response extends RepresentationModel {
        public Response(String v) {}
    }
}
