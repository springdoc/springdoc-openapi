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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.DEFAULT_DESCRIPTION;
import static org.springdoc.core.Constants.DELETE_METHOD;
import static org.springdoc.core.Constants.GET_METHOD;
import static org.springdoc.core.Constants.HEAD_METHOD;
import static org.springdoc.core.Constants.OPTIONS_METHOD;
import static org.springdoc.core.Constants.PATCH_METHOD;
import static org.springdoc.core.Constants.POST_METHOD;
import static org.springdoc.core.Constants.PUT_METHOD;
import static org.springdoc.core.Constants.TRACE_METHOD;

public class OperationBuilder {

	private final GenericParameterBuilder parameterBuilder;

	private final RequestBodyBuilder requestBodyBuilder;

	private final SecurityParser securityParser;

	private final PropertyResolverUtils propertyResolverUtils;

	public OperationBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			SecurityParser securityParser, PropertyResolverUtils propertyResolverUtils) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyBuilder = requestBodyBuilder;
		this.securityParser = securityParser;
		this.propertyResolverUtils = propertyResolverUtils;
	}

	public OpenAPI parse(io.swagger.v3.oas.annotations.Operation apiOperation,
			Operation operation, OpenAPI openAPI, MethodAttributes methodAttributes) {
		Components components = openAPI.getComponents();
		if (StringUtils.isNotBlank(apiOperation.summary()))
			operation.setSummary(propertyResolverUtils.resolve(apiOperation.summary()));

		if (StringUtils.isNotBlank(apiOperation.description()))
			operation.setDescription(propertyResolverUtils.resolve(apiOperation.description()));

		if (StringUtils.isNotBlank(apiOperation.operationId()))
			operation.setOperationId(getOperationId(apiOperation.operationId(), openAPI));

		if (apiOperation.deprecated())
			operation.setDeprecated(apiOperation.deprecated());

		buildTags(apiOperation, operation);

		if (operation.getExternalDocs() == null)  // if not set in root annotation
			AnnotationsUtils.getExternalDocumentation(apiOperation.externalDocs())
					.ifPresent(operation::setExternalDocs);

		// servers
		AnnotationsUtils.getServers(apiOperation.servers())
				.ifPresent(servers -> servers.forEach(operation::addServersItem));

		// build parameters
		for (io.swagger.v3.oas.annotations.Parameter parameterDoc : apiOperation.parameters()) {
			Parameter parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, components,
					methodAttributes.getJsonViewAnnotation());
			operation.addParametersItem(parameter);
		}

		// RequestBody in Operation
		requestBodyBuilder.buildRequestBodyFromDoc(apiOperation.requestBody(), methodAttributes.getClassConsumes(),
				methodAttributes.getMethodConsumes(), components, null).ifPresent(operation::setRequestBody);

		// build response
		buildResponse(components, apiOperation, operation, methodAttributes);

		// security
		securityParser.buildSecurityRequirement(apiOperation.security(), operation);

		// Extensions in Operation
		buildExtensions(apiOperation, operation);
		return openAPI;
	}

	public boolean isHidden(Method method) {
		io.swagger.v3.oas.annotations.Operation apiOperation = AnnotationUtils.findAnnotation(method,
				io.swagger.v3.oas.annotations.Operation.class);
		return (apiOperation != null && (apiOperation.hidden()))
				|| (AnnotationUtils.findAnnotation(method, Hidden.class) != null);
	}

	public Optional<Map<String, Callback>> buildCallbacks(
			Set<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks, OpenAPI openAPI,
			MethodAttributes methodAttributes) {
		Map<String, Callback> callbacks = new LinkedHashMap<>();

		boolean doBreak = false;
		for (io.swagger.v3.oas.annotations.callbacks.Callback methodCallback : apiCallbacks) {
			Map<String, Callback> callbackMap = new HashMap<>();
			if (methodCallback == null) {
				callbacks.putAll(callbackMap);
				doBreak = true;
			}
			Callback callbackObject = new Callback();
			if (!doBreak && StringUtils.isNotBlank(methodCallback.ref())) {
				callbackObject.set$ref(methodCallback.ref());
				callbackMap.put(methodCallback.name(), callbackObject);
				callbacks.putAll(callbackMap);
				doBreak = true;
			}

			if (doBreak)
				break;

			PathItem pathItemObject = new PathItem();
			for (io.swagger.v3.oas.annotations.Operation callbackOperation : methodCallback.operation()) {
				Operation callbackNewOperation = new Operation();
				parse(callbackOperation, callbackNewOperation, openAPI, methodAttributes);
				setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
			}
			callbackObject.addPathItem(methodCallback.callbackUrlExpression(), pathItemObject);
			callbackMap.put(methodCallback.name(), callbackObject);
			callbacks.putAll(callbackMap);
		}

		if (CollectionUtils.isEmpty(callbacks))
			return Optional.empty();
		else
			return Optional.of(callbacks);
	}

	private void setPathItemOperation(PathItem pathItemObject, String method, Operation operation) {
		switch (method) {
			case POST_METHOD:
				pathItemObject.post(operation);
				break;
			case GET_METHOD:
				pathItemObject.get(operation);
				break;
			case DELETE_METHOD:
				pathItemObject.delete(operation);
				break;
			case PUT_METHOD:
				pathItemObject.put(operation);
				break;
			case PATCH_METHOD:
				pathItemObject.patch(operation);
				break;
			case TRACE_METHOD:
				pathItemObject.trace(operation);
				break;
			case HEAD_METHOD:
				pathItemObject.head(operation);
				break;
			case OPTIONS_METHOD:
				pathItemObject.options(operation);
				break;
			default:
				// Do nothing here
				break;
		}
	}

	private void buildExtensions(io.swagger.v3.oas.annotations.Operation apiOperation, Operation operation) {
		if (apiOperation.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(apiOperation.extensions());
			extensions.forEach(operation::addExtension);
		}
	}

	private void buildTags(io.swagger.v3.oas.annotations.Operation apiOperation, Operation operation) {
		Optional<List<String>> mlist = getStringListFromStringArray(apiOperation.tags());
		if (mlist.isPresent()) {
			List<String> tags = mlist.get().stream()
					.filter(t -> operation.getTags() == null
							|| (operation.getTags() != null && !operation.getTags().contains(t)))
					.collect(Collectors.toList());
			tags.forEach(operation::addTagsItem);
		}
	}

	private String getOperationId(String operationId, OpenAPI openAPI) {
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

	private boolean existOperationId(String operationId, OpenAPI openAPI) {
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

	private Set<String> extractOperationIdFromPathItem(PathItem path) {
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

	private Optional<ApiResponses> getApiResponses(
			final io.swagger.v3.oas.annotations.responses.ApiResponse[] responses,
			MethodAttributes methodAttributes, Operation operation, Components components) {

		ApiResponses apiResponsesObject = new ApiResponses();
		String[] classProduces = methodAttributes.getClassProduces();
		String[] methodProduces = methodAttributes.getMethodProduces();

		ApiResponses apiResponsesOp = operation.getResponses();

		for (io.swagger.v3.oas.annotations.responses.ApiResponse response : responses) {
			ApiResponse apiResponseObject = new ApiResponse();
			if (StringUtils.isNotBlank(response.ref())) {
				setRef(apiResponsesObject, response, apiResponseObject);
				continue;
			}
			setDescription(response, apiResponseObject);
			setExtensions(response, apiResponseObject);

			if (apiResponsesOp == null)
				SpringDocAnnotationsUtils.getContent(response.content(),
						classProduces == null ? new String[0] : classProduces,
						methodProduces == null ? new String[0] : methodProduces, null, components, null)
						.ifPresent(apiResponseObject::content);
			else
				GenericResponseBuilder.buildContentFromDoc(components, apiResponsesOp, methodAttributes, response, apiResponseObject);

			AnnotationsUtils.getHeaders(response.headers(), null).ifPresent(apiResponseObject::headers);
			// Make schema as string if empty
			calculateHeader(apiResponseObject);
			if (isResponseObject(apiResponseObject)) {
				setLinks(response, apiResponseObject);
				if (StringUtils.isNotBlank(response.responseCode())) {
					apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
				}
				else {
					apiResponsesObject._default(apiResponseObject);
				}
			}
		}

		return Optional.of(apiResponsesObject);
	}

	private boolean isResponseObject(ApiResponse apiResponseObject) {
		return StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null
				|| apiResponseObject.getHeaders() != null;
	}

	private void setLinks(io.swagger.v3.oas.annotations.responses.ApiResponse response, ApiResponse apiResponseObject) {
		Map<String, Link> links = AnnotationsUtils.getLinks(response.links());
		if (links.size() > 0) {
			apiResponseObject.setLinks(links);
		}
	}

	private void setDescription(io.swagger.v3.oas.annotations.responses.ApiResponse response,
			ApiResponse apiResponseObject) {
		if (StringUtils.isNotBlank(response.description())) {
			apiResponseObject.setDescription(response.description());
		}
		else {
			apiResponseObject.setDescription(DEFAULT_DESCRIPTION);
		}
	}

	private void calculateHeader(ApiResponse apiResponseObject) {
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
	}

	private void setRef(ApiResponses apiResponsesObject, io.swagger.v3.oas.annotations.responses.ApiResponse response,
			ApiResponse apiResponseObject) {
		apiResponseObject.set$ref(response.ref());
		if (StringUtils.isNotBlank(response.responseCode())) {
			apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
		}
		else {
			apiResponsesObject._default(apiResponseObject);
		}
	}

	private void setExtensions(io.swagger.v3.oas.annotations.responses.ApiResponse response,
			ApiResponse apiResponseObject) {
		if (response.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(response.extensions());
			extensions.forEach(apiResponseObject::addExtension);
		}
	}


	private void buildResponse(Components components, io.swagger.v3.oas.annotations.Operation apiOperation,
			Operation operation, MethodAttributes methodAttributes) {
		getApiResponses(apiOperation.responses(), methodAttributes, operation, components).ifPresent(responses -> {
			if (operation.getResponses() == null) {
				operation.setResponses(responses);
			}
			else {
				responses.forEach(operation.getResponses()::addApiResponse);
			}
		});
	}

	private Optional<List<String>> getStringListFromStringArray(String[] array) {
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

	public String getOperationId(String operationId, String oldOperationId, OpenAPI openAPI) {
		if (StringUtils.isNotBlank(oldOperationId))
			return this.getOperationId(oldOperationId, openAPI);
		else
			return this.getOperationId(operationId, openAPI);
	}

}
