package org.springdoc.api;

import io.swagger.v3.oas.models.OpenAPI;

public interface OpenApiCustomiser {
  public void customise(OpenAPI openApi);
}
