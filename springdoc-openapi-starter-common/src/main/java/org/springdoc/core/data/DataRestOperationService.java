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

package org.springdoc.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ParameterMetadata;
import org.springframework.data.rest.core.mapping.ParametersMetadata;
import org.springframework.data.rest.core.mapping.ResourceDescription;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.TypedResourceDescription;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Data rest operation builder.
 *
 * @author bnasslahsen
 */
public class DataRestOperationService {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataRestOperationService.class);

	/**
	 * The constant STRING_SEPARATOR.
	 */
	private static final String STRING_SEPARATOR = "-";

	/**
	 * The Data rest request builder.
	 */
	private final DataRestRequestService dataRestRequestService;

	/**
	 * The Tags builder.
	 */
	private final DataRestTagsService tagsBuilder;

	/**
	 * The Data rest response builder.
	 */
	private final DataRestResponseService dataRestResponseService;

	/**
	 * The Operation service.
	 */
	private final OperationService operationService;

	/**
	 * Instantiates a new Data rest operation builder.
	 *
	 * @param dataRestRequestService  the data rest request builder
	 * @param tagsBuilder             the tags builder
	 * @param dataRestResponseService the data rest response builder
	 * @param operationService        the operation service
	 */
	public DataRestOperationService(DataRestRequestService dataRestRequestService, DataRestTagsService tagsBuilder,
			DataRestResponseService dataRestResponseService, OperationService operationService) {
		this.dataRestRequestService = dataRestRequestService;
		this.tagsBuilder = tagsBuilder;
		this.dataRestResponseService = dataRestResponseService;
		this.operationService = operationService;
	}

	/**
	 * Build operation.
	 *
	 * @param handlerMethod         the handler method
	 * @param dataRestRepository    the repository data rest
	 * @param openAPI               the open api
	 * @param requestMethod         the request method
	 * @param operationPath         the operation path
	 * @param methodAttributes      the method attributes
	 * @param resourceMetadata      the resource metadata
	 * @param methodResourceMapping the method resource mapping
	 * @param controllerType        the controller type
	 * @return the operation
	 */
	public Operation buildOperation(HandlerMethod handlerMethod, DataRestRepository dataRestRepository,
			OpenAPI openAPI, RequestMethod requestMethod, String operationPath, MethodAttributes methodAttributes,
			ResourceMetadata resourceMetadata, MethodResourceMapping methodResourceMapping, ControllerType controllerType) {
		Operation operation = null;
		if (ControllerType.ENTITY.equals(controllerType)
				|| ControllerType.PROPERTY.equals(controllerType)
				|| ControllerType.SCHEMA.equals(controllerType) || ControllerType.GENERAL.equals(controllerType)) {
			operation = buildEntityOperation(handlerMethod, dataRestRepository,
					openAPI, requestMethod, operationPath, methodAttributes, resourceMetadata);
		}
		else if (ControllerType.SEARCH.equals(controllerType)) {
			operation = buildSearchOperation(handlerMethod, dataRestRepository, openAPI, requestMethod,
					methodAttributes, methodResourceMapping, resourceMetadata);
		}
		return operation;
	}

	/**
	 * Build entity operation.
	 *
	 * @param handlerMethod      the handler method
	 * @param dataRestRepository the repository data rest
	 * @param openAPI            the open api
	 * @param requestMethod      the request method
	 * @param operationPath      the operation path
	 * @param methodAttributes   the method attributes
	 * @param resourceMetadata   the resource metadata
	 * @return the operation
	 */
	private Operation buildEntityOperation(HandlerMethod handlerMethod, DataRestRepository dataRestRepository,
			OpenAPI openAPI, RequestMethod requestMethod, String operationPath, MethodAttributes methodAttributes,
			ResourceMetadata resourceMetadata) {
		Class<?> domainType = null;
		if (!ControllerType.GENERAL.equals(dataRestRepository.getControllerType()))
			domainType = dataRestRepository.getDomainType();
		Operation operation = initOperation(handlerMethod, domainType, requestMethod);
		dataRestRequestService.buildParameters(openAPI, handlerMethod, requestMethod, methodAttributes, operation, resourceMetadata, dataRestRepository);
		dataRestResponseService.buildEntityResponse(operation, handlerMethod, openAPI, requestMethod, operationPath, methodAttributes, dataRestRepository, resourceMetadata);
		tagsBuilder.buildEntityTags(operation, handlerMethod, dataRestRepository);
		if (domainType != null)
			addOperationDescription(operation, requestMethod, domainType.getSimpleName().toLowerCase(), dataRestRepository);
		return operation;
	}

	/**
	 * Build search operation.
	 *
	 * @param handlerMethod         the handler method
	 * @param dataRestRepository    the repository data rest
	 * @param openAPI               the open api
	 * @param requestMethod         the request method
	 * @param methodAttributes      the method attributes
	 * @param methodResourceMapping the method resource mapping
	 * @param resourceMetadata      the resource metadata
	 * @return the operation
	 */
	private Operation buildSearchOperation(HandlerMethod handlerMethod, DataRestRepository dataRestRepository,
			OpenAPI openAPI, RequestMethod requestMethod, MethodAttributes methodAttributes,
			MethodResourceMapping methodResourceMapping, ResourceMetadata resourceMetadata) {
		Class<?> domainType = dataRestRepository.getDomainType();
		Operation operation = initOperation(handlerMethod, domainType, requestMethod);

		// Add support for operation annotation
		io.swagger.v3.oas.annotations.Operation apiOperation = AnnotatedElementUtils.findMergedAnnotation(methodResourceMapping.getMethod(),
				io.swagger.v3.oas.annotations.Operation.class);

		if (apiOperation != null)
			operationService.parse(apiOperation, operation, openAPI, methodAttributes);

		ParametersMetadata parameterMetadata = methodResourceMapping.getParametersMetadata();
		Method method = methodResourceMapping.getMethod();

		if (!CollectionUtils.isEmpty(parameterMetadata.getParameterNames())) {
			HandlerMethod repositoryHandlerMethod = new HandlerMethod(methodResourceMapping.getMethod().getDeclaringClass(), methodResourceMapping.getMethod());
			MethodParameter[] parameters = repositoryHandlerMethod.getMethodParameters();
			for (MethodParameter methodParameter : parameters) {
				dataRestRequestService.buildCommonParameters(openAPI, requestMethod, methodAttributes, operation, new String[] { methodParameter.getParameterName() }, new MethodParameter[] { methodParameter }, dataRestRepository);
			}
		}

		for (ParameterMetadata parameterMetadatum : parameterMetadata) {
			String pName = parameterMetadatum.getName();
			ResourceDescription description = parameterMetadatum.getDescription();
			if (description instanceof TypedResourceDescription) {
				Type type = getParameterType(pName,method,description);
				Schema<?> schema = SpringDocAnnotationsUtils.extractSchema(openAPI.getComponents(), type, null, null, openAPI.getSpecVersion());
				Parameter parameter = getParameterFromAnnotations(openAPI, methodAttributes, method, pName);
				if (parameter == null) {
					parameter = new Parameter().name(pName).in(ParameterIn.QUERY.toString()).schema(schema);
					operation.addParametersItem(parameter);
				}
				else if (CollectionUtils.isEmpty(operation.getParameters()))
					operation.addParametersItem(parameter);
				else
					GenericParameterService.mergeParameter(operation.getParameters(), parameter);
			}
		}

		if (methodResourceMapping.isPagingResource()) {
			MethodParameter[] parameters = handlerMethod.getMethodParameters();
			Arrays.stream(parameters).filter(methodParameter -> DefaultedPageable.class.equals(methodParameter.getParameterType())).findAny()
					.ifPresent(methodParameterPage -> dataRestRequestService.buildCommonParameters(openAPI, requestMethod, methodAttributes, operation,
							new String[] { methodParameterPage.getParameterName() }, new MethodParameter[] { methodParameterPage }, dataRestRepository));
		}
		dataRestResponseService.buildSearchResponse(operation, handlerMethod, openAPI, methodResourceMapping, domainType, methodAttributes, resourceMetadata, dataRestRepository);
		tagsBuilder.buildSearchTags(operation, handlerMethod, dataRestRepository, method);
		return operation;
	}

	/**
	 * Gets parameter type.
	 *
	 * @param pName       the p name
	 * @param method      the method
	 * @param description the description
	 * @return the parameter type
	 */
	private Type getParameterType(String pName, Method method, ResourceDescription description) {
		Type type = null;
		java.lang.reflect.Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			java.lang.reflect.Parameter parameter = parameters[i];
			if (pName.equals(parameter.getName()) || (parameter.getAnnotation(Param.class)!=null && pName.equals(parameter.getAnnotation(Param.class).value()))) {
				ResolvableType resolvableType = ResolvableType.forMethodParameter(method, i);
				type = resolvableType.getType();
				break;
			}
		}
		if (type == null) {
			TypedResourceDescription typedResourceDescription = (TypedResourceDescription) description;
			Field fieldType = FieldUtils.getField(TypedResourceDescription.class, "type", true);
			try {
				type = (Type) fieldType.get(typedResourceDescription);
			}
			catch (IllegalAccessException e) {
				LOGGER.warn(e.getMessage());
				type = String.class;
			}
		}
		return type;
	}

	/**
	 * Update parameter from annotations parameter.
	 *
	 * @param openAPI          the open api
	 * @param methodAttributes the method attributes
	 * @param method           the method
	 * @param pName            the p name
	 * @return the parameter
	 */
	private Parameter getParameterFromAnnotations(OpenAPI openAPI, MethodAttributes methodAttributes, Method method, String pName) {
		Parameter parameter = null;
		for (java.lang.reflect.Parameter reflectParameter : method.getParameters()) {
			Param paramAnnotation = reflectParameter.getAnnotation(Param.class);
			if (paramAnnotation != null && paramAnnotation.value().equals(pName)) {
				io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
						AnnotatedElementUtils.forAnnotations(reflectParameter.getAnnotations()),
						io.swagger.v3.oas.annotations.Parameter.class);
				if (parameterDoc != null && (!parameterDoc.hidden() || parameterDoc.schema().hidden())) {
					parameter = dataRestRequestService.buildParameterFromDoc(parameterDoc, openAPI.getComponents(), methodAttributes.getJsonViewAnnotation(), methodAttributes.getLocale());
					parameter.setName(pName);
				}
			}
		}
		return parameter;
	}

	/**
	 * Init operation.
	 *
	 * @param handlerMethod the handler method
	 * @param domainType    the domain type
	 * @param requestMethod the request method
	 * @return the operation
	 */
	private Operation initOperation(HandlerMethod handlerMethod, Class<?> domainType, RequestMethod requestMethod) {
		Operation operation = new Operation();
		StringBuilder operationIdBuilder = new StringBuilder();
		operationIdBuilder.append(handlerMethod.getMethod().getName());
		if (domainType != null) {
			operationIdBuilder.append(STRING_SEPARATOR).append(domainType.getSimpleName().toLowerCase())
					.append(STRING_SEPARATOR).append(requestMethod.toString().toLowerCase());
		}
		operation.setOperationId(operationIdBuilder.toString());
		return operation;
	}

	/**
	 * Add operation description.
	 *
	 * @param operation          the operation
	 * @param requestMethod      the request method
	 * @param entity             the entity
	 * @param dataRestRepository the data rest repository
	 */
	private void addOperationDescription(Operation operation, RequestMethod requestMethod, String entity, DataRestRepository dataRestRepository) {
		switch (requestMethod) {
			case GET:
				operation.setDescription(createDescription("get-", entity, dataRestRepository));
				break;
			case POST:
				operation.setDescription(createDescription("create-", entity, dataRestRepository));
				break;
			case DELETE:
				operation.setDescription(createDescription("delete-", entity, dataRestRepository));
				break;
			case PUT:
				operation.setDescription(createDescription("update-", entity, dataRestRepository));
				break;
			case PATCH:
				operation.setDescription(createDescription("patch-", entity, dataRestRepository));
				break;
			default:
				throw new IllegalArgumentException(requestMethod.name());
		}
	}

	/**
	 * Create description.
	 *
	 * @param action             the action
	 * @param entity             the entity
	 * @param dataRestRepository the data rest repository
	 * @return the string
	 */
	private String createDescription(String action, String entity, DataRestRepository dataRestRepository) {
		String description;
		if (ControllerType.PROPERTY.equals(dataRestRepository.getControllerType()))
			description = action + dataRestRepository.getPropertyType().getSimpleName().toLowerCase() + "-by-" + entity + "-Id";
		else
			description = action + entity;
		return description;
	}
}
