/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package test.org.springdoc.api.v30.app9.component.controller;


import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import test.org.springdoc.api.v30.app9.component.controller.hateoas.ComponentDtoModelAssembler;
import test.org.springdoc.api.v30.app9.component.dto.DemoComponentDto;
import test.org.springdoc.api.v30.app9.component.dto.converter.DemoComponentConverter;
import test.org.springdoc.api.v30.app9.component.model.DemoComponent;
import test.org.springdoc.api.v30.app9.component.service.ComponentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/components", produces = "application/json")
@Tag(name = "components", description = "Everything about components")
public class ComponentsController {

	@Autowired
	private ComponentsService componentsService;

	@Autowired
	private DemoComponentConverter toDtoConverter;

	@Autowired
	private ComponentDtoModelAssembler componentDtoModelAssembler;

	@Autowired
	private PagedResourcesAssembler<DemoComponent> pagedResourcesAssembler;

	@Operation(summary = "List the components")
	@PageableAsQueryParam
	@GetMapping
	public ResponseEntity<PagedModel<RepresentationModel<EntityModel<DemoComponentDto>>>> findAll(@Parameter(hidden = true) Pageable pageable) {
		Page<DemoComponent> results = componentsService.findAll(pageable);

		return ResponseEntity.ok(pagedResourcesAssembler.toModel(results, componentDtoModelAssembler));
	}

	@Operation(summary = "Get one component by its ID", description = "Returns a single component", //
			responses = { //
					@ApiResponse(responseCode = "200", description = "Component found"), //
					@ApiResponse(responseCode = "404", description = "Component not found", content = { @Content(schema = @Schema(implementation = Void.class)) }) //
			})
	@GetMapping("/{componentId}")
	public ResponseEntity<EntityModel<DemoComponentDto>> findById(@PathVariable String componentId) {
		Optional<DemoComponent> foundComponent = componentsService.findById(componentId);

		if (foundComponent.isPresent()) {
			return ResponseEntity.ok(EntityModel.of(toDtoConverter.convert(foundComponent.get()), //
					linkTo(methodOn(ComponentsController.class).findAll(null)).withRel("components")));
		}

		return ResponseEntity.notFound().location(linkTo(methodOn(ComponentsController.class).findAll(null)).toUri()).build();
	}

}
