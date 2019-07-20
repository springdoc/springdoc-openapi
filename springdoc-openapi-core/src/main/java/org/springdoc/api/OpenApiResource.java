package org.springdoc.api;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.Constants;
import org.springdoc.core.InfoBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBuilder;
import org.springdoc.core.ResponseBuilder;
import org.springdoc.core.TagsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponses;

@RestController
public class OpenApiResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiResource.class);

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
	private RequestMappingInfoHandlerMapping mappingHandler;


	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public String openapiJson() throws Exception {
		OpenAPI openAPI = this.getOpenApi();
		return Json.mapper().writeValueAsString(openAPI);
	}

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	public String openapiYaml() throws Exception {
		OpenAPI openAPI = this.getOpenApi();
		return Yaml.mapper().writeValueAsString(openAPI);
	}

	private OpenAPI getOpenApi() throws Exception {
		long start = System.currentTimeMillis();
		OpenAPI openAPI = new OpenAPI();
		Components components = new Components();
		openAPI.setComponents(components);
		// Info block
		infoBuilder.build(openAPI);

		Map<RequestMappingInfo, HandlerMethod> map = mappingHandler.getHandlerMethods();
		Map<String, Object> findRestControllers1 = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(RestController.class);
		Map<String, Object> findRestControllers2 = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(RequestMapping.class);

		Map<String, Object> findRestControllers = Stream.of(findRestControllers1, findRestControllers2)
				.flatMap(mapEl -> mapEl.entrySet().stream())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));

		Map<String, Object> findControllerAdvice = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(ControllerAdvice.class);

		// calculate generic responses
		responseBuilder.buildGenericResponse(components, findControllerAdvice);

		Paths paths = new Paths();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
			Set<String> patterns = patternsRequestCondition.getPatterns();
			String operationPath = null;
			if (patterns != null) {
				Optional<String> firstpattern = patterns.stream().findFirst();
				if (firstpattern.isPresent())
					operationPath = firstpattern.get();
			}

			if (operationPath != null && operationPath.startsWith(Constants.SLASH)
					&& findRestControllers.containsKey(handlerMethod.getBean().toString())) {
				Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
				for (RequestMethod requestMethod : requestMethods) {

					// skip hidden operations
					io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils
							.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.Operation.class);
					if (apiOperation != null && apiOperation.hidden()) {
						continue;
					}

					RequestMapping reqMappringClass = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
							RequestMapping.class);

					String[] classProduces = null;
					String[] classConsumes = null;
					if (reqMappringClass != null) {
						classProduces = reqMappringClass.produces();
						classConsumes = reqMappringClass.consumes();
					}

					String[] methodProduces = null;
					String[] methodConsumes = null;

					this.calculateConsumesProduces(methodProduces, methodConsumes, requestMethod, handlerMethod);

					String[] allConsumes = ArrayUtils.addAll(methodConsumes, classConsumes);
					String[] allProduces = ArrayUtils.addAll(methodProduces, classProduces);

					Operation operation = new Operation();

					// compute tags
					operation = tagbuiBuilder.build(handlerMethod, operation, openAPI);

					// Add documentation from operation annotation
					operationParser.parse(components, apiOperation, operation, openAPI, classConsumes, methodConsumes,
							classProduces, methodProduces);

					// requests
					operation = requestBuilder.build(components, handlerMethod, requestMethod, operation, allConsumes);

					// responses
					ApiResponses apiResponses = responseBuilder.build(components, handlerMethod, operation,
							allProduces);

					operation.setResponses(apiResponses);

					PathItem pathItemObject = buildPathItem(requestMethod, operation, operationPath, paths);
					paths.addPathItem(operationPath, pathItemObject);
					if (openAPI.getPaths() != null) {
						paths.putAll(openAPI.getPaths());
					}
					openAPI.setPaths(paths);
				}
			}
		}
		LOGGER.info("Init duration for springdoc-openapi is: " + (System.currentTimeMillis() - start) + " ms");
		return openAPI;
	}

	private void calculateConsumesProduces(String[] methodProduces, String[] methodConsumes,
			RequestMethod requestMethod, HandlerMethod handlerMethod) {
		switch (requestMethod) {
		case POST:
			PostMapping reqPostMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
					PostMapping.class);
			if (reqPostMappringMethod != null) {
				methodProduces = reqPostMappringMethod.produces();
				methodConsumes = reqPostMappringMethod.consumes();
			}
			break;
		case GET:
			GetMapping reqGetMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
					GetMapping.class);
			if (reqGetMappringMethod != null) {
				methodProduces = reqGetMappringMethod.produces();
				methodConsumes = reqGetMappringMethod.consumes();
			}
			break;
		case DELETE:
			DeleteMapping reqDeleteMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
					DeleteMapping.class);
			if (reqDeleteMappringMethod != null) {
				methodProduces = reqDeleteMappringMethod.produces();
				methodConsumes = reqDeleteMappringMethod.consumes();
			}
			break;
		case PUT:
			PutMapping reqPutMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
					PutMapping.class);
			if (reqPutMappringMethod != null) {
				methodProduces = reqPutMappringMethod.produces();
				methodConsumes = reqPutMappringMethod.consumes();
			}
			break;
		default:
			RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
					RequestMapping.class);
			if (reqMappringMethod != null) {
				methodProduces = reqMappringMethod.produces();
				methodConsumes = reqMappringMethod.consumes();
			}
			break;
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
