package org.springdoc.api;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import reactor.core.publisher.Mono;

@RestController
public class OpenApiResource extends AbstractOpenApiResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiResource.class);

	@Autowired
	private RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

	@Autowired
	private RouterFunctionMapping routerFunctionMapping;

	@Autowired
	private ApplicationContext applicationContext;

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

		RouterFunction<?> routerFunction = routerFunctionMapping.getRouterFunction();

		SortedRouterFunctionsContainer container = new SortedRouterFunctionsContainer();
		applicationContext.getAutowireCapableBeanFactory().autowireBean(container);

		List<RouterFunction<?>> routerFunctions = CollectionUtils.isEmpty(container.routerFunctions)
				? Collections.emptyList()
				: container.routerFunctions;

		if (!CollectionUtils.isEmpty(routerFunctions)) {
			for (RouterFunction router : routerFunctions) {

				if (router instanceof org.springframework.web.reactive.function.server.RouterFunctions) {
					LOGGER.info("done");
				}
				LOGGER.info("Mapped badou" + router.getClass());

			}
		}

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

	private static class SortedRouterFunctionsContainer {

		@Nullable
		private List<RouterFunction<?>> routerFunctions;

		@Autowired(required = false)
		public void setRouterFunctions(List<RouterFunction<?>> routerFunctions) {
			this.routerFunctions = routerFunctions;
		}
	}
}
