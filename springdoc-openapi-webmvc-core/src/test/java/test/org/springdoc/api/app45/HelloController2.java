package test.org.springdoc.api.app45;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "People", description = "Use this resource to serve all requests and initiate all operations related to people")
@SecurityRequirement(name = "bearer")
@RestController
@RequestMapping(value = "/v1/people2")
public class HelloController2 {


	@Operation(description = "List all persons")
	@ApiResponse(responseCode = "200", description = "", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))))
	@GetMapping(path = "/list", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonDTO> list() {
		PersonDTO person = new PersonDTO();
		person.setFirstName("Nass");
		return Collections.singletonList(person);
	}

	@Operation(description = "List all persons")
	@ApiResponse(responseCode = "200", description = "", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))))
	@GetMapping(path = "/listTwo", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonDTO> listTwo() {
		PersonDTO person = new PersonDTO();
		person.setFirstName("Nass");
		return Collections.singletonList(person);
	}

}