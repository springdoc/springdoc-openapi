package io.springdoc.core;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
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

@RestController("/openapi")
public class OpenApiResource {

	private static Logger LOGGER = LoggerFactory.getLogger(OpenApiResource.class);

	@Autowired
	private RequestBuilder requestBuilder;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private TagsBuilder tagbuiBuilder;

	@Autowired
	private OperationParser operationParser;

	private OpenAPI openAPI;

	@Autowired
	private RequestMappingInfoHandlerMapping mappingHandler;

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = "/openapi.yaml", produces = "application/vnd.oai.openapi")
	@ResponseBody
	public String openapiYaml() throws Exception {
		this.getOpenApi();
		return Yaml.mapper().writeValueAsString(openAPI);
	}

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = "/openapi.json", produces = "application/json")
	@ResponseBody
	public String openapiJson() throws Exception {
		this.getOpenApi();
		return Json.mapper().writeValueAsString(openAPI);
	}

	public void getOpenApi() throws Exception {
		// TODO GET URI INFO from http servlet request
		long start = System.currentTimeMillis();
		openAPI = new OpenAPI();
		Components components = new Components();
		openAPI.setComponents(components);
		// Info block
		openAPI.setInfo(InfoBuilder.build());


		Map<RequestMappingInfo, HandlerMethod> map = mappingHandler.getHandlerMethods();
		Map<String, Object> findRestControllers1 = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(RestController.class);
		Map<String, Object> findRestControllers2 = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(RequestMapping.class);

		Map<String, Object> findRestControllers = Stream.of(findRestControllers1, findRestControllers2)
				.flatMap(mapEl -> mapEl.entrySet().stream())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<String, Object> findControllerAdvice = mappingHandler.getApplicationContext()
				.getBeansWithAnnotation(ControllerAdvice.class);

		responseBuilder.build(components, findControllerAdvice);

		Paths paths = new Paths();
		PathItem pathItemObject;
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			String operationPath = requestMappingInfo.getPatternsCondition().toString();
			if (operationPath != null && operationPath.contains("/")
					&& findRestControllers.containsKey(handlerMethod.getBean().toString())) {
				operationPath = requestMappingInfo.getPatternsCondition().getPatterns().stream().findFirst().get();
				Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
				for (RequestMethod requestMethod : requestMethods) {
					if (paths.containsKey(operationPath)) {
						pathItemObject = paths.get(operationPath);
					} else {
						pathItemObject = new PathItem();
					}

					RequestMapping reqMappringClass = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
							RequestMapping.class);

					String[] classProduces = null;
					String[] classConsumes = null;
					if (reqMappringClass != null) {
						classProduces = reqMappringClass.produces();
						classConsumes = reqMappringClass.consumes();
					}

					RequestMapping reqMappringMethod = ReflectionUtils.getAnnotation(handlerMethod.getMethod(),
							RequestMapping.class);

					String[] methodProduces = null;
					String[] methodConsumes = null;
					if (reqMappringMethod != null) {
						methodProduces = reqMappringMethod.produces();
						methodConsumes = reqMappringMethod.consumes();
					}

					// skip hidden operations
					io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils
							.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.Operation.class);
					if (apiOperation != null && apiOperation.hidden()) {
						continue;
					}

					Operation operation = new Operation();

					// compute tags
					operation = tagbuiBuilder.build(handlerMethod, operation);

					// requests
					operation = requestBuilder.build(components, handlerMethod, requestMethod, requestMappingInfo,
							operation, classConsumes, methodConsumes);

					// responses
					ApiResponses apiResponses = responseBuilder.build(components, requestMappingInfo, handlerMethod,
							operation, classProduces, methodProduces);

					operation.setResponses(apiResponses);
					// Add documentation from operation annotation
					operationParser.parse(components, handlerMethod, apiOperation, operation,
							openAPI, classConsumes, methodConsumes, classProduces, methodProduces);

					setPathItemOperation(pathItemObject, requestMethod, operation);
					paths.addPathItem(operationPath, pathItemObject);
					if (openAPI.getPaths() != null) {
						paths.putAll(openAPI.getPaths());
					}
					openAPI.setPaths(paths);
				}
			}
		}
		LOGGER.info("Init duration for springdoc-openapi is: " + (System.currentTimeMillis() - start) + " ms");
	}

	private void setPathItemOperation(PathItem pathItemObject, RequestMethod requestMethod, Operation operation) {
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
	}
}
