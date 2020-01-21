package org.springdoc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@RestController
public class MultipleOpenApiResource implements InitializingBean {

    private final List<GroupedOpenApi> groupedOpenApis;
    private final ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder;
    private final AbstractRequestBuilder requestBuilder;
    private final AbstractResponseBuilder responseBuilder;
    private final OperationBuilder operationParser;
    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;
    private final Optional<ActuatorProvider> servletContextProvider;
    private Map<String, OpenApiResource> groupedOpenApiResources;
    @Value(SPRINGDOC_CACHE_DISABLED_VALUE)
    private boolean cacheDisabled;

    public MultipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
                                   ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder, AbstractRequestBuilder requestBuilder,
                                   AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
                                   RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> servletContextProvider) {

        this.groupedOpenApis = groupedOpenApis;
        this.defaultOpenAPIBuilder = defaultOpenAPIBuilder;
        this.requestBuilder = requestBuilder;
        this.responseBuilder = responseBuilder;
        this.operationParser = operationParser;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.servletContextProvider = servletContextProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.groupedOpenApiResources = groupedOpenApis.stream()
                .collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
                        new OpenApiResource(
                                defaultOpenAPIBuilder.getObject(),
                                requestBuilder,
                                responseBuilder,
                                operationParser,
                                requestMappingHandlerMapping,
                                servletContextProvider,
                                Optional.of(item.getOpenApiCustomisers()), item.getPathsToMatch(), item.getPackagesToScan(),
                                cacheDisabled
                        )
                ));
    }

    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL + "/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl,
                              @PathVariable String group)
            throws JsonProcessingException {
        return getOpenApiResourceOrThrow(group).openapiJson(request, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
    }

    @Operation(hidden = true)
    @GetMapping(value = DEFAULT_API_DOCS_URL_YAML + "/{group}", produces = APPLICATION_OPENAPI_YAML)
    public String openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl,
                              @PathVariable String group)
            throws JsonProcessingException {
        return getOpenApiResourceOrThrow(group).openapiYaml(request, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
    }


    private OpenApiResource getOpenApiResourceOrThrow(String group) {
        OpenApiResource openApiResource = groupedOpenApiResources.get(group);
        if (openApiResource == null) {
            throw new IllegalStateException("No OpenAPI resource found for group " + group);
        }
        return openApiResource;
    }
}
