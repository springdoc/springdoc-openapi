package test.org.springdoc.api.app1;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "items")
public class ItemController {

	@GetMapping("/items")
	public List<ItemDTO> showItems(@RequestParam("cusID") @Size(min = 4, max = 6) final String customerID,
			@Size(min = 4, max = 6) int toto,
			@Parameter(name = "start", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "string", format = "date-time", required = false, example = "1970-01-01T00:00:00.000Z")) @RequestParam(value = "start", required = false) Instant startDate) {
		return new ArrayList<ItemDTO>();
	}

	@PostMapping("/items")
	public ResponseEntity<URI> addItem(@Valid @RequestBody final ItemLightDTO itemDTO) {
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(UUID.randomUUID()).toUri();
		return ResponseEntity.created(location).build();
	}

}