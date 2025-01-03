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

package org.springdoc.core.customizers;

import org.springdoc.core.fn.RouterOperation;

import org.springframework.web.method.HandlerMethod;

/**
 * Implement and register a bean of type {@link RouterOperationCustomizer} to customize an router operation
 * based on the handler method input on default OpenAPI descriptions but not groups
 *
 * @author hyeonisism
 */
@FunctionalInterface
public interface RouterOperationCustomizer {

	/**
	 * Customize router operation.
	 *
	 * @param routerOperation input operation
	 * @param handlerMethod   original handler method
	 * @return customized router operation
	 */
	RouterOperation customize(RouterOperation routerOperation, HandlerMethod handlerMethod);

}
