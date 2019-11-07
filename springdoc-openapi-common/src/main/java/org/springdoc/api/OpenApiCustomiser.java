package org.springdoc.api;

import io.swagger.v3.oas.models.OpenAPI;

public interface OpenApiCustomiser {
    void customise(OpenAPI openApi);
}
