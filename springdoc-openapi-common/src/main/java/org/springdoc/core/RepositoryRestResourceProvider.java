package org.springdoc.core;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.fn.RouterOperation;

public interface RepositoryRestResourceProvider {

	List<RouterOperation> getRouterOperations(OpenAPI openAPI);
}
