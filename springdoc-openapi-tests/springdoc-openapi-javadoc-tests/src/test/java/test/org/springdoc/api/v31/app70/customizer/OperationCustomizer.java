/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app70.customizer;

import io.swagger.v3.oas.models.Operation;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Operation customizer.
 */
@Component
class OperationCustomizer implements org.springdoc.core.customizers.OperationCustomizer {
	/**
	 * Customize operation.
	 *
	 * @param operation     the operation
	 * @param handlerMethod the handler method
	 * @return the operation
	 */
	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		CustomizedOperation annotation = handlerMethod.getMethodAnnotation(CustomizedOperation.class);
		if (annotation != null) {
			operation.description(operation.getDescription() + ", " + annotation.addition());
		}
		return operation;
	}
}
