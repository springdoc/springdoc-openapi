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

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

/**
 * The type Repository rest configuration provider.
 *
 * @author bnasslahsen
 */
public class RepositoryRestConfigurationProvider {


	/**
	 * The Optional repository rest configuration.
	 */
	private final Optional<RepositoryRestConfiguration> optionalRepositoryRestConfiguration;

	/**
	 * Instantiates a new Repository rest configuration provider.
	 *
	 * @param optionalRepositoryRestConfiguration the optional repository rest configuration
	 */
	public RepositoryRestConfigurationProvider(Optional<RepositoryRestConfiguration> optionalRepositoryRestConfiguration) {
		this.optionalRepositoryRestConfiguration = optionalRepositoryRestConfiguration;
	}

	/**
	 * Gets repository rest configuration.
	 *
	 * @return the repository rest configuration
	 */
	public RepositoryRestConfiguration getRepositoryRestConfiguration() {
		return optionalRepositoryRestConfiguration.orElse(null);
	}

	/**
	 * Is repository rest configuration present boolean.
	 *
	 * @return the boolean
	 */
	public boolean isRepositoryRestConfigurationPresent() {
		return optionalRepositoryRestConfiguration.isPresent();
	}
}
