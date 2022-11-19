package test.org.springdoc.api.app5.controller;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import test.org.springdoc.api.app5.entities.CompanyDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Davide Pedone
 * 2020
 */
@RestController
@RequiredArgsConstructor
public class CompanyController {


	@PostMapping
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Details of the Item to be created",
			content = @Content(schema = @Schema(ref = "#/components/schemas/CompanyDtoNew")))
	public CompanyDto create(@Valid @RequestBody final CompanyDto companyDto) {

		return null;
	}

}
