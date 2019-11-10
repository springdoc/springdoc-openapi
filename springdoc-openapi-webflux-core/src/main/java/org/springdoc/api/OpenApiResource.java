package org.springdoc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PathUtils;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@RestController
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class OpenApiResource extends AbstractOpenApiResource {

    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

    public OpenApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
                           AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                           RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
                           Optional<List<OpenApiCustomiser>> openApiCustomisers) {
        super(openAPIBuilder, requestBuilder, responseBuilder, operationParser, openApiCustomisers);
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> openapiJson(ServerHttpRequest serverHttpRequest, @Value(API_DOCS_URL) String apiDocsUrl)
            throws JsonProcessingException {
        calculateServerUrl(serverHttpRequest, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return Mono.just(Json.mapper().writeValueAsString(openAPI));
    }

    @Operation(hidden = true)
    @GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
    public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest,
                                    @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl) throws JsonProcessingException {
        calculateServerUrl(serverHttpRequest, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return Mono.just(Yaml.mapper().writeValueAsString(openAPI));
    }

    @Override
    protected void getPaths(Map<String, Object> restControllers) {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
            Set<PathPattern> patterns = patternsRequestCondition.getPatterns();
            for (PathPattern pathPattern : patterns) {
                String operationPath = pathPattern.getPatternString();
                Map<String, String> regexMap = new LinkedHashMap<>();
                operationPath = PathUtils.parsePath(operationPath, regexMap);
                if (operationPath.startsWith(DEFAULT_PATH_SEPARATOR)
                        && restControllers.containsKey(handlerMethod.getBean().toString())) {
                    Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
                    calculatePath(openAPIBuilder, handlerMethod, operationPath, requestMethods);
                }
            }
        }
    }

    private void calculateServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl) {
        String requestUrl = serverHttpRequest.getURI().toString();
        String serverBaseUrl = requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
        openAPIBuilder.setServerBaseUrl(serverBaseUrl);
    }
}
