package test.org.springdoc.api.app9.component.controller.hateoas;



import test.org.springdoc.api.app9.component.dto.DemoComponentDto;
import test.org.springdoc.api.app9.component.dto.converter.DemoComponentConverter;
import test.org.springdoc.api.app9.component.model.DemoComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"deprecation","unchecked"})
public class ComponentDtoModelAssembler implements RepresentationModelAssembler<DemoComponent, RepresentationModel<EntityModel<DemoComponentDto>>> {

	@Autowired
	private DemoComponentConverter toDtoConverter;

	@Override
	public RepresentationModel<EntityModel<DemoComponentDto>> toModel(DemoComponent entity) {
		return new EntityModel<DemoComponentDto>(toDtoConverter.convert(entity));
	}

}
