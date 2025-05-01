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

package org.springdoc.core.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.utils.Constants.DELETE_METHOD;
import static org.springdoc.core.utils.Constants.GET_METHOD;
import static org.springdoc.core.utils.Constants.HEAD_METHOD;
import static org.springdoc.core.utils.Constants.OPTIONS_METHOD;
import static org.springdoc.core.utils.Constants.PATCH_METHOD;
import static org.springdoc.core.utils.Constants.POST_METHOD;
import static org.springdoc.core.utils.Constants.PUT_METHOD;
import static org.springdoc.core.utils.Constants.TRACE_METHOD;

/**
 * The type Operation builder.
 *
 * @author bnasslahsen
 */
public class OperationService {

	/**
	 * The Parameter builder.
	 */
	private final GenericParameterService parameterBuilder;

	/**
	 * The Request body builder.
	 */
	private final RequestBodyService requestBodyService;

	/**
	 * The Security parser.
	 */
	private final SecurityService securityParser;

	/**
	 * The Property resolver utils.
	 */
	private final PropertyResolverUtils propertyResolverUtils;

	/**
	 * Instantiates a new Operation builder.
	 *
	 * @param parameterBuilder      the parameter builder
	 * @param requestBodyService    the request body builder
	 * @param securityParser        the security parser
	 * @param propertyResolverUtils the property resolver utils
	 */
	public OperationService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			SecurityService securityParser, PropertyResolverUtils propertyResolverUtils) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyService = requestBodyService;
		this.securityParser = securityParser;
		this.propertyResolverUtils = propertyResolverUtils;
	}

	/**
	 * Parse open api.
	 *
	 * @param apiOperation     the api operation
	 * @param operation        the operation
	 * @param openAPI          the open api
	 * @param methodAttributes the method attributes
	 * @return the open api
	 */
	public OpenAPI parse(io.swagger.v3.oas.annotations.Operation apiOperation,
			Operation operation, OpenAPI openAPI, MethodAttributes methodAttributes) {
		Components components = openAPI.getComponents();
		Locale locale = methodAttributes.getLocale();
		if (StringUtils.isNotBlank(apiOperation.summary()))
			operation.setSummary(propertyResolverUtils.resolve(apiOperation.summary(), locale));

		if (StringUtils.isNotBlank(apiOperation.description()))
			operation.setDescription(propertyResolverUtils.resolve(apiOperation.description(), locale));

		if (StringUtils.isNotBlank(apiOperation.operationId()))
			operation.setOperationId(apiOperation.operationId());

		if (apiOperation.deprecated())
			operation.setDeprecated(apiOperation.deprecated());

		buildTags(apiOperation, operation);

		if (operation.getExternalDocs() == null)  // if not set in root annotation
			AnnotationsUtils.getExternalDocumentation(apiOperation.externalDocs(), propertyResolverUtils.isOpenapi31())
					.ifPresent(operation::setExternalDocs);

		// servers
		AnnotationsUtils.getServers(apiOperation.servers())
				.ifPresent(servers -> servers.forEach(operation::addServersItem));

		// build parameters
		for (io.swagger.v3.oas.annotations.Parameter parameterDoc : apiOperation.parameters()) {
			Parameter parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, components,
					methodAttributes.getJsonViewAnnotation(), locale);
			operation.addParametersItem(parameter);
		}

		// RequestBody in Operation
		requestBodyService.buildRequestBodyFromDoc(apiOperation.requestBody(), operation.getRequestBody(), methodAttributes, components, locale)
				.ifPresent(operation::setRequestBody);

		// build response
		buildResponse(components, apiOperation, operation, methodAttributes);

		// security
		securityParser.buildSecurityRequirement(apiOperation.security(), operation);

		// Extensions in Operation
		buildExtensions(apiOperation, operation, locale);
		return openAPI;
	}

	/**
	 * Is hidden boolean.
	 *
	 * @param method the method
	 * @return the boolean
	 */
	public boolean isHidden(Method method) {
		io.swagger.v3.oas.annotations.Operation apiOperation = AnnotationUtils.findAnnotation(method,
				io.swagger.v3.oas.annotations.Operation.class);
		return (apiOperation != null && (apiOperation.hidden()))
				|| (AnnotationUtils.findAnnotation(method, Hidden.class) != null);
	}

	/**
	 * Build callbacks optional.
	 *
	 * @param apiCallbacks     the api callbacks
	 * @param openAPI          the open api
	 * @param methodAttributes the method attributes
	 * @return the optional
	 */
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

	/**
	 * Sets path item operation.
	 *
	 * @param pathItemObject the path item object
	 * @param method         the method
	 * @param operation      the operation
	 */
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

	/**
	 * Build extensions.
	 *
	 * @param apiOperation the api operation
	 * @param operation    the operation
	 * @param locale       the locale
	 */
	private void buildExtensions(io.swagger.v3.oas.annotations.Operation apiOperation, Operation operation, Locale locale) {
		if (apiOperation.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(propertyResolverUtils.isOpenapi31(), apiOperation.extensions());
			if (propertyResolverUtils.isResolveExtensionsProperties()) {
				Map<String, Object> extensionsResolved = propertyResolverUtils.resolveExtensions(locale, extensions);
				extensionsResolved.forEach(operation::addExtension);
			}
			else {
				extensions.forEach(operation::addExtension);
			}
		}
	}

	/**
	 * Build tags.
	 *
	 * @param apiOperation the api operation
	 * @param operation    the operation
	 */
	private void buildTags(io.swagger.v3.oas.annotations.Operation apiOperation, Operation operation) {
		Optional<List<String>> mlist = getStringListFromStringArray(apiOperation.tags());
		if (mlist.isPresent()) {
			List<String> tags = mlist.get().stream()
					.filter(t -> operation.getTags() == null
							|| (operation.getTags() != null && !operation.getTags().contains(t)))
					.toList();
			tags.forEach(operation::addTagsItem);
		}
	}

	/**
	 * Gets api responses.
	 *
	 * @param responses        the responses
	 * @param methodAttributes the method attributes
	 * @param operation        the operation
	 * @param components       the components
	 * @return the api responses
	 */
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
			setDescription(response, apiResponseObject, methodAttributes.getJavadocReturn());
			setExtensions(response, apiResponseObject, methodAttributes.getLocale());

			buildResponseContent(methodAttributes, components, classProduces, methodProduces, apiResponsesOp, response, apiResponseObject);

			SpringDocAnnotationsUtils.getHeaders(response.headers(), components, null, propertyResolverUtils.isOpenapi31()).ifPresent(apiResponseObject::headers);
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

	/**
	 * Build response content.
	 *
	 * @param methodAttributes  the method attributes
	 * @param components        the components
	 * @param classProduces     the class produces
	 * @param methodProduces    the method produces
	 * @param apiResponsesOp    the api responses op
	 * @param response          the response
	 * @param apiResponseObject the api response object
	 */
	private void buildResponseContent(MethodAttributes methodAttributes, Components components, String[] classProduces, String[] methodProduces, ApiResponses apiResponsesOp, io.swagger.v3.oas.annotations.responses.ApiResponse response, ApiResponse apiResponseObject) {
		if (apiResponsesOp == null)
			SpringDocAnnotationsUtils.getContent(response.content(),
							classProduces == null ? new String[0] : classProduces,
							methodProduces == null ? new String[0] : methodProduces, null, components, null, propertyResolverUtils.isOpenapi31())
					.ifPresent(apiResponseObject::content);
		else
			GenericResponseService.buildContentFromDoc(components, apiResponsesOp, methodAttributes, response, apiResponseObject, propertyResolverUtils.isOpenapi31());
	}

	/**
	 * Is response object boolean.
	 *
	 * @param apiResponseObject the api response object
	 * @return the boolean
	 */
	private boolean isResponseObject(ApiResponse apiResponseObject) {
		return StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null
				|| apiResponseObject.getHeaders() != null;
	}

	/**
	 * Sets links.
	 *
	 * @param response          the response
	 * @param apiResponseObject the api response object
	 */
	private void setLinks(io.swagger.v3.oas.annotations.responses.ApiResponse response, ApiResponse apiResponseObject) {
		Map<String, Link> links = AnnotationsUtils.getLinks(response.links(), propertyResolverUtils.isOpenapi31());
		if (links.size() > 0) {
			apiResponseObject.setLinks(links);
		}
	}

	/**
	 * Sets description.
	 *
	 * @param response          the response
	 * @param apiResponseObject the api response object
	 * @param javadocReturn     the javadoc return
	 */
	private void setDescription(io.swagger.v3.oas.annotations.responses.ApiResponse response,
			ApiResponse apiResponseObject, String javadocReturn) {
		if (StringUtils.isNotBlank(response.description())) {
			apiResponseObject.setDescription(response.description());
		}
		else if (StringUtils.isNotBlank(javadocReturn)) {
			apiResponseObject.setDescription(javadocReturn);
		}
		else {
			GenericResponseService.setDescription(response.responseCode(), apiResponseObject);
		}
	}

	/**
	 * Calculate header.
	 *
	 * @param apiResponseObject the api response object
	 */
	private void calculateHeader(ApiResponse apiResponseObject) {
		Map<String, Header> headers = apiResponseObject.getHeaders();
		if (!CollectionUtils.isEmpty(headers)) {
			for (Map.Entry<String, Header> entry : headers.entrySet()) {
				Header header = entry.getValue();
				if (header.getSchema() == null) {
					Schema<?> schema = AnnotationsUtils.resolveSchemaFromType(String.class, null, null, propertyResolverUtils.isOpenapi31());
					header.setSchema(schema);
					entry.setValue(header);
				}
			}
		}
	}

	/**
	 * Sets ref.
	 *
	 * @param apiResponsesObject the api responses object
	 * @param response           the response
	 * @param apiResponseObject  the api response object
	 */
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

	/**
	 * Sets extensions.
	 *
	 * @param response          the response
	 * @param apiResponseObject the api response object
	 * @param locale            the locale
	 */
	private void setExtensions(io.swagger.v3.oas.annotations.responses.ApiResponse response,
			ApiResponse apiResponseObject, Locale locale) {
		if (response.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(propertyResolverUtils.isOpenapi31(), response.extensions());
			if (propertyResolverUtils.isResolveExtensionsProperties()) {
				Map<String, Object> extensionsResolved = propertyResolverUtils.resolveExtensions(locale, extensions);
				extensionsResolved.forEach(apiResponseObject::addExtension);
			}
			else {
				extensions.forEach(apiResponseObject::addExtension);
			}
		}
	}


	/**
	 * Build response.
	 *
	 * @param components       the components
	 * @param apiOperation     the api operation
	 * @param operation        the operation
	 * @param methodAttributes the method attributes
	 */
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

	/**
	 * Gets string list from string array.
	 *
	 * @param array the array
	 * @return the string list from string array
	 */
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

	/**
	 * Merge operation.
	 *
	 * @param operation      the operation
	 * @param operationModel the operation model
	 * @return the operation
	 */
	public Operation mergeOperation(Operation operation, Operation operationModel) {
		if (operationModel.getOperationId().length() < operation.getOperationId().length()) {
			operation.setOperationId(operationModel.getOperationId());
		}

		ApiResponses apiResponses = operation.getResponses();
		for (Entry<String, ApiResponse> apiResponseEntry : operationModel.getResponses().entrySet()) {
			if (apiResponses.containsKey(apiResponseEntry.getKey())) {
				Content existingContent = apiResponses.get(apiResponseEntry.getKey()).getContent();
				Content newContent = apiResponseEntry.getValue().getContent();
				if (newContent != null)
					newContent.forEach((mediaTypeStr, mediaType) -> SpringDocAnnotationsUtils.mergeSchema(existingContent, mediaType.getSchema(), mediaTypeStr));
			}
			else
				apiResponses.addApiResponse(apiResponseEntry.getKey(), apiResponseEntry.getValue());
		}
		return operation;
	}

	/**
	 * Gets javadoc provider.
	 *
	 * @return the javadoc provider
	 */
	public JavadocProvider getJavadocProvider() {
		return parameterBuilder.getJavadocProvider();
	}

	/**
	 * Gets propertyResolverUtils
	 *
	 * @return propertyResolverUtils property resolver utils
	 */
	public PropertyResolverUtils getPropertyResolverUtils(){
		return parameterBuilder.getPropertyResolverUtils();
	}
}
