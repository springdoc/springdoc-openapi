/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.customizers;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.util.CollectionUtils;

/**
 * Allows externalizing strings in generated openapi schema via properties that follow
 * conventional naming similar or identical to <a href="https://swagger.io/docs/specification/basic-structure/">openapi schema</a>
 * <p>
 * To set value of a string in schema, define an application property that matches the target node
 * with springdoc.spec-properties prefix.
 * <p>
 * Sample supported properties for api-info customization:
 * <ul>
 *     <li>springdoc.spec-properties.info.title - to set title of api-info</li>
 *     <li>springdoc.spec-properties.info.description - to set description of api-info</li>
 *     <li>springdoc.spec-properties.info.version - to set version of api-info</li>
 *     <li>springdoc.spec-properties.info.termsOfService - to set terms of service of api-info</li>
 * </ul>
 * <p>
 * Sample supported properties for components customization:
 * <ul>
 *     <li>springdoc.spec-properties.components.User.description - to set description of User model</li>
 *     <li>springdoc.spec-properties.components.User.properties.name.description - to set description of 'name' property</li>
 *     <li>springdoc.spec-properties.components.User.properties.name.example - to set example of 'name' property</li>
 * </ul>
 * <p>
 * Sample supported properties for paths/operationIds customization:
 * <ul>
 *     <li>springdoc.spec-properties.paths.{operationId}.description - to set description of {operationId}</li>
 *     <li>springdoc.spec-properties.paths.{operationId}.summary - to set summary of {operationId}</li>
 * </ul>
 * <p>
 * Support for groped openapi customization is similar to the above, but with a group name prefix.
 * E.g.
 * <ul>
 *     <li>springdoc.spec-properties.{group-name}.info.title - to set title of api-info</li>
 *     <li>springdoc.spec-properties.{group-name}.components.User.description - to set description of User model</li>
 *     <li>springdoc.spec-properties.{group-name}.paths.{operationId}.description - to set description of {operationId}</li>
 * </ul>
 *
 * @author Anton Tkachenko tkachenkoas@gmail.com
 * @author bnasslahsen
 */
public class SpecPropertiesCustomizer implements GlobalOpenApiCustomizer {

	private final OpenAPI openApiProperties;

	public SpecPropertiesCustomizer(SpringDocConfigProperties springDocConfigProperties) {
		this.openApiProperties = springDocConfigProperties.getOpenApi();
	}

	public SpecPropertiesCustomizer(OpenAPI openApiProperties) {
		this.openApiProperties = openApiProperties;
	}

	@Override
	public void customise(OpenAPI openApi) {
		customizeOpenApi(openApi, openApiProperties);
	}

	private void customizeOpenApi(OpenAPI openApi, OpenAPI openApiProperties) {
		if (openApiProperties != null) {
			Info infoProperties = openApiProperties.getInfo();
			if (infoProperties != null)
				customizeInfo(openApi, infoProperties);

			Components componentsProperties = openApiProperties.getComponents();
			if (componentsProperties != null)
				customizeComponents(openApi, componentsProperties);
			Paths pathsProperties = openApiProperties.getPaths();
			if (pathsProperties != null)
				customizePaths(openApi, pathsProperties);
		}
	}

	private void customizePaths(OpenAPI openApi, Paths pathsProperties) {
		Paths paths = openApi.getPaths();
		if (paths == null) {
			openApi.paths(pathsProperties);
		}
		else {
			paths.forEach((path, pathItem) -> {
				if (path.startsWith("/")) {
					path = path.substring(1); // Remove the leading '/'
				}
				PathItem pathItemProperties = pathsProperties.get(path);
				if (pathItemProperties != null) {
					resolveString(pathItem::setDescription, pathItemProperties::getDescription);
					resolveString(pathItem::setSummary, pathItemProperties::getSummary);
					List<Operation> operations = pathItem.readOperations();
					List<Operation> operationsProperties = pathItemProperties.readOperations();
					for (int i = 0; i < operations.size(); i++) {
						Operation operation = operations.get(i);
						Operation operationProperties = operationsProperties.get(i);
						resolveString(operation::setDescription, operationProperties::getDescription);
						resolveString(operation::setSummary, operationProperties::getSummary);
					}
				}});
		}
	}
	
	private void customizeComponents(OpenAPI openApi, Components componentsProperties) {
		Components components = openApi.getComponents();
		if (components == null || CollectionUtils.isEmpty(components.getSchemas())) {
			openApi.components(componentsProperties);
		}
		else {
			Map<String, Schema> schemaMap = components.getSchemas();
			schemaMap.forEach((key, schema) -> {
				Schema schemaProperties = componentsProperties.getSchemas().get(key);
				if (schemaProperties != null) {
					resolveString(schema::setDescription, schemaProperties::getDescription);
					Map<String, Schema> properties = schema.getProperties();
					if (CollectionUtils.isEmpty(properties)) {
						return;
					}
					properties.forEach((propKey, propSchema) -> {
						Schema propSchemaProperties = (Schema) schemaProperties.getProperties().get(propKey);
						if (propSchemaProperties != null) {
							resolveString(propSchema::setDescription, propSchemaProperties::getDescription);
							resolveString(propSchema::setExample, propSchemaProperties::getExample);
						}
					});
				}
			});
		}
	}

	private void customizeInfo(OpenAPI openApi, Info infoProperties) {
		Info info = openApi.getInfo();
		if (info != null) {
			resolveString(info::title, infoProperties::getTitle);
			resolveString(info::description, infoProperties::getDescription);
			resolveString(info::version, infoProperties::getVersion);
			resolveString(info::termsOfService, infoProperties::getTermsOfService);
		}
		else
			openApi.info(infoProperties);

		License license = info.getLicense();
		License licenseProperties = infoProperties.getLicense();
		if (license != null) {
			resolveString(license::name, licenseProperties::getName);
			resolveString(license::url, licenseProperties::getUrl);
		}
		else
			info.license(licenseProperties);

		Contact contact = info.getContact();
		Contact contactProperties = infoProperties.getContact();
		if (contact != null) {
			resolveString(contact::name, contactProperties::getName);
			resolveString(contact::email, contactProperties::getEmail);
			resolveString(contact::url, contactProperties::getUrl);
		}
		else
			info.contact(contactProperties);
	}


	/**
	 * Resolve string.
	 *
	 * @param setter the setter
	 * @param getter the value
	 */
	private void resolveString(Consumer<String> setter, Supplier<Object> getter) {
		String value = (String) getter.get();
		if (StringUtils.isNotBlank(value)) {
			setter.accept(value);
		}
	}

}
