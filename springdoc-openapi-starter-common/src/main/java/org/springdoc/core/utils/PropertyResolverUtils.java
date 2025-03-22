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

package org.springdoc.core.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.SpecVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.CollectionUtils;

/**
 * The type Property resolver utils.
 *
 * @author bnasslahsen
 */
public class PropertyResolverUtils {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyResolverUtils.class);

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
	 * Instantiates a new Property resolver utils.
	 *
	 * @param factory                   the factory
	 * @param messageSource             the message source
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
	 * @param locale            the locale
	 * @return the string
	 */
	public String resolve(String parameterProperty, Locale locale) {
		String result = parameterProperty;
		if (parameterProperty != null) {
			if (!springDocConfigProperties.isDisableI18n())
				try {
					result = messageSource.getMessage(parameterProperty, null, locale);
				}
				catch (NoSuchMessageException ex) {
					LOGGER.trace(ex.getMessage());
				}
			if (parameterProperty.equals(result))
				try {
					result = factory.resolveEmbeddedValue(parameterProperty);
				}
				catch (IllegalArgumentException ex) {
					LOGGER.warn(ex.getMessage());
				}
		}
		return result;
	}

	/**
	 * Returns a string where all leading indentation has been removed from each line.
	 * It detects the smallest common indentation of all the lines in the input string,
	 * and removes it.
	 * If the input text is {@code null}, the method returns {@code null}.
	 *
	 *	@param text The original string with possible leading indentation.
	 *	@return The string with the smallest common leading indentation removed from each line,
	 *          or {@code null} if the input text is {@code null}.
	 */
	public String trimIndent(String text) {
		if (text == null) {
			return null;
		}
		final String newLine = "\n";
		String[] lines = text.split("\\r?\\n");
		int minIndent = resolveMinIndent(lines);
		try {
			return Arrays.stream(lines)
					.map(line -> line.substring(Math.min(line.length(), minIndent)))
					.collect(Collectors.joining(newLine));
		} catch (Exception ex) {
			LOGGER.warn(ex.getMessage());
			return text;
		}
	}

	/**
	 * Resolve min indent int.
	 *
	 * @param lines the lines
	 * @return the int
	 */
	private int resolveMinIndent(String[] lines) {
		return Arrays.stream(lines)
			.filter(line -> !line.trim().isEmpty())
			.mapToInt(this::countLeadingSpaces)
			.min()
			.orElse(0);
	}

	/**
	 * Count leading spaces int.
	 *
	 * @param line the line
	 * @return the int
	 */
	private int countLeadingSpaces(String line) {
        int count = 0;
        for (char ch : line.toCharArray()) {
            if (ch != ' ' && ch != '\t') break;
            count++;
        }
        return count;
    }

	/**
	 * Gets factory.
	 *
	 * @return the factory
	 */
	public ConfigurableBeanFactory getFactory() {
		return factory;
	}

	/**
	 * Gets spring doc config properties.
	 *
	 * @return the spring doc config properties
	 */
	public SpringDocConfigProperties getSpringDocConfigProperties() {
		return springDocConfigProperties;
	}

	/**
	 * Gets spec version.
	 *
	 * @return the spec version
	 */
	public SpecVersion getSpecVersion() {
		return springDocConfigProperties.getSpecVersion();
	}

	/**
	 * Is openapi 31 boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOpenapi31() {
		return springDocConfigProperties.isOpenapi31();
	}

	/**
	 * Is resolve extensions properties boolean.
	 *
	 * @return the boolean
	 */
	public boolean isResolveExtensionsProperties() {
		return springDocConfigProperties.getApiDocs().isResolveExtensionsProperties();
	}

	/**
	 * Resolve extensions map.
	 *
	 * @param locale     the locale
	 * @param extensions the extensions
	 * @return the map
	 */
	public Map<String, Object> resolveExtensions(Locale locale, Map<String, Object> extensions) {
		if (!CollectionUtils.isEmpty(extensions)) {
			Map<String, Object> extensionsResolved = new HashMap<>();
			extensions.forEach((key, value) -> {
				String keyResolved = resolve(key, locale);
				if (value instanceof HashMap<?, ?>) {
					Map<String, Object> valueResolved = new HashMap<>();
					((HashMap<?, ?>) value).forEach((key1, value1) -> {
						String key1Resolved = resolve(key1.toString(), locale);
						String value1Resolved = resolve(value1.toString(), locale);
						valueResolved.put(key1Resolved, value1Resolved);
					});
					extensionsResolved.put(keyResolved, valueResolved);
				}
				else
					extensionsResolved.put(keyResolved, value);
			});
			return extensionsResolved;
		}
		else
			return extensions;
	}
}
