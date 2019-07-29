package org.springdoc.api;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.InfoBuilder;
import org.springdoc.core.MediaAttributes;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBuilder;
import org.springdoc.core.ResponseBuilder;
import org.springdoc.core.TagsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponses;
import reactor.core.publisher.Mono;

@RestController
public class OpenApiResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiResource.class);

	@Autowired
	private OpenAPIBuilder openAPIBuilder;

	@Autowired
	private RequestBuilder requestBuilder;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private TagsBuilder tagbuiBuilder;

	@Autowired
	private OperationBuilder operationParser;

	@Autowired
	private InfoBuilder infoBuilder;

	@Autowired
	private RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> openapiJson() throws JsonProcessingException {
		OpenAPI openAPI = this.getOpenApi();
		return Mono.just(Json.mapper().writeValueAsString(openAPI));
	}

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	public Mono<String> openapiYaml() throws JsonProcessingException {
		OpenAPI openAPI = this.getOpenApi();
		return Mono.just(Yaml.mapper().writeValueAsString(openAPI));
	}

	private OpenAPI getOpenApi() {
		long start = System.currentTimeMillis();
		// Info block
		infoBuilder.build(openAPIBuilder.getOpenAPI());

		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		Map<String, Object> findRestControllers1 = requestMappingHandlerMapping.getApplicationContext()
				.getBeansWithAnnotation(RestController.class);
		Map<String, Object> findRestControllers2 = requestMappingHandlerMapping.getApplicationContext()
				.getBeansWithAnnotation(RequestMapping.class);

		Map<String, Object> findRestControllers = Stream.of(findRestControllers1, findRestControllers2)
				.flatMap(mapEl -> mapEl.entrySet().stream())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));

		Map<String, Object> findControllerAdvice = requestMappingHandlerMapping.getApplicationContext()
				.getBeansWithAnnotation(ControllerAdvice.class);

		// calculate generic responses
		responseBuilder.buildGenericResponse(openAPIBuilder.getComponents(), findControllerAdvice);

		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
			Set<PathPattern> patterns = patternsRequestCondition.getPatterns();

			for (PathPattern pathPattern : patterns) {
				String operationPath = pathPattern.getPatternString();
				if (operationPath != null && operationPath.startsWith(DEFAULT_PATH_SEPARATOR)
						&& findRestControllers.containsKey(handlerMethod.getBean().toString())) {
					Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
					calculatePath(openAPIBuilder, handlerMethod, operationPath, requestMethods);
				}
			}
		}
		openAPIBuilder.getOpenAPI().setPaths(openAPIBuilder.getPaths());
		LOGGER.info("Init duration for springdoc-openapi is: {} ms", (System.currentTimeMillis() - start));
		return openAPIBuilder.getOpenAPI();
	}

	private void calculatePath(OpenAPIBuilder openAPIBuilder, HandlerMethod handlerMethod, String operationPath,
			Set<RequestMethod> requestMethods) {
		OpenAPI openAPI = openAPIBuilder.getOpenAPI();
		Components components = openAPIBuilder.getComponents();
		Paths paths = openAPIBuilder.getPaths();

		for (RequestMethod requestMethod : requestMethods) {
			// skip hidden operations
			io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils
					.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.Operation.class);
			if (apiOperation != null && apiOperation.hidden()) {
				continue;
			}

			RequestMapping reqMappringClass = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
					RequestMapping.class);

			MediaAttributes mediaAttributes = new MediaAttributes();
			if (reqMappringClass != null) {
				mediaAttributes.setClassConsumes(reqMappringClass.consumes());
				mediaAttributes.setClassProduces(reqMappringClass.produces());
			}

			mediaAttributes.calculateConsumesProduces(requestMethod, handlerMethod.getMethod());

			Operation operation = new Operation();

			// compute tags
			operation = tagbuiBuilder.build(handlerMethod, operation, openAPI);

			// Add documentation from operation annotation
			if (apiOperation != null) {
				openAPI = operationParser.parse(components, apiOperation, operation, openAPI, mediaAttributes);
			}

			// requests
			operation = requestBuilder.build(components, handlerMethod, requestMethod, operation,
					mediaAttributes.getAllConsumes());

			// responses
			ApiResponses apiResponses = responseBuilder.build(components, handlerMethod, operation,
					mediaAttributes.getAllProduces());

			operation.setResponses(apiResponses);

			PathItem pathItemObject = buildPathItem(requestMethod, operation, operationPath, paths);
			paths.addPathItem(operationPath, pathItemObject);
		}
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
