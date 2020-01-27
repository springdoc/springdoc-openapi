/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.api;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.MethodAttributes;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.GroupConfig;
import org.springdoc.core.customizers.OpenApiCustomiser;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

public abstract class AbstractOpenApiResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOpenApiResource.class);

	final OpenAPIBuilder openAPIBuilder;

	private final AbstractRequestBuilder requestBuilder;

	private final GenericResponseBuilder responseBuilder;

	private final OperationBuilder operationParser;

	private final Optional<List<OpenApiCustomiser>> openApiCustomisers;

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	private final SpringDocConfigProperties springDocConfigProperties;

	private static final List<Class<?>> ADDITIONAL_REST_CONTROLLERS = new ArrayList<>();

	private boolean computeDone;

	private final String groupName;

	protected AbstractOpenApiResource(String groupName, OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties) {
		super();
		this.groupName = Objects.requireNonNull(groupName, "groupName");
		this.openAPIBuilder = openAPIBuilder;
		this.requestBuilder = requestBuilder;
		this.responseBuilder = responseBuilder;
		this.operationParser = operationParser;
		this.openApiCustomisers = openApiCustomisers;
		this.springDocConfigProperties = springDocConfigProperties;
	}

	protected synchronized OpenAPI getOpenApi() {
		OpenAPI openApi;
		if (!computeDone || Boolean.TRUE.equals(springDocConfigProperties.getCache().getDisabled())) {
			Instant start = Instant.now();
			openAPIBuilder.build();
			Map<String, Object> restControllersMap = openAPIBuilder.getRestControllersMap();
			Map<String, Object> requestMappingMap = openAPIBuilder.getRequestMappingMap();
			Map<String, Object> controllerMap = openAPIBuilder.getControllersMap();
			Map<String, Object> restControllers = Stream.of(restControllersMap, requestMappingMap, controllerMap)
					.flatMap(mapEl -> mapEl.entrySet().stream())
					.filter(controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(),
							Hidden.class) == null))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));

			Map<String, Object> findControllerAdvice = openAPIBuilder.getControllerAdviceMap();
			// calculate generic responses
			responseBuilder.buildGenericResponse(openAPIBuilder.getComponents(), findControllerAdvice);

			getPaths(restControllers);
			openApi = openAPIBuilder.getOpenAPI();

			// run the optional customisers
			openApiCustomisers.ifPresent(apiCustomisers -> apiCustomisers.forEach(openApiCustomiser -> openApiCustomiser.customise(openApi)));
			LOGGER.info("Init duration for springdoc-openapi is: {} ms",
					Duration.between(start, Instant.now()).toMillis());
			computeDone = true;
		}
		else {
			openApi = openAPIBuilder.getOpenAPI();
		}
		return openApi;
	}

	protected abstract void getPaths(Map<String, Object> findRestControllers);

	protected void calculatePath(OpenAPIBuilder openAPIBuilder, HandlerMethod handlerMethod, String operationPath,
			Set<RequestMethod> requestMethods) {
		OpenAPI openAPI = openAPIBuilder.getOpenAPI();
		Components components = openAPIBuilder.getComponents();
		Paths paths = openAPIBuilder.getPaths();

		Map<HttpMethod, Operation> operationMap = null;
		if (paths.containsKey(operationPath)) {
			PathItem pathItem = paths.get(operationPath);
			operationMap = pathItem.readOperationsMap();
		}

		for (RequestMethod requestMethod : requestMethods) {

			Operation existingOperation = getExistingOperation(operationMap, requestMethod);
			Method method = handlerMethod.getMethod();
			// skip hidden operations
			if (operationParser.isHidden(method)) {
				continue;
			}

			RequestMapping reqMappingClass = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
					RequestMapping.class);

			MethodAttributes methodAttributes = new MethodAttributes();
			methodAttributes.setMethodOverloaded(existingOperation != null);

			if (reqMappingClass != null) {
				methodAttributes.setClassConsumes(reqMappingClass.consumes());
				methodAttributes.setClassProduces(reqMappingClass.produces());
			}

			methodAttributes.calculateConsumesProduces(method);

			Operation operation = (existingOperation != null) ? existingOperation : new Operation();

			if (ReflectionUtils.getAnnotation(method, Deprecated.class) != null) {
				operation.setDeprecated(true);
			}

			// compute tags
			operation = openAPIBuilder.buildTags(handlerMethod, operation, openAPI);

			// Add documentation from operation annotation
			io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method,
					io.swagger.v3.oas.annotations.Operation.class);

			calculateJsonView(apiOperation, methodAttributes, method);

			if (apiOperation != null) {
				openAPI = operationParser.parse(components, apiOperation, operation, openAPI, methodAttributes);
			}

			io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = ReflectionUtils.getAnnotation(method,
					io.swagger.v3.oas.annotations.parameters.RequestBody.class);

			// RequestBody in Operation
			requestBuilder.getRequestBodyBuilder()
					.buildRequestBodyFromDoc(requestBodyDoc, methodAttributes.getClassConsumes(),
							methodAttributes.getMethodConsumes(), components,
							methodAttributes.getJsonViewAnnotationForRequestBody())
					.ifPresent(operation::setRequestBody);

			// requests
			operation = requestBuilder.build(components, handlerMethod, requestMethod, operation, methodAttributes);

			// responses
			ApiResponses apiResponses = responseBuilder.build(components, handlerMethod, operation, methodAttributes);
			operation.setResponses(apiResponses);

			List<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks = ReflectionUtils
					.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.callbacks.Callback.class);

			// callbacks
			if (!CollectionUtils.isEmpty(apiCallbacks)) {
				operationParser.buildCallbacks(apiCallbacks, components, openAPI, methodAttributes)
						.ifPresent(operation::setCallbacks);
			}

			PathItem pathItemObject = buildPathItem(requestMethod, operation, operationPath, paths);
			paths.addPathItem(operationPath, pathItemObject);
		}
	}

	private void calculateJsonView(io.swagger.v3.oas.annotations.Operation apiOperation,
			MethodAttributes methodAttributes, Method method) {
		JsonView jsonViewAnnotation;
		JsonView jsonViewAnnotationForRequestBody;
		if (apiOperation != null && apiOperation.ignoreJsonView()) {
			jsonViewAnnotation = null;
			jsonViewAnnotationForRequestBody = null;
		}
		else {
			jsonViewAnnotation = ReflectionUtils.getAnnotation(method, JsonView.class);
			/*
			 * If one and only one exists, use the @JsonView annotation from the method
			 * parameter annotated with @RequestBody. Otherwise fall back to the @JsonView
			 * annotation for the method itself.
			 */
			jsonViewAnnotationForRequestBody = (JsonView) Arrays.stream(ReflectionUtils.getParameterAnnotations(method))
					.filter(arr -> Arrays.stream(arr)
							.anyMatch(annotation -> (annotation.annotationType()
									.equals(io.swagger.v3.oas.annotations.parameters.RequestBody.class) || annotation.annotationType().equals(RequestBody.class))))
					.flatMap(Arrays::stream).filter(annotation -> annotation.annotationType().equals(JsonView.class))
					.reduce((a, b) -> null).orElse(jsonViewAnnotation);
		}
		methodAttributes.setJsonViewAnnotation(jsonViewAnnotation);
		methodAttributes.setJsonViewAnnotationForRequestBody(jsonViewAnnotationForRequestBody);
	}

	private Operation getExistingOperation(Map<HttpMethod, Operation> operationMap, RequestMethod requestMethod) {
		Operation existingOperation = null;
		if (!CollectionUtils.isEmpty(operationMap)) {
			// Get existing operation definition
			switch (requestMethod) {
				case GET:
					existingOperation = operationMap.get(HttpMethod.GET);
					break;
				case POST:
					existingOperation = operationMap.get(HttpMethod.POST);
					break;
				case PUT:
					existingOperation = operationMap.get(HttpMethod.PUT);
					break;
				case DELETE:
					existingOperation = operationMap.get(HttpMethod.DELETE);
					break;
				case PATCH:
					existingOperation = operationMap.get(HttpMethod.PATCH);
					break;
				case HEAD:
					existingOperation = operationMap.get(HttpMethod.HEAD);
					break;
				case OPTIONS:
					existingOperation = operationMap.get(HttpMethod.OPTIONS);
					break;
				default:
					// Do nothing here
					break;
			}
		}
		return existingOperation;
	}

	private PathItem buildPathItem(RequestMethod requestMethod, Operation operation, String operationPath,
			Paths paths) {
		PathItem pathItemObject;
		if (paths.containsKey(operationPath)) {
			pathItemObject = paths.get(operationPath);
		}
		else {
			pathItemObject = new PathItem();
		}
		switch (requestMethod) {
			case POST:
				pathItemObject.post(operation);
				break;
			case GET:
				pathItemObject.get(operation);
				break;
			case DELETE:
				pathItemObject.delete(operation);
				break;
			case PUT:
				pathItemObject.put(operation);
				break;
			case PATCH:
				pathItemObject.patch(operation);
				break;
			case TRACE:
				pathItemObject.trace(operation);
				break;
			case HEAD:
				pathItemObject.head(operation);
				break;
			case OPTIONS:
				pathItemObject.options(operation);
				break;
			default:
				// Do nothing here
				break;
		}
		return pathItemObject;
	}

	protected boolean isPackageToScan(String aPackage) {
		List<String> packagesToScan = springDocConfigProperties.getPackagesToScan();
		if (CollectionUtils.isEmpty(packagesToScan)) {
			Optional<GroupConfig> optionalGroupConfig = springDocConfigProperties.getGroupConfigs().stream().filter(groupConfig -> this.groupName.equals(groupConfig.getGroup())).findAny();
			if (optionalGroupConfig.isPresent())
				packagesToScan = optionalGroupConfig.get().getPackagesToScan();
		}
		return CollectionUtils.isEmpty(packagesToScan) || packagesToScan.stream().anyMatch(pack -> aPackage.equals(pack) || aPackage.startsWith(pack + "."));
	}

	protected boolean isPathToMatch(String operationPath) {
		List<String> pathsToMatch = springDocConfigProperties.getPathsToMatch();
		if (CollectionUtils.isEmpty(pathsToMatch)) {
			Optional<GroupConfig> optionalGroupConfig = springDocConfigProperties.getGroupConfigs().stream().filter(groupConfig -> this.groupName.equals(groupConfig.getGroup())).findAny();
			if (optionalGroupConfig.isPresent())
				pathsToMatch = optionalGroupConfig.get().getPathsToMatch();
		}
		return CollectionUtils.isEmpty(pathsToMatch) || pathsToMatch.stream().anyMatch(pattern -> antPathMatcher.match(pattern, operationPath));
	}

	protected String decode(String requestURI) {
		try {
			return URLDecoder.decode(requestURI, StandardCharsets.UTF_8.toString());
		}
		catch (UnsupportedEncodingException e) {
			return requestURI;
		}
	}

	protected boolean isAdditionalRestController(Class<?> rawClass) {
		return ADDITIONAL_REST_CONTROLLERS.stream().anyMatch(clazz -> clazz.isAssignableFrom(rawClass));
	}

	public static void addRestControllers(Class<?>... classes){
		ADDITIONAL_REST_CONTROLLERS.addAll(Arrays.asList(classes));
	}

	protected Set getDefaultAllowedHttpMethods(){
		RequestMethod[] allowedRequestMethods = {RequestMethod.GET,  RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD} ;
		return new HashSet<>(Arrays.asList(allowedRequestMethods));
	}
}
