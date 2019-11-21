package org.springdoc.core;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.api.OpenApiCustomiser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class which sub class AbstractOpenApiResource in a different package and makes sure base class access is not changed.
 */
public class OpenApiResource extends AbstractOpenApiResource {

    public OpenApiResource(OpenAPIBuilder openAPIBuilder,
                           AbstractRequestBuilder requestBuilder,
                           AbstractResponseBuilder responseBuilder,
                           OperationBuilder operationParser,
                           Optional<List<OpenApiCustomiser>> openApiCustomisers) {
        super(openAPIBuilder, requestBuilder, responseBuilder, operationParser, openApiCustomisers);
    }

    public OpenAPI getComputedApi() {
        return super.getOpenApi();
    }

    @Override
    protected void getPaths(Map<String, Object> findRestControllers) {
        // do stuff
    }
}
