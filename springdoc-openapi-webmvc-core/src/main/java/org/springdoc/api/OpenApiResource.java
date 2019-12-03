package org.springdoc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@RestController
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class OpenApiResource extends AbstractOpenApiResource {

    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

    private final Optional<ActuatorProvider> servletContextProvider;

    @Value(SPRINGDOC_SHOW_ACTUATOR_VALUE)
    private boolean showActuator;

    public OpenApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> servletContextProvider,
                           Optional<List<OpenApiCustomiser>> openApiCustomisers) {
        super(openAPIBuilder, requestBuilder, responseBuilder, operationParser, openApiCustomisers);
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.servletContextProvider = servletContextProvider;
    }

    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl)
            throws JsonProcessingException {
        calculateServerUrl(request, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return Json.mapper().writeValueAsString(openAPI);
    }

    @Operation(hidden = true)
    @GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
    public String openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl)
            throws JsonProcessingException {
        calculateServerUrl(request, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return Yaml.mapper().writeValueAsString(openAPI);
    }

    @Override
    protected void getPaths(Map<String, Object> restControllers) {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        calculatePath(restControllers, map);
        if (showActuator && servletContextProvider.isPresent()) {
            map = servletContextProvider.get().getWebMvcHandlerMapping().getHandlerMethods();
            Set<HandlerMethod> handlerMethods = new HashSet<>(map.values());
            this.openAPIBuilder.addTag(handlerMethods, SPRINGDOC_ACTUATOR_TAG);
            calculatePath(restControllers, map);
        }
    }

    private void calculatePath(Map<String, Object> restControllers, Map<RequestMappingInfo, HandlerMethod> map) {
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
            Set<String> patterns = patternsRequestCondition.getPatterns();
            String operationPath = CollectionUtils.isEmpty(patterns) ? "/" : patterns.iterator().next();
            Map<String, String> regexMap = new LinkedHashMap<>();
            operationPath = PathUtils.parsePath(operationPath, regexMap);

            if (isRestController(restControllers, handlerMethod, operationPath) && isPackageToScan(handlerMethod.getBeanType().getPackage().getName()) && isPathToMatch(operationPath)) {
                Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
                calculatePath(openAPIBuilder, handlerMethod, operationPath, requestMethods);
            }
        }
    }

    private boolean isRestController(Map<String, Object> restControllers, HandlerMethod handlerMethod,
                                     String operationPath) {
        boolean result;
        if (showActuator)
            result = operationPath.startsWith(DEFAULT_PATH_SEPARATOR);
        else {
            ResponseBody requestBodyAnnotation = ReflectionUtils.getAnnotation(handlerMethod.getBeanType(),
                    ResponseBody.class);
            if (requestBodyAnnotation == null)
                requestBodyAnnotation = ReflectionUtils.getAnnotation(handlerMethod.getMethod(), ResponseBody.class);
            result = operationPath.startsWith(DEFAULT_PATH_SEPARATOR)
                    && (restControllers.containsKey(handlerMethod.getBean().toString())
                    || (requestBodyAnnotation != null));
        }

        return result;
    }

    private void calculateServerUrl(HttpServletRequest request, String apiDocsUrl) {
        StringBuffer requestUrl = request.getRequestURL();
        String serverBaseUrl = requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
        openAPIBuilder.setServerBaseUrl(serverBaseUrl);
    }
}
