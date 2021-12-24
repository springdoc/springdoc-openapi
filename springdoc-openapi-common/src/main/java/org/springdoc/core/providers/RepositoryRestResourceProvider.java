package org.springdoc.core.providers;

import java.util.List;
import java.util.Locale;
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
	 * @param locale the locale
	 * @return the router operations
	 */
	List<RouterOperation> getRouterOperations(OpenAPI openAPI, Locale locale);

	/**
	 * Gets Base PathAwar eController endpoints.
	 *
	 * @return the Base PathAware Controller endpoints
	 */
	Map<String, Object> getBasePathAwareControllerEndpoints();

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
