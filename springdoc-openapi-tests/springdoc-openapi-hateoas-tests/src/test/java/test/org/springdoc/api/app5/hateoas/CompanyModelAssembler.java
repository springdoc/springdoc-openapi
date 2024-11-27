package test.org.springdoc.api.app5.hateoas;

import test.org.springdoc.api.app5.controller.CompanyController;
import test.org.springdoc.api.app5.entities.Company;
import test.org.springdoc.api.app5.entities.CompanyDto;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class CompanyModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDto> {

	public CompanyModelAssembler() {
		super(CompanyController.class, CompanyDto.class);
	}

	@Override
	@NonNull
	public CompanyDto toModel(@NonNull final Company company) {
		return CompanyDto.builder()
				.id(company.getId())
				.name(company.getName())
				.build();
	}
}