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
import org.springdoc.core.ActuatorProvider;

import org.springframework.web.method.HandlerMethod;

/**
 * The type Actuator operation customizer.
 * @author bnasslahsen
 */
public class ActuatorOperationCustomizer implements OperationCustomizer {

	/**
	 * The Actuator provider.
	 */
	private final ActuatorProvider actuatorProvider;

	/**
	 * The Method count.
	 */
	private int methodCount;

	/**
	 * Instantiates a new Actuator operation customizer.
	 *
	 * @param actuatorProvider the actuator provider
	 */
	public ActuatorOperationCustomizer(ActuatorProvider actuatorProvider) {
		this.actuatorProvider = actuatorProvider;
	}

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getTags() != null && operation.getTags().contains(actuatorProvider.getTag().getName())) {
			operation.setSummary(handlerMethod.toString());
			operation.setOperationId(operation.getOperationId() + "_" + methodCount++);
		}
		return operation;
	}
}
