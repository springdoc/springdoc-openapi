/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.filters;

import java.lang.reflect.Method;

/**
 * Implement and register a bean of type {@link OpenApiMethodFilter} to
 * conditionally include any detected methods in default OpenAPI descriptions
 * but not groups
 *
 * @author michael.clarke
 * @see  GlobalOpenApiMethodFilter filter methods in default OpenAPI      description and groups
 */
@FunctionalInterface
public interface OpenApiMethodFilter {

	/**
	 * Whether the given method should be included in the generated OpenApi definitions. Only methods from classes
	 * detected by the relevant loader will be passed to this filter; it cannot be used to load methods that are not
	 * annotated with `RequestMethod` or similar mechanisms. Methods that are rejected by this filter will not be
	 * processed any further, although methods accepted by this filter may still be rejected by other checks, such as
	 * package inclusion checks so may still be excluded from the final OpenApi definition.
	 *
	 * @param method the method to perform checks against
	 * @return whether this method should be used for further processing
	 */
	boolean isMethodToInclude(Method method);

}
