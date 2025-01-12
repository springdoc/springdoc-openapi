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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.data.util.TypeInformation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.utils.SpringDocUtils.isComposedSchema;

/**
 * The class Spring doc data rest utils.
 *
 * @author bnasslashen
 */
public class SpringDocDataRestUtils {

	/**
	 * The constant REQUEST_BODY.
	 */
	private static final String REQUEST_BODY = "RequestBody";

	/**
	 * The constant RESPONSE.
	 */
	private static final String RESPONSE = "Response";

	/**
	 * The constant EMBEDDED.
	 */
	private static final String EMBEDDED = "_embedded";

	/**
	 * The Link relation provider.
	 */
	private final LinkRelationProvider linkRelationProvider;

	/**
	 * The Entity ino map.
	 */
	private final HashMap<String, EntityInfo> entityInoMap = new HashMap();

	/**
	 * The Repository rest configuration.
	 */
	private final RepositoryRestConfiguration repositoryRestConfiguration;

	/**
	 * Instantiates a new Spring doc data rest utils.
	 *
	 * @param linkRelationProvider        the link relation provider
	 * @param repositoryRestConfiguration the repository rest configuration
	 */
	public SpringDocDataRestUtils(LinkRelationProvider linkRelationProvider, RepositoryRestConfiguration repositoryRestConfiguration) {
		this.linkRelationProvider = linkRelationProvider;
		this.repositoryRestConfiguration = repositoryRestConfiguration;
	}

	/**
	 * Customise.
	 *
	 * @param openAPI            the open api
	 * @param mappings           the mappings
	 * @param persistentEntities the persistent entities
	 */
	public void customise(OpenAPI openAPI, ResourceMappings mappings, PersistentEntities persistentEntities) {

		for (PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity : persistentEntities) {
			TypeInformation<?> typeInformation = persistentEntity.getTypeInformation();
			Class domainType = typeInformation.getType();
			ResourceMetadata resourceMetadata = mappings.getMetadataFor(domainType);
			final PersistentEntity<?, ?> entity = persistentEntities.getRequiredPersistentEntity(domainType);
			EntityInfo entityInfo = new EntityInfo();
			entityInfo.setDomainType(domainType);
			List<String> ignoredFields = getIgnoredFields(resourceMetadata, entity);
			if (!repositoryRestConfiguration.isIdExposedFor(entity.getType()))
				entityInfo.setIgnoredFields(ignoredFields);
			List<String> associationsFields = getAssociationsFields(resourceMetadata, entity);
			entityInfo.setAssociationsFields(associationsFields);
			entityInoMap.put(domainType.getSimpleName(), entityInfo);
		}
		
		openAPI.getPaths().entrySet().stream()
				.forEach(stringPathItemEntry -> {
					PathItem pathItem = stringPathItemEntry.getValue();
					pathItem.readOperations().forEach(operation -> {
						boolean openapi31 = SpecVersion.V31 == openAPI.getSpecVersion();
						RequestBody requestBody = operation.getRequestBody();
						updateRequestBody(openAPI, requestBody, openapi31);
						ApiResponses apiResponses = operation.getResponses();
						apiResponses.forEach((code, apiResponse) -> updateApiResponse(openAPI, openAPI.getComponents(), apiResponse, openapi31));

					});
				});
	}

	/**
	 * Update api response.
	 *
	 * @param openAPI     the open api
	 * @param components  the components
	 * @param apiResponse the api response
	 * @param openapi31   the openapi 31
	 */
	private void updateApiResponse(OpenAPI openAPI, Components components, ApiResponse apiResponse, boolean openapi31) {
		if (apiResponse != null && !CollectionUtils.isEmpty(apiResponse.getContent())) {
			apiResponse.getContent().values().forEach(mediaType -> {
				Schema schema = mediaType.getSchema();
				if (schema.get$ref() != null && !schema.get$ref().endsWith(RESPONSE)) {
					String key = schema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
					Set<String> entitiesNames = entityInoMap.keySet();
					entitiesNames.forEach(entityName -> {
						if (key.endsWith(entityName))
							updateResponseSchema(entityName, components.getSchemas().get(key), openAPI.getComponents(), openapi31);
					});
				}
			});
		}
	}

	/**
	 * Update request body.
	 *
	 * @param openAPI     the open api
	 * @param requestBody the request body
	 * @param openapi31   the openapi 31
	 */
	private void updateRequestBody(OpenAPI openAPI, RequestBody requestBody, boolean openapi31) {
		if (requestBody != null && !CollectionUtils.isEmpty(requestBody.getContent())) {
			requestBody.getContent().values().forEach(mediaType -> {
				Schema schema = mediaType.getSchema();
				if (schema.get$ref() != null && !schema.get$ref().endsWith(REQUEST_BODY)) {
					String key = schema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
					if (entityInoMap.containsKey(key))
						updateRequestBodySchema(key, schema, openAPI.getComponents(), openapi31);
				}
				else if (isComposedSchema(schema)) {
					updateComposedSchema(schema.getAllOf(), schema.getOneOf(), REQUEST_BODY, openAPI.getComponents(), openapi31);
				}
			});
		}
	}

