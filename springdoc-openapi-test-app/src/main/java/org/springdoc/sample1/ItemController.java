package org.springdoc.sample1;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "items")
public class ItemController {

	@GetMapping("/items")
	public List<ItemDTO> showItems(@RequestParam("cusID") final String customerID) {
		return new ArrayList<ItemDTO>();
	}
   
	@PostMapping("/items")
	public ResponseEntity<URI> addItem(@Valid @RequestBody final ItemLightDTO itemDTO) {
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(UUID.randomUUID()).toUri();
		return ResponseEntity.created(location).build();
	}
}