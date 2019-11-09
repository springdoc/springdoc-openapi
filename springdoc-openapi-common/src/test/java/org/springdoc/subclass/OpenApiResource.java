package org.springdoc.subclass;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.api.OpenApiCustomiser;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;

import java.util.List;
import java.util.Map;

/**
 * Class which sub class AbstractOpenApiResource in a different package and makes sure base class access is not changed.
 */
public class OpenApiResource extends AbstractOpenApiResource {

    public OpenApiResource(OpenAPIBuilder openAPIBuilder,
                           AbstractRequestBuilder requestBuilder,
                           AbstractResponseBuilder responseBuilder,
                           OperationBuilder operationParser,
                           List<OpenApiCustomiser> openApiCustomisers) {
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
