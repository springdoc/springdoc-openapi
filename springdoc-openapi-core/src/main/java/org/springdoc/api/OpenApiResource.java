package org.springdoc.api;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;

@RestController
public class OpenApiResource extends AbstractOpenApiResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiResource.class);

	@Autowired
	private RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl)
			throws JsonProcessingException {
		calculateServerUrl(request, apiDocsUrl);
		OpenAPI openAPI = this.getOpenApi();
		return Json.mapper().writeValueAsString(openAPI);
	}

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	public String openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl)
			throws JsonProcessingException {
		calculateServerUrl(request, apiDocsUrl);
		OpenAPI openAPI = this.getOpenApi();
		return Yaml.mapper().writeValueAsString(openAPI);
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
			Set<String> patterns = patternsRequestCondition.getPatterns();
			String operationPath = patterns.iterator().next();

			if (operationPath != null && operationPath.startsWith(DEFAULT_PATH_SEPARATOR)
					&& findRestControllers.containsKey(handlerMethod.getBean().toString())) {
				Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
				calculatePath(openAPIBuilder, handlerMethod, operationPath, requestMethods);
			}
		}
		openAPIBuilder.getOpenAPI().setPaths(openAPIBuilder.getPaths());
		LOGGER.info("Init duration for springdoc-openapi is: {} ms", (System.currentTimeMillis() - start));
		return openAPIBuilder.getOpenAPI();
	}

	private void calculateServerUrl(HttpServletRequest request, String apiDocsUrl) {
		StringBuffer requestUrl = request.getRequestURL();
		String serverBaseUrl = requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
		infoBuilder.setServerBaseUrl(serverBaseUrl);
	}
}
