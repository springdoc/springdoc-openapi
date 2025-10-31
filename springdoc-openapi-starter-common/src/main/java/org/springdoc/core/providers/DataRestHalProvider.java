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

import org.springdoc.core.data.SpringDocJackson2HalModule;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.hateoas.autoconfigure.HateoasProperties;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

/**
 * The type Data rest hal provider.
 *
 * @author bnasslahsen
 */
public class DataRestHalProvider extends HateoasHalProvider implements InitializingBean {

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

	@Override
	public void afterPropertiesSet() {
		if (!isHalEnabled())
			return;
		if (!SpringDocJackson2HalModule.isAlreadyRegisteredIn(objectMapperProvider.jsonMapper()))
			objectMapperProvider.jsonMapper().registerModule(new SpringDocJackson2HalModule());
	}

	@Override
	public boolean isHalEnabled() {
		return repositoryRestConfigurationOptional
				.map(RepositoryRestConfiguration::useHalAsDefaultJsonMediaType)
				.orElse(true);
	}
}
