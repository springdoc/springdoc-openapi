package org.springdoc.api;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.GeneralInfoBuilder;
import org.springdoc.core.MethodAttributes;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBodyBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponses;

public abstract class AbstractOpenApiResource {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractOpenApiResource.class);
	protected OpenAPIBuilder openAPIBuilder;
	protected AbstractRequestBuilder requestBuilder;
	protected AbstractResponseBuilder responseBuilder;
	protected OperationBuilder operationParser;
	protected RequestBodyBuilder requestBodyBuilder;
	protected GeneralInfoBuilder generalInfoBuilder;

	protected AbstractOpenApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
			AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestBodyBuilder requestBodyBuilder, GeneralInfoBuilder generalInfoBuilder) {
		super();
		this.openAPIBuilder = openAPIBuilder;
		this.requestBuilder = requestBuilder;
		this.responseBuilder = responseBuilder;
		this.operationParser = operationParser;
		this.requestBodyBuilder = requestBodyBuilder;
		this.generalInfoBuilder = generalInfoBuilder;
	}

	protected OpenAPI getOpenApi() {
		Instant start = Instant.now();
		generalInfoBuilder.build(openAPIBuilder.getOpenAPI());
		Map<String, Object> restControllersMap = generalInfoBuilder.getRestControllersMap();
		Map<String, Object> requestMappingMap = generalInfoBuilder.getRequestMappingMap();
		Map<String, Object> restControllers = Stream.of(restControllersMap, requestMappingMap)
				.flatMap(mapEl -> mapEl.entrySet().stream())
				.filter(controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(),
						Hidden.class) == null))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));

		Map<String, Object> findControllerAdvice = generalInfoBuilder.getControllerAdviceMap();
		// calculate generic responses
		responseBuilder.buildGenericResponse(openAPIBuilder.getComponents(), findControllerAdvice);

		getPaths(restControllers);
		LOGGER.info("Init duration for springdoc-openapi is: {} ms", Duration.between(start, Instant.now()).toMillis());
		return openAPIBuilder.getOpenAPI();
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
			// skip hidden operations
			if (operationParser.isHidden(handlerMethod.getMethod())) {
				continue;
			}

			RequestMapping reqMappringClass = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
					RequestMapping.class);

			MethodAttributes methodAttributes = new MethodAttributes();
			methodAttributes.setMethodOverloaded(existingOperation != null);

			if (reqMappringClass != null) {
				methodAttributes.setClassConsumes(reqMappringClass.consumes());
				methodAttributes.setClassProduces(reqMappringClass.produces());
			}

			methodAttributes.calculateConsumesProduces(handlerMethod.getMethod());

			Operation operation = (existingOperation != null) ? existingOperation : new Operation();

			// compute tags
			operation = generalInfoBuilder.buildTags(handlerMethod, operation, openAPI);

			// Add documentation from operation annotation
			io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils
					.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.Operation.class);
			if (apiOperation != null) {
				openAPI = operationParser.parse(components, apiOperation, operation, openAPI, methodAttributes);
			}

			io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = ReflectionUtils.getAnnotation(
					handlerMethod.getMethod(), io.swagger.v3.oas.annotations.parameters.RequestBody.class);

			// RequestBody in Operation
			requestBodyBuilder
					.buildRequestBodyFromDoc(requestBodyDoc, methodAttributes.getClassConsumes(),
							methodAttributes.getMethodConsumes(), components, null)
					.ifPresent(operation::setRequestBody);

			// requests
			operation = requestBuilder.build(components, handlerMethod, requestMethod, operation, methodAttributes);

			// responses
			ApiResponses apiResponses = responseBuilder.build(components, handlerMethod, operation, methodAttributes);
			operation.setResponses(apiResponses);

			List<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks = ReflectionUtils
					.getRepeatableAnnotations(handlerMethod.getMethod(),
							io.swagger.v3.oas.annotations.callbacks.Callback.class);

			// callbacks
			if (apiCallbacks != null) {
				operationParser.buildCallbacks(apiCallbacks, components, openAPI, methodAttributes)
						.ifPresent(operation::setCallbacks);
			}

			PathItem pathItemObject = buildPathItem(requestMethod, operation, operationPath, paths);
			paths.addPathItem(operationPath, pathItemObject);
		}
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
		} else {
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
}
