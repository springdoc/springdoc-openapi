package test.org.springdoc.api.app9.component.controller.hateoas;


import test.org.springdoc.api.app9.component.controller.ComponentsController;
import test.org.springdoc.api.app9.component.dto.DemoComponentDto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DemoComponentDtoModelProcessor implements RepresentationModelProcessor<EntityModel<DemoComponentDto>> {

	@Override
	public EntityModel<DemoComponentDto> process(EntityModel<DemoComponentDto> model) {
		final String id = model.getContent().getId();

		model.add(linkTo(methodOn(ComponentsController.class).findById(id)).withSelfRel());

		return model;
	}

}
