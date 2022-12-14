/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.CollectionUtils;

/**
 * The type Property resolver utils.
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
	 * Resolve properties info.
	 *
	 * @param servers the servers
	 * @param locale the locale
	 * @return the servers
	 */
	public List<Server> resolveProperties(List<Server> servers, Locale locale) {
		servers.forEach(server -> {
			resolveProperty(server::getUrl, server::url, this, locale);
			resolveProperty(server::getDescription, server::description, this, locale);
			if (CollectionUtils.isEmpty(server.getVariables()))
				server.setVariables(null);
		});
		return servers;
	}

	/**
	 * Resolve properties info.
	 *
	 * @param info the info
	 * @param locale the locale
	 * @return the info
	 */
	public Info resolveProperties(Info info, Locale locale) {
		resolveProperty(info::getTitle, info::title, this, locale);
		resolveProperty(info::getDescription, info::description, this, locale);
		resolveProperty(info::getVersion, info::version, this, locale);
		resolveProperty(info::getTermsOfService, info::termsOfService, this, locale);

		License license = info.getLicense();
		if (license != null) {
			resolveProperty(license::getName, license::name, this, locale);
			resolveProperty(license::getUrl, license::url, this, locale);
		}

		Contact contact = info.getContact();
		if (contact != null) {
			resolveProperty(contact::getName, contact::name, this, locale);
			resolveProperty(contact::getEmail, contact::email, this, locale);
			resolveProperty(contact::getUrl, contact::url, this, locale);
		}
		return info;
	}

	/**
	 * Resolve properties schema.
	 *
	 * @param schema the schema
	 * @param locale the locale
	 * @return the schema
	 */
	@SuppressWarnings("unchecked")
	public Schema resolveProperties(Schema<?> schema, Locale locale) {
		resolveProperty(schema::getName, schema::name, this, locale);
		resolveProperty(schema::getTitle, schema::title, this, locale);
		resolveProperty(schema::getDescription, schema::description, this, locale);

		Map<String, Schema> properties = schema.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			LinkedHashMap<String, Schema> resolvedSchemas = properties.entrySet().stream().map(es -> {
				es.setValue(resolveProperties(es.getValue(), locale));
				if(es.getValue() instanceof ArraySchema){
					ArraySchema arraySchema = (ArraySchema) es.getValue();
					resolveProperties(arraySchema.getItems(), locale);
				}
				return es;
			}).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
					LinkedHashMap::new));
			schema.setProperties(resolvedSchemas);
		}

		return schema;
	}

	/**
	 * Resolve property.
	 *
	 * @param getProperty the get property
	 * @param setProperty the set property
	 * @param propertyResolverUtils the property resolver utils
	 * @param locale the locale
	 */
	private void resolveProperty(Supplier<String> getProperty, Consumer<String> setProperty,
			PropertyResolverUtils propertyResolverUtils, Locale locale) {
		String value = getProperty.get();
		if (StringUtils.isNotBlank(value)) {
			setProperty.accept(propertyResolverUtils.resolve(value, locale));
		}
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
}