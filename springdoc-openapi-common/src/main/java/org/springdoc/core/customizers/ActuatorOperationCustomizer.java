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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.Operation;

import org.springframework.web.method.HandlerMethod;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.springdoc.core.providers.ActuatorProvider.getTag;

/**
 * The type Actuator operation customizer.
 * @author bnasslahsen
 */
public class ActuatorOperationCustomizer implements OperationCustomizer {

	/**
	 * The Method count.
	 */
	private HashMap<String, Integer> methodCountMap = new HashMap<>();


	/**
	 * The regex pattern for operationId lookup.
	 */
	private static final Pattern pattern = Pattern.compile(".*'([^']*)'.*");

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getTags() != null && operation.getTags().contains(getTag().getName())) {
			String summary = handlerMethod.toString();
			Matcher matcher = pattern.matcher(summary);
			String operationId = operation.getOperationId();
			while (matcher.find()) {
				operationId = matcher.group(1);
			}
			if (methodCountMap.containsKey(operationId)) {
				Integer methodCount = methodCountMap.get(operationId)+1;
				methodCountMap.put(operationId, methodCount);
				operationId = operationId + "_" + methodCount;
			}
			else
				methodCountMap.put(operationId, INTEGER_ONE);

			if (!summary.contains("$"))
				operation.setSummary(summary);
			operation.setOperationId(operationId);
		}
		return operation;
	}
}
