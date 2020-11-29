/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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
 *
 */

package org.springdoc.core.customizers;

import io.swagger.v3.oas.models.Operation;

import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.ActuatorProvider.getTag;

/**
 * The type Actuator operation customizer.
 * @author bnasslahsen
 */
public class ActuatorOperationCustomizer implements OperationCustomizer {

	/**
	 * The Method count.
	 */
	private int methodCount;


	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getTags() != null && operation.getTags().contains(getTag().getName())) {
			String summary = handlerMethod.toString();
			if (!summary.contains("$"))
				operation.setSummary(summary);
			operation.setOperationId(operation.getOperationId() + "_" + methodCount++);
		}
		return operation;
	}
}
