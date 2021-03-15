package org.springdoc.data.rest.utils;

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
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.util.CollectionUtils;

/**
 * The class Spring doc data rest utils.
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
	private LinkRelationProvider linkRelationProvider;

	/**
	 * The Entity ino map.
	 */
	private HashMap<String, EntityInfo> entityInoMap = new HashMap();

	/**
	 * Instantiates a new Spring doc data rest utils.
	 *
	 * @param linkRelationProvider the link relation provider
	 */
	public SpringDocDataRestUtils(LinkRelationProvider linkRelationProvider) {
		this.linkRelationProvider = linkRelationProvider;
	}

	/**
	 * Customise.
	 *
	 * @param openAPI the open api
	 * @param mappings the mappings
	 * @param persistentEntities the persistent entities
	 */
	public void customise(OpenAPI openAPI, ResourceMappings mappings, PersistentEntities persistentEntities) {

		persistentEntities.getManagedTypes().stream().forEach(typeInformation ->
				{
					Class domainType = typeInformation.getType();
					ResourceMetadata resourceMetadata = mappings.getMetadataFor(domainType);
					final PersistentEntity<?, ?> entity = persistentEntities.getRequiredPersistentEntity(domainType);
					EntityInfo entityInfo = new EntityInfo();
					entityInfo.setDomainType(domainType);
					List<String> ignoredFields = getIgnoredFields(resourceMetadata, entity);
					entityInfo.setIgnoredFields(ignoredFields);
					List<String> associationsFields = getAssociationsFields(resourceMetadata, entity);
					entityInfo.setAssociationsFields(associationsFields);
					entityInoMap.put(domainType.getSimpleName(), entityInfo);
				}
		);

		openAPI.getPaths().entrySet().stream()
				.forEach(stringPathItemEntry -> {
					PathItem pathItem = stringPathItemEntry.getValue();
					pathItem.readOperations().forEach(operation -> {
						RequestBody requestBody = operation.getRequestBody();
						updateRequestBody(openAPI, requestBody);
						ApiResponses apiResponses = operation.getResponses();
						apiResponses.forEach((code, apiResponse) -> updateApiResponse(openAPI, openAPI.getComponents(), apiResponse));

					});
				});
	}

	/**
	 * Update api response.
	 *
	 * @param openAPI the open api
	 * @param components the components
	 * @param apiResponse the api response
	 */
	private void updateApiResponse(OpenAPI openAPI, Components components, ApiResponse apiResponse) {
		if (apiResponse != null && !CollectionUtils.isEmpty(apiResponse.getContent())) {
			apiResponse.getContent().values().forEach(mediaType -> {
				Schema schema = mediaType.getSchema();
				if (schema.get$ref() != null && !schema.get$ref().endsWith(RESPONSE)) {
					String key = schema.get$ref().substring(21);
					Set<String> entitiesNames = entityInoMap.keySet();
					entitiesNames.forEach(entityName -> {
						if (key.endsWith(entityName))
							updateResponseSchema(entityName, components.getSchemas().get(key), openAPI.getComponents());
					});
				}
			});
		}
	}

	/**
	 * Update request body.
	 *
	 * @param openAPI the open api
	 * @param requestBody the request body
	 */
	private void updateRequestBody(OpenAPI openAPI, RequestBody requestBody) {
		if (requestBody != null && !CollectionUtils.isEmpty(requestBody.getContent())) {
			requestBody.getContent().values().forEach(mediaType -> {
				Schema schema = mediaType.getSchema();
				if (schema.get$ref() != null && !schema.get$ref().endsWith(REQUEST_BODY)) {
					String key = schema.get$ref().substring(21);
					if (entityInoMap.keySet().contains(key))
						updateRequestBodySchema(key, schema, openAPI.getComponents());
				}
				else if (schema instanceof ComposedSchema) {
					updateComposedSchema((ComposedSchema) schema, REQUEST_BODY, openAPI.getComponents());
				}
			});
		}
	}

	/**
	 * Gets request body schema.
	 *
	 * @param className the class name
	 * @param schema the schema
	 * @param components the components
	 * @return the request body schema
	 */
	private void updateRequestBodySchema(String className, Schema schema, Components components) {
		//update ref
		String newKey = className + REQUEST_BODY;
		schema.set$ref(newKey);
		//create new schema
		Class schemaImplementation = entityInoMap.get(className).getDomainType();
		ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(schemaImplementation));
		Map<String, Schema> schemaMap;
		if (resolvedSchema != null) {
			schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach((key, referencedSchema) -> {
					components.addSchemas(key + REQUEST_BODY, referencedSchema);
					referencedSchema.setName(key + REQUEST_BODY);
					Map properties = referencedSchema.getProperties();
					updateRequestBodySchemaProperties(key, referencedSchema, properties);
					if (referencedSchema instanceof ComposedSchema)
						updateComposedSchema((ComposedSchema) referencedSchema, REQUEST_BODY, components);
				});
			}
		}
	}

	private void updateRequestBodySchemaProperties(String key, Schema referencedSchema, Map properties) {
		if (!CollectionUtils.isEmpty(referencedSchema.getProperties())) {
			Iterator<Entry<String, Schema>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Schema> entry = it.next();
				String propId = entry.getKey();
				if (entityInoMap.containsKey(key) && entityInoMap.get(key).getAssociationsFields().contains(propId)) {
					if (entry.getValue() instanceof ArraySchema)
						referencedSchema.addProperties(propId, new ArraySchema().items(new StringSchema()));
					else
						referencedSchema.addProperties(propId, new StringSchema());
				}
			}
		}
	}

	/**
	 * Update response schema schema.
	 *
	 * @param className the class name
	 * @param existingSchema the existing schema
	 * @param components the components
	 * @return the schema
	 */
	private Schema updateResponseSchema(String className, Schema existingSchema, Components components) {
		Map<String, Schema> properties = existingSchema.getProperties();
		EntityInfo entityInfo = entityInoMap.get(className);
		if (!CollectionUtils.isEmpty(properties)) {
			Iterator<Entry<String, Schema>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Schema> entry = it.next();
				String propId = entry.getKey();
				if (entityInfo.getIgnoredFields().contains(propId))
					it.remove();
				else if (EMBEDDED.equals(propId)) {
					updateResponseSchemaEmbedded(components, entityInfo, entry);
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
	 * @param entry the entry
	 */
	private void updateResponseSchemaEmbedded(Components components, EntityInfo entityInfo, Entry<String, Schema> entry) {
		String entityClassName = linkRelationProvider.getCollectionResourceRelFor(entityInfo.getDomainType()).value();
		ArraySchema arraySchema = (ArraySchema) ((ObjectSchema) entry.getValue()).getProperties().get(entityClassName);
		if (arraySchema != null) {
			Schema itemsSchema = arraySchema.getItems();
			Set<String> entitiesNames = entityInoMap.keySet();
			if (itemsSchema.get$ref() != null && !itemsSchema.get$ref().endsWith(RESPONSE)) {
				String key = itemsSchema.get$ref().substring(21);
				if (entitiesNames.contains(key)) {
					String newKey = itemsSchema.get$ref() + RESPONSE;
					createNewResponseSchema(key, components);
					itemsSchema.set$ref(newKey);
				}
			}
			else if (itemsSchema instanceof ComposedSchema) {
				updateComposedSchema((ComposedSchema) itemsSchema, RESPONSE, components);
			}
		}
	}

	/**
	 * Create new response schema schema.
	 *
	 * @param className the class name
	 * @param components the components
	 * @return the schema
	 */
	private Schema createNewResponseSchema(String className, Components components) {
		Class schemaImplementation = entityInoMap.get(className).getDomainType();
		Schema schemaObject = new Schema();
		ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(schemaImplementation));
		Map<String, Schema> schemaMap;
		if (resolvedSchema != null) {
			schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach((key, referencedSchema) ->
						addSchemas(components, key, referencedSchema));
			}
			schemaObject = resolvedSchema.schema;
		}
		return schemaObject;

	}

	/**
	 * Add schemas.
	 *
	 * @param components the components
	 * @param key the key
	 * @param referencedSchema the referenced schema
	 */
	private void addSchemas(Components components, String key, Schema referencedSchema) {
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
		if (referencedSchema instanceof ComposedSchema) {
			updateComposedSchema((ComposedSchema) referencedSchema, RESPONSE, null);
		}
		components.addSchemas(key + RESPONSE, referencedSchema);
	}

	/**
	 * Update composed schema.
	 *
	 * @param referencedSchema the referenced schema
	 * @param suffix the suffix
	 * @param components the components
	 */
	private void updateComposedSchema(ComposedSchema referencedSchema, String suffix, Components components) {
		//Update the allOf
		ComposedSchema composedSchema = referencedSchema;
		List<Schema> allOfSchemas = composedSchema.getAllOf();
		updateKey(allOfSchemas, suffix, components);
		//Update the oneOf
		List<Schema> oneOfSchemas = composedSchema.getOneOf();
		updateKey(oneOfSchemas, suffix, components);
	}

	/**
	 * Update key.
	 *
	 * @param allSchemas the all schemas
	 * @param suffix can be Resposne or RequestBody
	 * @param components the components
	 */
	private void updateKey(List<Schema> allSchemas, String suffix, Components components) {
		if (!CollectionUtils.isEmpty(allSchemas))
			for (Schema allSchema : allSchemas) {
				if (allSchema.get$ref() != null) {
					String allKey = allSchema.get$ref().substring(21);
					updateSingleKey(suffix, components, allSchema, allKey);
				}
			}
	}

	/**
	 * Update single key.
	 *
	 * @param suffix the suffix
	 * @param components the components
	 * @param allSchema the all schema
	 * @param allKey the all key
	 */
	private void updateSingleKey(String suffix, Components components, Schema allSchema, String allKey) {
		if (!allKey.endsWith(REQUEST_BODY) && !allKey.endsWith(RESPONSE)) {
			String newAllKey = allKey + suffix;
			allSchema.set$ref(newAllKey);
			if (components != null && !components.getSchemas().containsKey(newAllKey) && entityInoMap.containsKey(allKey) && RESPONSE.equals(suffix)) {
				createNewResponseSchema(allKey, components);
			}
		}
	}

	/**
	 * Gets associations fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param entity the entity
	 * @return the associations fields
	 */
	private List<String> getAssociationsFields(ResourceMetadata
			resourceMetadata, PersistentEntity<?, ?> entity) {
		List<String> associationsFields = new ArrayList<>();
		entity.doWithAssociations((SimpleAssociationHandler) association -> {
			PersistentProperty<?> property = association.getInverse();
			String filedName = resourceMetadata.getMappingFor(property).getRel().value();
			associationsFields.add(filedName);
		});
		return associationsFields;
	}

	/**
	 * Gets ignored fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param entity the entity
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
				String filedName = resourceMetadata.getMappingFor(property).getRel().value();
				ignoredFields.add(filedName);
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
		content.computeIfPresent(RestMediaTypes.TEXT_URI_LIST_VALUE,
				(key, value) -> new MediaType().schema(new StringSchema()));
	}

}
