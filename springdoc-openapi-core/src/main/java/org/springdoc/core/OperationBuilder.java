package org.springdoc.core;

import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Component
public class OperationBuilder {

	public OpenAPI parse(Components components, io.swagger.v3.oas.annotations.Operation apiOperation,
			Operation operation,
			OpenAPI openAPI, MediaAttributes mediaAttributes) {
		if (apiOperation != null) {
			if (StringUtils.isNotBlank(apiOperation.summary())) {
				operation.setSummary(apiOperation.summary());
			}
			if (StringUtils.isNotBlank(apiOperation.description())) {
				operation.setDescription(apiOperation.description());
			}
			if (StringUtils.isNotBlank(apiOperation.operationId())) {
				operation.setOperationId(getOperationId(apiOperation.operationId(), openAPI));
			}
			if (apiOperation.deprecated()) {
				operation.setDeprecated(apiOperation.deprecated());
			}

			Optional<List<String>> mlist = getStringListFromStringArray(apiOperation.tags());
			if (mlist.isPresent()) {
				List<String> tags = mlist.get().stream()
						.filter(t -> operation.getTags() == null
								|| (operation.getTags() != null && !operation.getTags().contains(t)))
						.collect(Collectors.toList());
				for (String tagsItem : tags) {
					operation.addTagsItem(tagsItem);
				}
			}

			if (operation.getExternalDocs() == null) { // if not set in root annotation
				AnnotationsUtils.getExternalDocumentation(apiOperation.externalDocs())
						.ifPresent(operation::setExternalDocs);
			}

			getApiResponses(apiOperation.responses(), mediaAttributes.getClassProduces(),
					mediaAttributes.getMethodProduces(), components, null)
					.ifPresent(responses -> {
						if (operation.getResponses() == null) {
							operation.setResponses(responses);
						} else {
							responses.forEach(operation.getResponses()::addApiResponse);
						}
					});
			AnnotationsUtils.getServers(apiOperation.servers())
					.ifPresent(servers -> servers.forEach(operation::addServersItem));

			// security
			Optional<List<SecurityRequirement>> requirementsObject = SecurityParser
					.getSecurityRequirements(apiOperation.security());
			if (requirementsObject.isPresent()) {
				requirementsObject.get().stream()
						.filter(r -> operation.getSecurity() == null || !operation.getSecurity().contains(r))
						.forEach(operation::addSecurityItem);
			}

			// RequestBody in Operation
			if (apiOperation != null && apiOperation.requestBody() != null && operation.getRequestBody() == null) {

				getRequestBody(apiOperation.requestBody(), mediaAttributes.getClassConsumes(),
						mediaAttributes.getMethodConsumes(), components, null)
						.ifPresent(operation::setRequestBody);
			}

			// Extensions in Operation
			if (apiOperation.extensions().length > 0) {
				Map<String, Object> extensions = AnnotationsUtils.getExtensions(apiOperation.extensions());
				if (extensions != null) {
					for (Map.Entry<String, Object> entry : extensions.entrySet()) {
						operation.addExtension(entry.getKey(), entry.getValue());
					}
				}
			}

		}
		return openAPI;
	}

	private static String getOperationId(String operationId, OpenAPI openAPI) {
		boolean operationIdUsed = existOperationId(operationId, openAPI);
		String operationIdToFind = null;
		int counter = 0;
		while (operationIdUsed) {
			operationIdToFind = String.format("%s_%d", operationId, ++counter);
			operationIdUsed = existOperationId(operationIdToFind, openAPI);
		}
		if (operationIdToFind != null) {
			operationId = operationIdToFind;
		}
		return operationId;
	}

	private static boolean existOperationId(String operationId, OpenAPI openAPI) {
		if (openAPI == null) {
			return false;
		}
		if (openAPI.getPaths() == null || openAPI.getPaths().isEmpty()) {
			return false;
		}
		for (PathItem path : openAPI.getPaths().values()) {
			Set<String> pathOperationIds = extractOperationIdFromPathItem(path);
			if (pathOperationIds.contains(operationId)) {
				return true;
			}
		}
		return false;
	}

	private static Set<String> extractOperationIdFromPathItem(PathItem path) {
		Set<String> ids = new HashSet<>();
		if (path.getGet() != null && StringUtils.isNotBlank(path.getGet().getOperationId())) {
			ids.add(path.getGet().getOperationId());
		}
		if (path.getPost() != null && StringUtils.isNotBlank(path.getPost().getOperationId())) {
			ids.add(path.getPost().getOperationId());
		}
		if (path.getPut() != null && StringUtils.isNotBlank(path.getPut().getOperationId())) {
			ids.add(path.getPut().getOperationId());
		}
		if (path.getDelete() != null && StringUtils.isNotBlank(path.getDelete().getOperationId())) {
			ids.add(path.getDelete().getOperationId());
		}
		if (path.getOptions() != null && StringUtils.isNotBlank(path.getOptions().getOperationId())) {
			ids.add(path.getOptions().getOperationId());
		}
		if (path.getHead() != null && StringUtils.isNotBlank(path.getHead().getOperationId())) {
			ids.add(path.getHead().getOperationId());
		}
		if (path.getPatch() != null && StringUtils.isNotBlank(path.getPatch().getOperationId())) {
			ids.add(path.getPatch().getOperationId());
		}
		return ids;
	}

