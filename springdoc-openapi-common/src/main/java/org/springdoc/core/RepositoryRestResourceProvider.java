package org.springdoc.core;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.fn.RouterOperation;

/**
 * The interface Repository rest resource provider.
 * @author bnasslahsen
 */
public interface RepositoryRestResourceProvider {

	/**
	 * Gets router operations.
	 *
	 * @param openAPI the open api
	 * @return the router operations
	 */
	List<RouterOperation> getRouterOperations(OpenAPI openAPI);
}
