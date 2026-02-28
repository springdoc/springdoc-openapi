/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.core.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

/**
 * The type Spring doc app initializer.
 *
 * @author bnasslahsen
 */
public class SpringDocAppInitializer {

	/**
	 * The Endpoint.
	 */
	private final String endpoint;

	/**
	 * The Property.
	 */
	private final String property;

	/**
	 * The Springdoc enabled.
	 */
	private final boolean springdocEnabled;
	
	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDocAppInitializer.class);

	/**
	 * Instantiates a new Spring doc app initializer.
	 *
	 * @param endpoint         the endpoint
	 * @param property         the property
	 * @param springdocEnabled the springdoc enabled
	 */
	public SpringDocAppInitializer(String endpoint, String property, boolean springdocEnabled) {
		this.endpoint = endpoint;
		this.property = property;
		this.springdocEnabled = springdocEnabled;
	}

	/**
	 * Init.
	 */
	@EventListener(ApplicationReadyEvent.class)
	@Order(0)
	public void init() {
		if(!this.springdocEnabled)
			LOGGER.warn("SpringDoc {} endpoint is enabled by default. To disable it in production, set the property '{}=false'", endpoint, property);
	}
}