	private static Optional<ApiResponses> getApiResponses(
			final io.swagger.v3.oas.annotations.responses.ApiResponse[] responses, String[] classProduces,
			String[] methodProduces, Components components, JsonView jsonViewAnnotation) {
		if (responses == null) {
			return Optional.empty();
		}
		ApiResponses apiResponsesObject = new ApiResponses();
		for (io.swagger.v3.oas.annotations.responses.ApiResponse response : responses) {
			ApiResponse apiResponseObject = new ApiResponse();
			if (StringUtils.isNotBlank(response.ref())) {
				apiResponseObject.set$ref(response.ref());
				if (StringUtils.isNotBlank(response.responseCode())) {
					apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
				} else {
					apiResponsesObject._default(apiResponseObject);
				}
				continue;
			}
			if (StringUtils.isNotBlank(response.description())) {
				apiResponseObject.setDescription(response.description());
			} else {
				apiResponseObject.setDescription(DEFAULT_DESCRIPTION);
			}
			if (response.extensions().length > 0) {
				Map<String, Object> extensions = AnnotationsUtils.getExtensions(response.extensions());
				if (extensions != null) {
					for (Map.Entry<String, Object> entry : extensions.entrySet()) {
						apiResponseObject.addExtension(entry.getKey(), entry.getValue());
					}
				}
			}

			AnnotationsUtils.getContent(response.content(), classProduces == null ? new String[0] : classProduces,
					methodProduces == null ? new String[0] : methodProduces, null, components, jsonViewAnnotation)
					.ifPresent(apiResponseObject::content);
			AnnotationsUtils.getHeaders(response.headers(), jsonViewAnnotation).ifPresent(apiResponseObject::headers);
			// Make schema as string if empty
			Map<String, Header> headers = apiResponseObject.getHeaders();
			if (!CollectionUtils.isEmpty(headers)) {
				for (Map.Entry<String, Header> entry : headers.entrySet()) {
					Header header = entry.getValue();
					if (header.getSchema() == null) {
						Schema<?> schema = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
						header.setSchema(schema);
						entry.setValue(header);
					}
				}
			}
			if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null
					|| apiResponseObject.getHeaders() != null) {

				Map<String, Link> links = AnnotationsUtils.getLinks(response.links());
				if (links.size() > 0) {
					apiResponseObject.setLinks(links);
				}
				if (StringUtils.isNotBlank(response.responseCode())) {
					apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
				} else {
					apiResponsesObject._default(apiResponseObject);
				}
			}
		}

		if (apiResponsesObject.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(apiResponsesObject);
	}



	private static Optional<List<String>> getStringListFromStringArray(String[] array) {
		if (array == null) {
			return Optional.empty();
		}
		List<String> list = new ArrayList<>();
		boolean isEmpty = true;
		for (String value : array) {
			if (StringUtils.isNotBlank(value)) {
				isEmpty = false;
			}
			list.add(value);
		}
		if (isEmpty) {
			return Optional.empty();
		}
		return Optional.of(list);
	}

	private static Optional<RequestBody> getRequestBody(
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, String[] classConsumes,
			String[] methodConsumes, Components components, JsonView jsonViewAnnotation) {
		if (requestBody == null) {
			return Optional.empty();
		}
		RequestBody requestBodyObject = new RequestBody();
		boolean isEmpty = true;

		if (StringUtils.isNotBlank(requestBody.ref())) {
			requestBodyObject.set$ref(requestBody.ref());
			return Optional.of(requestBodyObject);
		}

		if (StringUtils.isNotBlank(requestBody.description())) {
			requestBodyObject.setDescription(requestBody.description());
			isEmpty = false;
		} else {
			requestBodyObject.setDescription(DEFAULT_DESCRIPTION);
		}
		if (requestBody.required()) {
			requestBodyObject.setRequired(requestBody.required());
			isEmpty = false;
		}
		if (requestBody.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(requestBody.extensions());
			if (extensions != null) {
				for (Map.Entry<String, Object> entry : extensions.entrySet()) {
					requestBodyObject.addExtension(entry.getKey(), entry.getValue());
				}
			}
			isEmpty = false;
		}

		if (requestBody.content().length > 0) {
			isEmpty = false;
		}

		if (isEmpty) {
			return Optional.empty();
		}
		AnnotationsUtils
				.getContent(requestBody.content(), classConsumes == null ? new String[0] : classConsumes,
						methodConsumes == null ? new String[0] : methodConsumes, null, components, jsonViewAnnotation)
				.ifPresent(requestBodyObject::setContent);
		return Optional.of(requestBodyObject);
	}
}
