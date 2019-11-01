package test.org.springdoc.api.app54;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class HelloController {

	@GetMapping(value = "/parties/{id}")
	@JsonView(Views.Public.class)
	@Operation(summary = "Gets meal party details [Meal party admin restricted]")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved the meal party") })
	public MealParty getMealParty(@PathVariable("id") long mealPartyId) {
		return null;
	}

	@JsonView(Views.MealPartyAdmin.class)
	@PostMapping(value = "/parties")
	public ResponseEntity<MealParty> saveMealParty(@JsonView(Views.Public.class) @RequestBody MealParty p) {
		return null;
	}

}