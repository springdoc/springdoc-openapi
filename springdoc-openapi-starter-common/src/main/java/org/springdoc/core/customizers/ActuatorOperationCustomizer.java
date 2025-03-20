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

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.endpoint.annotation.AbstractDiscoveredOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.invoke.OperationParameter;
import org.springframework.boot.actuate.endpoint.invoke.reflect.OperationMethod;
import org.springframework.boot.actuate.endpoint.web.WebServerNamespace;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.providers.ActuatorProvider.getTag;
import static org.springdoc.core.utils.SpringDocUtils.handleSchemaTypes;

/**
 * The type Actuator operation customizer.
 *
 * @author bnasslahsen
 */
public class ActuatorOperationCustomizer implements GlobalOperationComponentsCustomizer {

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
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;


	/**
	 * Instantiates a new Actuator operation customizer.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public ActuatorOperationCustomizer(SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
	}

	@Override
	public Operation customize(Operation operation, Components components, HandlerMethod handlerMethod) {
		if (operationHasValidTag(operation)) {
			Field operationField = FieldUtils.getDeclaredField(handlerMethod.getBean().getClass(), OPERATION,true);
			if (operationField != null) {
				processOperationField(handlerMethod, operation, components, operationField);
			}
			setOperationSummary(operation, handlerMethod);
		}
		return operation;
	}

	/**
	 * Operation has valid tag boolean.
	 *
	 * @param operation the operation
	 * @return the boolean
	 */
	private boolean operationHasValidTag(Operation operation) {
		return operation.getTags() != null && operation.getTags().contains(getTag().getName());
	}

	/**
	 * Process operation field.
	 *
	 * @param handlerMethod  the handler method
	 * @param operation      the operation
	 * @param components     the components
	 * @param operationField the operation field
	 */
	private void processOperationField(HandlerMethod handlerMethod, Operation operation, Components components, Field operationField) {
		try {
			Object actuatorOperation = operationField.get(handlerMethod.getBean());
			Field actuatorOperationField = FieldUtils.getDeclaredField(actuatorOperation.getClass(), OPERATION, true);
			if (actuatorOperationField != null) {
				AbstractDiscoveredOperation discoveredOperation =
						(AbstractDiscoveredOperation) actuatorOperationField.get(actuatorOperation);
				handleOperationMethod(discoveredOperation.getOperationMethod(), components, operation);
			}
		}
		catch (IllegalAccessException e) {
			LOGGER.warn(e.getMessage());
		}
	}

	/**
	 * Handle operation method.
	 *
	 * @param operationMethod the operation method
	 * @param components      the components
	 * @param operation       the operation
	 */
	private void handleOperationMethod(OperationMethod operationMethod, Components components, Operation operation) {
		String operationId = operationMethod.getMethod().getName();
		operation.setOperationId(operationId);

		switch (operationMethod.getOperationType()) {
			case READ:
				addParameters(operationMethod, operation, components, ParameterIn.QUERY);
				break;
			case WRITE:
				addWriteParameters(operationMethod,components, operation);
				operation.setResponses(new ApiResponses()
						.addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()), new ApiResponse().description(HttpStatus.NO_CONTENT.getReasonPhrase()))
						.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().description(HttpStatus.BAD_REQUEST.getReasonPhrase())));
				break;
			case DELETE:
				operation.setResponses(new ApiResponses().addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()), new ApiResponse().description(HttpStatus.NO_CONTENT.getReasonPhrase())));
				addParameters(operationMethod, operation, components, ParameterIn.QUERY);
				break;
			default:
				break;
		}
	}

	/**
	 * Add parameters.
	 *
	 * @param operationMethod the operation method
	 * @param operation       the operation
	 * @param components      the components
	 * @param parameterIn     the parameter in
	 */
	private void addParameters(OperationMethod operationMethod, Operation operation, Components components, ParameterIn parameterIn) {
		for (OperationParameter operationParameter : operationMethod.getParameters()) {
			Parameter parameter = getParameterFromField(operationParameter);
			if(parameter == null) continue;
			Schema<?> schema = resolveSchema(parameter, components);
			if (parameter.getAnnotation(Selector.class) != null) {
				operation.addParametersItem(new io.swagger.v3.oas.models.parameters.PathParameter()
						.name(parameter.getName())
						.schema(schema));
				operation.getResponses().addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse().description(HttpStatus.NOT_FOUND.getReasonPhrase()));
			}
			else if (isValidParameterType(parameter)) {
				operation.addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter()
						.name(parameter.getName())
						.in(parameterIn.toString())
						.schema(schema));
			}
		}
	}

	/**
	 * Add write parameters.
	 *
	 * @param operationMethod the operation method
	 * @param components      the components
	 * @param operation       the operation
	 */
	private void addWriteParameters(OperationMethod operationMethod, Components components, Operation operation) {
		for (OperationParameter operationParameter : operationMethod.getParameters()) {
			Parameter parameter = getParameterFromField(operationParameter);
			if(parameter == null) continue;
			Schema<?> schema = resolveSchema(parameter, components);
			if (parameter.getAnnotation(Selector.class) != null) {
				operation.addParametersItem(new io.swagger.v3.oas.models.parameters.PathParameter()
						.name(parameter.getName())
						.schema(schema));
			}
			else {
				operation.setRequestBody(new RequestBody()
						.content(new Content()
								.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
										new MediaType().schema(schema))));
			}
		}
	}

	/**
	 * Gets parameter from field.
	 *
	 * @param operationParameter the operation parameter
	 * @return the parameter from field
	 */
	private Parameter getParameterFromField(OperationParameter operationParameter) {
		try {
			return (Parameter) FieldUtils.readDeclaredField(operationParameter, PARAMETER, true);
		}
		catch (IllegalAccessException e) {
			LOGGER.warn(e.getMessage());
			return null;
		}
	}

	/**
	 * Resolve schema schema.
	 *
	 * @param parameter  the parameter
	 * @param components
	 * @return the schema
	 */
	private Schema<?> resolveSchema(Parameter parameter, Components components) {
		Schema schema = AnnotationsUtils.resolveSchemaFromType(parameter.getType(), components, null, springDocConfigProperties.isOpenapi31());
		if(springDocConfigProperties.isOpenapi31()) handleSchemaTypes(schema);
		return schema;
	}

	/**
	 * Is valid parameter type boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	private boolean isValidParameterType(Parameter parameter) {
		return !ApiVersion.class.isAssignableFrom(parameter.getType()) &&
				!WebServerNamespace.class.isAssignableFrom(parameter.getType()) &&
				!SecurityContext.class.isAssignableFrom(parameter.getType());
	}

	/**
	 * Sets operation summary.
	 *
	 * @param operation     the operation
	 * @param handlerMethod the handler method
	 */
	private void setOperationSummary(Operation operation, HandlerMethod handlerMethod) {
		String summary = handlerMethod.toString();
		if (operation.getSummary() == null && !summary.contains("$")) {
			operation.setSummary(summary);
		}
	}

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		return this.customize(operation, null, handlerMethod);
	}
}
