package org.springdoc.data.rest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.data.rest.core.DataRestRepository;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
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
	 * Instantiates a new Spring doc data rest utils.
	 *
	 * @param linkRelationProvider the link relation provider
	 */
	public SpringDocDataRestUtils(LinkRelationProvider linkRelationProvider) {
		this.linkRelationProvider = linkRelationProvider;
	}

	/**
	 * Enhance response content.
	 *
	 * @param openAPI the open api
	 * @param resourceMetadata the resource metadata
	 * @param content the content
	 * @param dataRestRepository the data rest repository
	 */
	public void enhanceResponseContent(OpenAPI openAPI, ResourceMetadata resourceMetadata, Content content, DataRestRepository dataRestRepository) {
		Class<?> returnType = dataRestRepository.getReturnType();
		List<String> ignoredFields = getIgnoredFields(resourceMetadata, dataRestRepository, returnType);
		if (!CollectionUtils.isEmpty(ignoredFields)) {
			content.values().forEach(mediaType -> {
				Schema schema = mediaType.getSchema();
				if (schema.get$ref() != null) {
					String key = schema.get$ref().substring(21);
					Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
					Schema existingSchema = schemas.get(key);
					if (!key.endsWith(RESPONSE) && key.toLowerCase().contains(returnType.getSimpleName().toLowerCase())) {
						String newKey = key + RESPONSE;
						Schema newSchema = new ObjectSchema();
						Map<String, Schema> properties = existingSchema.getProperties();
						properties.forEach((propId, val) -> updateResponseSchema(newSchema, returnType, ignoredFields, propId, val));
						if (key.equals(returnType.getSimpleName()))
							schemas.put(newKey, newSchema);
						else
							schemas.put(key, newSchema);
					}
				}
			});
		}
		buildTextUriContent(content);
	}

	/**
	 * Enhance request body content.
	 *
	 * @param openAPI the open api
	 * @param resourceMetadata the resource metadata
	 * @param content the content
	 * @param dataRestRepository the data rest repository
	 */
	public void enhanceRequestBodyContent(OpenAPI openAPI, ResourceMetadata resourceMetadata, Content content, DataRestRepository dataRestRepository) {
		Class<?> returnType = dataRestRepository.getReturnType();
		List<String> associationsFields = getAssociationsFields(resourceMetadata, dataRestRepository, returnType);
		content.values().forEach(mediaType -> {
			Schema schema = mediaType.getSchema();
			if (schema.get$ref() != null) {
				String key = schema.get$ref().substring(21);
				Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
				Schema existingSchema = schemas.get(key);
				if (!key.endsWith(REQUEST_BODY) && returnType.getSimpleName().equals(key)) {
					String newKey = key + REQUEST_BODY;
					Schema newSchema = new ObjectSchema();
					Map<String, Schema> properties = existingSchema.getProperties();
					properties.forEach((propId, val) -> updateRequestBodySchema(associationsFields, newSchema, propId, val));
					schemas.put(newKey, newSchema);
					schema.set$ref(Components.COMPONENTS_SCHEMAS_REF + newKey);
				}
			}
		});
		buildTextUriContent(content);
	}

	/**
	 * Gets ignored fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the data rest repository
	 * @param returnType the return type
	 * @return the ignored fields
	 */
	private List<String> getIgnoredFields(ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository, Class<?> returnType) {
		List<String> ignoredFields = new ArrayList<>();
		if (returnType != null) {
			PersistentEntity<?, ?> entity = dataRestRepository.getPersistentEntity();
			if (entity != null && entity.getIdProperty() != null) {
				String idField = entity.getIdProperty().getName();
				ignoredFields.add(idField);
			}
			entity.doWithAssociations((SimpleAssociationHandler) association -> {
				PersistentProperty<?> property = association.getInverse();
				String filedName = resourceMetadata.getMappingFor(property).getRel().value();
				ignoredFields.add(filedName);
			});
		}
		return ignoredFields;
	}

	/**
	 * Gets associations fields.
	 *
	 * @param resourceMetadata the resource metadata
	 * @param dataRestRepository the data rest repository
	 * @param returnType the return type
	 * @return the associations fields
	 */
	private List<String> getAssociationsFields(ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository, Class<?> returnType) {
		List<String> associationsFields = new ArrayList<>();
		if (returnType != null) {
			PersistentEntity<?, ?> entity = dataRestRepository.getPersistentEntity();
			entity.doWithAssociations((SimpleAssociationHandler) association -> {
				PersistentProperty<?> property = association.getInverse();
				String filedName = resourceMetadata.getMappingFor(property).getRel().value();
				associationsFields.add(filedName);
			});
		}
		return associationsFields;
	}

	/**
	 * Build text uri content.
	 *
	 * @param content the content
	 */
	private void buildTextUriContent(Content content) {
		if (content.get(RestMediaTypes.TEXT_URI_LIST_VALUE) != null)
			content.put(RestMediaTypes.TEXT_URI_LIST_VALUE, new MediaType().schema(new StringSchema()));
	}


	/**
	 * Update request body schema.
	 *
	 * @param associationsFields the associations fields
	 * @param newSchema the new schema
	 * @param propId the prop id
	 * @param val the val
	 */
	private void updateRequestBodySchema(List<String> associationsFields, Schema newSchema, String propId, Schema val) {
		if (!associationsFields.contains(propId))
			newSchema.addProperties(propId, val);
		else {
			if (val instanceof ArraySchema)
				newSchema.addProperties(propId, new ArraySchema().items(new StringSchema()));
			else
				newSchema.addProperties(propId, new StringSchema());
		}
	}

	/**
	 * Update response schema.
	 *
	 * @param newSchema the new schema
	 * @param returnType the return type
	 * @param ignoredFields the ignored fields
	 * @param propId the prop id
	 * @param val the val
	 */
	private void updateResponseSchema(Schema newSchema, Class<?> returnType, List<String> ignoredFields,  String propId, Schema val) {
		if (EMBEDDED.equals(propId)) {
			String entityClassName = linkRelationProvider.getCollectionResourceRelFor(returnType).value();
			ArraySchema arraySchema = (ArraySchema) ((ObjectSchema) val).getProperties().get(entityClassName);
			if (arraySchema != null) {
				Schema itemsSchema = arraySchema.getItems();
				if (itemsSchema.get$ref().substring(21).equals(returnType.getSimpleName()))
					itemsSchema.set$ref(itemsSchema.get$ref() + RESPONSE);
			}
			newSchema.addProperties(EMBEDDED, val);
		}
		else if (!ignoredFields.contains(propId))
			newSchema.addProperties(propId, val);
	}

}
