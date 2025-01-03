/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.providers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.fn.RouterOperation;

/**
 * The interface Repository rest resource provider.
 *
 * @author bnasslahsen
 */
public interface RepositoryRestResourceProvider {

	/**
	 * Gets router operations.
	 *
	 * @param openAPI the open api
	 * @param locale  the locale
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
