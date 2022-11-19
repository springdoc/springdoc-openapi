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

package org.springdoc.core.customizers;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.annotation.AbstractDiscoveredOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameter;
import org.springframework.boot.actuate.endpoint.invoke.reflect.OperationMethod;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.providers.ActuatorProvider.getTag;

/**
 * The type Actuator operation customizer.
 * @author bnasslahsen
 */
public class ActuatorOperationCustomizer implements GlobalOperationCustomizer {

	/**
	 * The constant OPERATION.
	 */
	private static final String OPERATION = "operation";

	/**
	 * The constant PARAMETER.
	 */
	private static final String PARAMETER = "parameter";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorOperationCustomizer.class);

	/**
	 * The regex pattern for operationId lookup.
	 */
	private static final Pattern pattern = Pattern.compile(".*'([^']*)'.*");

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getTags() != null && operation.getTags().contains(getTag().getName())) {
			Field operationFiled = FieldUtils.getDeclaredField(handlerMethod.getBean().getClass(), OPERATION, true);
			Object actuatorOperation;
			if (operationFiled != null) {
				try {
					actuatorOperation = operationFiled.get(handlerMethod.getBean());
					Field actuatorOperationFiled = FieldUtils.getDeclaredField(actuatorOperation.getClass(), OPERATION, true);
					if (actuatorOperationFiled != null) {
						AbstractDiscoveredOperation discoveredOperation = (AbstractDiscoveredOperation) actuatorOperationFiled.get(actuatorOperation);
						OperationMethod operationMethod = discoveredOperation.getOperationMethod();
						if (OperationType.WRITE.equals(operationMethod.getOperationType())) {
							for (OperationParameter operationParameter : operationMethod.getParameters()) {
								Field parameterField = FieldUtils.getDeclaredField(operationParameter.getClass(), PARAMETER, true);
								Parameter parameter = (Parameter) parameterField.get(operationParameter);
								Schema<?> schema = AnnotationsUtils.resolveSchemaFromType(parameter.getType(), null, null);
								if (parameter.getAnnotation(Selector.class) == null) {
									operation.setRequestBody(new RequestBody()
											.content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType().schema(schema))));
								}
							}
						}
					}
				}
				catch (IllegalAccessException e) {
					LOGGER.warn(e.getMessage());
				}
			}

			String summary = handlerMethod.toString();
			Matcher matcher = pattern.matcher(summary);
			String operationId = operation.getOperationId();
			while (matcher.find()) {
				operationId = matcher.group(1);
			}

			if (operation.getSummary() == null && !summary.contains("$"))
				operation.setSummary(summary);

			operation.setOperationId(operationId);
		}
		return operation;
	}
}
