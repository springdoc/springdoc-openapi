package org.springdoc.core;

import java.util.List;
import java.util.Map;

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

	/**
	 * Gets repository rest controller endpoints.
	 *
	 * @return the repository rest controller endpoints
	 */
	Map<String, Object> getRepositoryRestControllerEndpoints();

	/**
	 * Gets handler methods.
	 *
	 * @return the handler methods
	 */
	Map getHandlerMethods();

	/**
	 * Customize.
	 *
	 * @param openAPI the open api
	 */
	void customize(OpenAPI openAPI);

}
