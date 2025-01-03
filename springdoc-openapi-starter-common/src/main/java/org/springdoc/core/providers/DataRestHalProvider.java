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

package org.springdoc.core.providers;

import java.util.Optional;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;

/**
 * The type Data rest hal provider.
 *
 * @author bnasslahsen
 */
public class DataRestHalProvider extends HateoasHalProvider {

	/**
	 * The Repository rest configuration optional.
	 */
	private final Optional<RepositoryRestConfiguration> repositoryRestConfigurationOptional;

	/**
	 * Instantiates a new Data rest hal provider.
	 *
	 * @param repositoryRestConfigurationOptional the repository rest configuration optional
	 * @param hateoasPropertiesOptional           the hateoas properties optional
	 * @param objectMapperProvider                the object mapper provider
	 */
	public DataRestHalProvider(Optional<RepositoryRestConfiguration> repositoryRestConfigurationOptional, Optional<HateoasProperties> hateoasPropertiesOptional,
			ObjectMapperProvider objectMapperProvider) {
		super(hateoasPropertiesOptional, objectMapperProvider);
		this.repositoryRestConfigurationOptional = repositoryRestConfigurationOptional;
	}

	@PostConstruct
	@Override
	protected void init() {
		if (!isHalEnabled())
			return;
		if (!Jackson2HalModule.isAlreadyRegisteredIn(objectMapperProvider.jsonMapper()))
			objectMapperProvider.jsonMapper().registerModule(new Jackson2HalModule());
	}

	@Override
	public boolean isHalEnabled() {
		return repositoryRestConfigurationOptional
				.map(RepositoryRestConfiguration::useHalAsDefaultJsonMediaType)
				.orElse(true);
	}
}
