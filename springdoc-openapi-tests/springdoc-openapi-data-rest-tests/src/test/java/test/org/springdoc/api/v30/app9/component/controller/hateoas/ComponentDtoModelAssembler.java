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

package test.org.springdoc.api.v30.app9.component.controller.hateoas;


import test.org.springdoc.api.v30.app9.component.dto.DemoComponentDto;
import test.org.springdoc.api.v30.app9.component.dto.converter.DemoComponentConverter;
import test.org.springdoc.api.v30.app9.component.model.DemoComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({ "deprecation", "unchecked" })
public class ComponentDtoModelAssembler implements RepresentationModelAssembler<DemoComponent, RepresentationModel<EntityModel<DemoComponentDto>>> {

	@Autowired
	private DemoComponentConverter toDtoConverter;

	@Override
	public RepresentationModel<EntityModel<DemoComponentDto>> toModel(DemoComponent entity) {
		return EntityModel.of(toDtoConverter.convert(entity));
	}

}
