/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.hateoas;

import java.util.Optional;

import javax.annotation.PostConstruct;

import io.swagger.v3.core.util.Json;

import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;

/**
 * The type Hateoas hal provider.
 * @author bnasslahsen
 */
public class HateoasHalProvider {

	/**
	 * The Hateoas properties optional.
	 */
	private final Optional<HateoasProperties> hateoasPropertiesOptional;

	/**
	 * Instantiates a new Hateoas hal provider.
	 *
	 * @param hateoasPropertiesOptional the hateoas properties optional
	 */
	public HateoasHalProvider(Optional<HateoasProperties> hateoasPropertiesOptional) {
		this.hateoasPropertiesOptional = hateoasPropertiesOptional;
	}

	/**
	 * Init.
	 */
	@PostConstruct
	protected void init() {
		if (!isHalEnabled())
			return;
		if (!Jackson2HalModule.isAlreadyRegisteredIn(Json.mapper()))
			Json.mapper().registerModule(new Jackson2HalModule());
	}

	/**
	 * Is hal enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isHalEnabled() {
		return hateoasPropertiesOptional
				.map(HateoasProperties::getUseHalAsDefaultJsonMediaType)
				.orElse(true);
	}

}
