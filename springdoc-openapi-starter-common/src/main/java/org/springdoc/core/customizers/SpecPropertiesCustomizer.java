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

package org.springdoc.core.customizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
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

	/**
	 * The Open api properties.
	 */
	private final OpenAPI openApiProperties;

	/**
	 * Instantiates a new Spec properties customizer.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public SpecPropertiesCustomizer(SpringDocConfigProperties springDocConfigProperties) {
		this.openApiProperties = springDocConfigProperties.getOpenApi();
	}

	/**
	 * Instantiates a new Spec properties customizer.
	 *
	 * @param openApiProperties the open api properties
	 */
	public SpecPropertiesCustomizer(OpenAPI openApiProperties) {
		this.openApiProperties = openApiProperties;
	}

	@Override
	public void customise(OpenAPI openApi) {
		customizeOpenApi(openApi, openApiProperties);
	}

	/**
	 * Customize open api.
	 *
	 * @param openApi           the open api
	 * @param openApiProperties the open api properties
	 */
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

			List<SecurityRequirement> securityRequirementsProperties = openApiProperties.getSecurity();
			if (!CollectionUtils.isEmpty(securityRequirementsProperties)) {
				customizeSecurity(openApi, securityRequirementsProperties);
			}
			if (!CollectionUtils.isEmpty(openApiProperties.getServers())) {
				openApi.setServers(new ArrayList<>(openApiProperties.getServers()));
			}
		}
	}

	/**
	 * Customize security.
	 *
	 * @param openApi              the open api
	 * @param securityRequirementsProperties the security requirements
	 */
	private void customizeSecurity(OpenAPI openApi, List<SecurityRequirement> securityRequirementsProperties) {
		List<SecurityRequirement> securityRequirements = openApi.getSecurity();
		if (CollectionUtils.isEmpty(securityRequirements)) {
			openApi.setSecurity(securityRequirementsProperties);
		}
		else {
			securityRequirementsProperties.forEach(securityRequirement -> {
				if (!securityRequirements.contains(securityRequirement)) {
					securityRequirements.add(securityRequirement);
				}
			});
		}
	}

	/**
	 * Customize paths.
	 *
	 * @param openApi         the open api
	 * @param pathsProperties the paths properties
	 */
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
					resolveString(pathItem::description, pathItemProperties::getDescription);
					resolveString(pathItem::summary, pathItemProperties::getSummary);

					Map<HttpMethod, Operation>  operationMap = pathItem.readOperationsMap();
					Map<HttpMethod, Operation>  operationMapProperties = pathItemProperties.readOperationsMap();

					operationMapProperties.forEach((httpMethod, operationProperties) -> {
						Operation operationToCustomize = operationMap.get(httpMethod);
						if (operationToCustomize != null) {
							resolveString(operationToCustomize::description, operationProperties::getDescription);
							resolveString(operationToCustomize::summary, operationProperties::getSummary);
							resolveSet(operationToCustomize::tags, operationProperties::getTags);
						}
					});
				}});
		}
	}

	/**
	 * Customize components.
	 *
	 * @param openApi              the open api
	 * @param componentsProperties the components properties
	 */
	private void customizeComponents(OpenAPI openApi, Components componentsProperties) {
		Components components = openApi.getComponents();
		if (components == null || CollectionUtils.isEmpty(components.getSchemas())) {
			openApi.components(componentsProperties);
		}
		else {
			Map<String, Schema> schemaMap = components.getSchemas();
			schemaMap.forEach((key, schema) -> {
				if(!CollectionUtils.isEmpty(componentsProperties.getSchemas())) {
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
								resolveString(propSchema::description, propSchemaProperties::getDescription);
								resolveString(propSchema::title, propSchemaProperties::getTitle);
								resolveString(propSchema::example, propSchemaProperties::getExample);
							}
						});
					}
				}
			});
			Map<String, SecurityScheme> securitySchemeMap = components.getSecuritySchemes();
			if (CollectionUtils.isEmpty(securitySchemeMap)) {
				components.setSecuritySchemes(componentsProperties.getSecuritySchemes());
			}
			else {
				securitySchemeMap.forEach((key, securityScheme) -> {
					SecurityScheme securitySchemeToCustomize = components.getSecuritySchemes().get(key);
					if (securitySchemeToCustomize != null) {
						resolveString(securitySchemeToCustomize::description, securityScheme::getDescription);
						resolveString(securitySchemeToCustomize::name, securityScheme::getName);
						resolveType(securitySchemeToCustomize::type, securityScheme::getType);
						resolveIn(securitySchemeToCustomize::in, securityScheme::getIn);
						resolveString(securitySchemeToCustomize::scheme, securityScheme::getScheme);
						resolveString(securitySchemeToCustomize::bearerFormat, securityScheme::getBearerFormat);
						resolveString(securitySchemeToCustomize::openIdConnectUrl, securityScheme::getOpenIdConnectUrl);
						resolveOAuthFlows(securitySchemeToCustomize::flows, securityScheme::getFlows);
						resolveString(securitySchemeToCustomize::$ref, securityScheme::get$ref);
					}
				});
			}
		}
	}

	/**
	 * Customize info.
	 *
	 * @param openApi        the open api
	 * @param infoProperties the info properties
	 */
	private void customizeInfo(OpenAPI openApi, Info infoProperties) {
		Info info = openApi.getInfo();
		if (info != null) {
			resolveString(info::title, infoProperties::getTitle);
			resolveString(info::description, infoProperties::getDescription);
			resolveString(info::version, infoProperties::getVersion);
			resolveString(info::termsOfService, infoProperties::getTermsOfService);
			resolveString(info::summary, infoProperties::getSummary);

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
		else
			openApi.info(infoProperties);
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

	/**
	 * Resolve type.
	 *
	 * @param setter the setter
	 * @param getter the getter
	 */
	private void resolveType(Consumer<Type> setter, Supplier<Object> getter) {
		Type value = (Type) getter.get();
		if (value!=null) {
			setter.accept(value);
		}
	}

	/**
	 * Resolve in.
	 *
	 * @param setter the setter
	 * @param getter the getter
	 */
	private void resolveIn(Consumer<In> setter, Supplier<Object> getter) {
		In value = (In) getter.get();
		if (value!=null) {
			setter.accept(value);
		}
	}

	/**
	 * Resolve o auth flows.
	 *
	 * @param setter the setter
	 * @param getter the getter
	 */
	private void resolveOAuthFlows(Consumer<OAuthFlows> setter, Supplier<Object> getter) {
		OAuthFlows value = (OAuthFlows) getter.get();
		if (value!=null) {
			setter.accept(value);
		}
	}

	
	/**
	 * Resolve set.
	 *
	 * @param setter the setter
	 * @param getter the getter
	 */
	private void resolveSet(Consumer<List> setter, Supplier<List> getter) {
		List value =  getter.get();
		if (!CollectionUtils.isEmpty(value)) {
			setter.accept(value);
		}
	}

}
