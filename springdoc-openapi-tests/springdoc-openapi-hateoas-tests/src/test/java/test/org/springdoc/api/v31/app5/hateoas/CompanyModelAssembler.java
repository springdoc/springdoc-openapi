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

package test.org.springdoc.api.v31.app5.hateoas;

import test.org.springdoc.api.v31.app5.controller.CompanyController;
import test.org.springdoc.api.v31.app5.entities.Company;
import test.org.springdoc.api.v31.app5.entities.CompanyDto;

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