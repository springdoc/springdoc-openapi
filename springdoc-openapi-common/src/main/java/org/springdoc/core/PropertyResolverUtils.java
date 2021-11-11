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

package org.springdoc.core;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * The type Property resolver utils.
 * @author bnasslahsen
 */
public class PropertyResolverUtils {

	/**
	 * The Factory.
	 */
	private final ConfigurableBeanFactory factory;

	/**
	 * The Message source.
	 */
	private final MessageSource messageSource;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyResolverUtils.class);

	/**
	 * Instantiates a new Property resolver utils.
	 *
	 * @param factory the factory
	 * @param messageSource the message source
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public PropertyResolverUtils(ConfigurableBeanFactory factory, MessageSource messageSource, SpringDocConfigProperties springDocConfigProperties) {
		this.factory = factory;
		this.messageSource = messageSource;
		this.springDocConfigProperties = springDocConfigProperties;
	}

	/**
	 * Resolve string.
	 *
	 * @param parameterProperty the parameter property
	 * @param locale the locale
	 * @return the string
	 */
	public String resolve(String parameterProperty, Locale locale) {
		if (!springDocConfigProperties.isDisableI18n())
			try {
				return messageSource.getMessage(parameterProperty, null, locale);
			}
			catch (NoSuchMessageException ex) {
				LOGGER.trace(ex.getMessage());
			}
		try {
			return factory.resolveEmbeddedValue(parameterProperty);
		}
		catch (IllegalArgumentException ex) {
			LOGGER.warn(ex.getMessage());
		}
		return parameterProperty;
	}

	/**
	 * Gets factory.
	 *
	 * @return the factory
	 */
	public ConfigurableBeanFactory getFactory() {
		return factory;
	}
}