	/**
	 * Gets request body schema.
	 *
	 * @param className  the class name
	 * @param schema     the schema
	 * @param components the components
	 * @param openapi31  the openapi 31
	 * @return the request body schema
	 */
	private void updateRequestBodySchema(String className, Schema schema, Components components, boolean openapi31) {
		//update ref
		String newKey = className + REQUEST_BODY;
		schema.set$ref(newKey);
		//create new schema
		Class schemaImplementation = entityInoMap.get(className).getDomainType();
		ResolvedSchema resolvedSchema = ModelConverters.getInstance(openapi31).readAllAsResolvedSchema(new AnnotatedType().type(schemaImplementation));
		Map<String, Schema> schemaMap;
		if (resolvedSchema != null) {
			schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach((key, referencedSchema) -> {
					components.addSchemas(key + REQUEST_BODY, referencedSchema);
					referencedSchema.setName(key + REQUEST_BODY);
					Map properties = referencedSchema.getProperties();
					updateRequestBodySchemaProperties(key, referencedSchema, properties);
					if (isComposedSchema(referencedSchema))
						updateComposedSchema(referencedSchema.getAllOf(), referencedSchema.getOneOf(), REQUEST_BODY, components, openapi31);
				});
			}
		}
	}

	/**
	 * Update request body schema properties.
	 *
	 * @param key              the key
	 * @param referencedSchema the referenced schema
	 * @param properties       the properties
	 */
	private void updateRequestBodySchemaProperties(String key, Schema referencedSchema, Map properties) {
		if (!CollectionUtils.isEmpty(referencedSchema.getProperties())) {
			Iterator<Entry<String, Schema>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Schema> entry = it.next();
				String propId = entry.getKey();
				if (entityInoMap.containsKey(key) && entityInoMap.get(key).getAssociationsFields().contains(propId)) {
					if (entry.getValue().getItems()!=null)
						referencedSchema.addProperty(propId, new ArraySchema().items(new StringSchema()));
					else
						referencedSchema.addProperty(propId, new StringSchema());
				}
			}
		}
	}

	/**
	 * Update response schema schema.
	 *
	 * @param className      the class name
	 * @param existingSchema the existing schema
	 * @param components     the components
	 * @param openapi31      the openapi 31
	 * @return the schema
	 */
	private Schema updateResponseSchema(String className, Schema existingSchema, Components components, boolean openapi31) {
		Map<String, Schema> properties = existingSchema.getProperties();
		EntityInfo entityInfo = entityInoMap.get(className);
		if (!CollectionUtils.isEmpty(properties)) {
			Iterator<Entry<String, Schema>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Schema> entry = it.next();
				String propId = entry.getKey();
				if (entityInfo.getIgnoredFields().contains(propId) || entityInfo.getAssociationsFields().contains(propId))
					it.remove();
				else if (EMBEDDED.equals(propId)) {
					updateResponseSchemaEmbedded(components, entityInfo, entry, openapi31);
				}
			}
		}
		return existingSchema;
	}

	/**
	 * Update response schema embedded.
	 *
	 * @param components the components
	 * @param entityInfo the entity info
	 * @param entry      the entry
	 * @param openapi31  the openapi 31
	 */
	private void updateResponseSchemaEmbedded(Components components, EntityInfo entityInfo, Entry<String, Schema> entry, boolean openapi31) {
		String entityClassName = linkRelationProvider.getCollectionResourceRelFor(entityInfo.getDomainType()).value();
		Schema itemsSchema = null;
		if (openapi31) {
			JsonSchema jsonSchema = (JsonSchema) entry.getValue().getProperties().get(entityClassName);
			if (jsonSchema != null)
				itemsSchema = jsonSchema.getItems();
		}
		else {
			ArraySchema arraySchema = (ArraySchema) entry.getValue().getProperties().get(entityClassName);
			if (arraySchema != null)
				itemsSchema = arraySchema.getItems();
		}
		if (itemsSchema != null) {
			Set<String> entitiesNames = entityInoMap.keySet();
			if (itemsSchema.get$ref() != null && !itemsSchema.get$ref().endsWith(RESPONSE)) {
				String key = itemsSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
				if (entitiesNames.contains(key)) {
					String newKey = itemsSchema.get$ref() + RESPONSE;
					createNewResponseSchema(key, components, openapi31);
					itemsSchema.set$ref(newKey);
					updateResponseSchema(key, components.getSchemas().get(key + RESPONSE), components, openapi31);
				}
			}
			else if (isComposedSchema(itemsSchema)) {
				updateComposedSchema(itemsSchema.getAllOf(), itemsSchema.getOneOf(), RESPONSE, components, openapi31);
			}
		}
	}

	/**
	 * Create new response schema schema.
	 *
	 * @param className  the class name
	 * @param components the components
	 * @param openapi31  the openapi 31
	 * @return the schema
	 */
	private Schema createNewResponseSchema(String className, Components components, boolean openapi31) {
		Class schemaImplementation = entityInoMap.get(className).getDomainType();
		Schema schemaObject = new Schema();
		ResolvedSchema resolvedSchema = ModelConverters.getInstance(openapi31).readAllAsResolvedSchema(new AnnotatedType().type(schemaImplementation));
		Map<String, Schema> schemaMap;
		if (resolvedSchema != null) {
			schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach((key, referencedSchema) ->
						addSchemas(components, key, referencedSchema, openapi31));
			}
			schemaObject = resolvedSchema.schema;
		}
		return schemaObject;

	}

	/**
	 * Add schemas.
	 *
	 * @param components       the components
	 * @param key              the key
	 * @param referencedSchema the referenced schema
	 * @param openapi31        the openapi 31
	 */
	private void addSchemas(Components components, String key, Schema referencedSchema, boolean openapi31) {
		Map<String, Schema> properties = referencedSchema.getProperties();
		if (!CollectionUtils.isEmpty(referencedSchema.getProperties())) {
			Iterator<Entry<String, Schema>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Schema> entry = it.next();
				String propId = entry.getKey();
				if (entityInoMap.containsKey(key) && entityInoMap.get(key).getIgnoredFields().contains(propId)) {
					it.remove();
				}
			}
		}
		if (isComposedSchema(referencedSchema)) {
			updateComposedSchema(referencedSchema.getAllOf(), referencedSchema.getOneOf(), RESPONSE, null, openapi31);
		}
		components.addSchemas(key + RESPONSE, referencedSchema);
	}

	/**
	 * Update composed schema.
	 *
	 * @param allOf      the all of
	 * @param oneOf      the one of
	 * @param suffix     the suffix
	 * @param components the components
	 * @param openapi31  the openapi 31
	 */
	private void updateComposedSchema(List<Schema> allOf, List<Schema> oneOf, String suffix, Components components, boolean openapi31) {
		//Update the allOf
		List<Schema> allOfSchemas = allOf;
		updateKey(allOfSchemas, suffix, components, openapi31);
		//Update the oneOf
		List<Schema> oneOfSchemas = oneOf;
		updateKey(oneOfSchemas, suffix, components, openapi31);
	}

	/**
	 * Update key.
	 *
	 * @param allSchemas the all schemas
	 * @param suffix     can be Resposne or RequestBody
	 * @param components the components
	 * @param openapi31  the openapi 31
	 */
	private void updateKey(List<Schema> allSchemas, String suffix, Components components, boolean openapi31) {
		if (!CollectionUtils.isEmpty(allSchemas))
			for (Schema allSchema : allSchemas) {
				if (allSchema.get$ref() != null) {
					String allKey = allSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
					updateSingleKey(suffix, components, allSchema, allKey, openapi31);
				}
			}
	}

	/**
	 * Update single key.
	 *
	 * @param suffix     the suffix
	 * @param components the components
	 * @param allSchema  the all schema
	 * @param allKey     the all key
	 * @param openapi31  the openapi 31
	 */
	private void updateSingleKey(String suffix, Components components, Schema allSchema, String allKey, boolean openapi31) {
		if (!allKey.endsWith(REQUEST_BODY) && !allKey.endsWith(RESPONSE)) {
			String newAllKey = allKey + suffix;
			allSchema.set$ref(newAllKey);
			if (components != null && !components.getSchemas().containsKey(newAllKey) && entityInoMap.containsKey(allKey) && RESPONSE.equals(suffix)) {
				createNewResponseSchema(allKey, components, openapi31);
			}
		}
	}

	/**
	 * Gets associations fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param entity           the entity
	 * @return the associations fields
	 */
	private List<String> getAssociationsFields(ResourceMetadata
			resourceMetadata, PersistentEntity<?, ?> entity) {
		List<String> associationsFields = new ArrayList<>();
		entity.doWithAssociations((SimpleAssociationHandler) association -> {
			PersistentProperty<?> property = association.getInverse();
			ResourceMapping mapping = resourceMetadata.getMappingFor(property);
			if (mapping.isExported()) {
				String filedName = mapping.getRel().value();
				associationsFields.add(filedName);
			}
		});
		return associationsFields;
	}

	/**
	 * Gets ignored fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param entity           the entity
	 * @return the ignored fields
	 */
	private List<String> getIgnoredFields(ResourceMetadata
			resourceMetadata, PersistentEntity<?, ?> entity) {
		List<String> ignoredFields = new ArrayList<>();
		if (entity != null && entity.getIdProperty() != null) {
			String idField = Objects.requireNonNull(entity.getIdProperty()).getName();
			ignoredFields.add(idField);
			entity.doWithAssociations((SimpleAssociationHandler) association -> {
				PersistentProperty<?> property = association.getInverse();
				ResourceMapping mapping = resourceMetadata.getMappingFor(property);
				if (mapping.isExported()) {
					String filedName = mapping.getRel().value();
					ignoredFields.add(filedName);
				}
			});
		}
		return ignoredFields;
	}

	/**
	 * Build text uri content.
	 *
	 * @param content the content
	 */
	public void buildTextUriContent(Content content) {
		if (content != null)
			content.computeIfPresent(RestMediaTypes.TEXT_URI_LIST_VALUE,
					(key, value) -> new MediaType().schema(new StringSchema()));
	}

}
