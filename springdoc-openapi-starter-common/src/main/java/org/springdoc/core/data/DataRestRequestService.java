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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.extractor.DelegatingMethodParameter;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.models.ParameterInfo;
import org.springdoc.core.models.RequestBodyInfo;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.RequestBodyService;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
import org.springdoc.core.utils.SpringDocDataRestUtils;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.support.BackendId;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.utils.SpringDocUtils.getParameterAnnotations;

/**
 * The type Data rest request builder.
 *
 * @author bnasslahsen
 */
public class DataRestRequestService {

	/**
	 * The Local spring doc parameter name discoverer.
	 */
	private final SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer;

	/**
	 * The Parameter builder.
	 */
	private final GenericParameterService parameterBuilder;

	/**
	 * The Request body builder.
	 */
	private final RequestBodyService requestBodyService;

	/**
	 * The Request builder.
	 */
	private final AbstractRequestService requestBuilder;

	/**
	 * The Spring doc data rest utils.
	 */
	private final SpringDocDataRestUtils springDocDataRestUtils;

	/**
	 * Instantiates a new Data rest request builder.
	 *
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 * @param parameterBuilder                      the parameter builder
	 * @param requestBodyService                    the request body builder
	 * @param requestBuilder                        the request builder
	 * @param springDocDataRestUtils                the spring doc data rest utils
	 */
	public DataRestRequestService(SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer, GenericParameterService parameterBuilder,
			RequestBodyService requestBodyService, AbstractRequestService requestBuilder, SpringDocDataRestUtils springDocDataRestUtils) {
		this.localSpringDocParameterNameDiscoverer = localSpringDocParameterNameDiscoverer;
		this.parameterBuilder = parameterBuilder;
		this.requestBodyService = requestBodyService;
		this.requestBuilder = requestBuilder;
		this.springDocDataRestUtils = springDocDataRestUtils;
	}

	/**
	 * Build parameters.
	 *
	 * @param openAPI            the open api
	 * @param handlerMethod      the handler method
	 * @param requestMethod      the request method
	 * @param methodAttributes   the method attributes
	 * @param operation          the operation
	 * @param resourceMetadata   the resource metadata
	 * @param dataRestRepository the data rest repository
	 */
	public void buildParameters(OpenAPI openAPI, HandlerMethod handlerMethod, RequestMethod requestMethod, MethodAttributes methodAttributes,
			Operation operation, ResourceMetadata resourceMetadata, DataRestRepository dataRestRepository) {
		String[] pNames = this.localSpringDocParameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		if (!resourceMetadata.isPagingResource()) {
			Optional<MethodParameter> methodParameterPage = Arrays.stream(parameters).filter(methodParameter -> DefaultedPageable.class.equals(methodParameter.getParameterType())).findFirst();
			if (methodParameterPage.isPresent())
				parameters = ArrayUtils.removeElement(parameters, methodParameterPage.get());
		}
		String[] reflectionParametersNames = Arrays.stream(handlerMethod.getMethod().getParameters()).map(java.lang.reflect.Parameter::getName).toArray(String[]::new);
		if (pNames == null || Arrays.stream(pNames).anyMatch(Objects::isNull))
			pNames = reflectionParametersNames;
		buildCommonParameters(openAPI, requestMethod, methodAttributes, operation, pNames, parameters, dataRestRepository);
	}

	/**
	 * Build common parameters.
	 *
	 * @param openAPI            the open api
	 * @param requestMethod      the request method
	 * @param methodAttributes   the method attributes
	 * @param operation          the operation
	 * @param pNames             the p names
	 * @param parameters         the parameters
	 * @param dataRestRepository the data rest repository
	 */
	public void buildCommonParameters(OpenAPI openAPI, RequestMethod requestMethod, MethodAttributes methodAttributes, Operation operation, String[] pNames, MethodParameter[] parameters,
			DataRestRepository dataRestRepository) {
		parameters = DelegatingMethodParameter.customize(pNames, parameters, parameterBuilder.getOptionalDelegatingMethodParameterCustomizers(), requestBuilder.isDefaultFlatParamObject());
		Class<?> domainType = dataRestRepository.getDomainType();
		for (MethodParameter methodParameter : parameters) {
			final String pName = methodParameter.getParameterName();
			io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
					AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()),
					io.swagger.v3.oas.annotations.Parameter.class);
			ParameterInfo parameterInfo = new ParameterInfo(pName, methodParameter, parameterBuilder, parameterDoc);
			if (isParamToIgnore(methodParameter)) {
				if (PersistentEntityResource.class.equals(methodParameter.getParameterType())) {
					Schema<?> schema = SpringDocAnnotationsUtils.resolveSchemaFromType(domainType, openAPI.getComponents(), null, getParameterAnnotations(methodParameter), openAPI.getSpecVersion());
					parameterInfo.setParameterModel(new Parameter().schema(schema));
				}
				else if (methodParameter.getParameterAnnotation(BackendId.class) != null) {
					parameterInfo.setParameterModel(new Parameter().name("id").in(ParameterIn.PATH.toString()).schema(new StringSchema()));
				}
				Parameter parameter = null;
				if (parameterDoc != null) {
					if (parameterDoc.hidden() || parameterDoc.schema().hidden())
						continue;
					parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, openAPI.getComponents(), methodAttributes.getJsonViewAnnotation(), methodAttributes.getLocale());
					parameterInfo.setParameterModel(parameter);
				}
				if (!ArrayUtils.isEmpty(methodParameter.getParameterAnnotations()))
					parameter = requestBuilder.buildParams(parameterInfo, openAPI.getComponents(), requestMethod, methodAttributes,
							openAPI.getOpenapi());
				addParameters(openAPI, requestMethod, methodAttributes, operation, methodParameter, parameterInfo, parameter);
			}
		}
	}

	/**
	 * Build parameter from doc parameter.
	 *
	 * @param parameterDoc       the parameter doc
	 * @param components         the components
	 * @param jsonViewAnnotation the json view annotation
	 * @param locale             the locale
	 * @return the parameter
	 */
	public Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc, Components components, JsonView jsonViewAnnotation, Locale locale) {
		return parameterBuilder.buildParameterFromDoc(parameterDoc, components, jsonViewAnnotation, locale);
	}

	/**
	 * Is param to ignore boolean.
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean isParamToIgnore(MethodParameter methodParameter) {
		return !requestBuilder.isParamToIgnore(methodParameter)
				&& !isHeaderToIgnore(methodParameter)
				&& !"property".equals(methodParameter.getParameterName());
	}

	/**
	 * Add parameters.
	 *
	 * @param openAPI          the open api
	 * @param requestMethod    the request method
	 * @param methodAttributes the method attributes
	 * @param operation        the operation
	 * @param methodParameter  the method parameter
	 * @param parameterInfo    the parameter info
	 * @param parameter        the parameter
	 */
	private void addParameters(OpenAPI openAPI, RequestMethod requestMethod, MethodAttributes methodAttributes, Operation operation,
			MethodParameter methodParameter, ParameterInfo parameterInfo, Parameter parameter) {
		List<Annotation> parameterAnnotations = Arrays.asList(getParameterAnnotations(methodParameter));
		if (requestBuilder.isValidParameter(parameter,methodAttributes)) {
			requestBuilder.applyBeanValidatorAnnotations(methodParameter, parameter, parameterAnnotations, parameterInfo.isParameterObject(), openAPI.getOpenapi());
			operation.addParametersItem(parameter);
		}
		else if (!RequestMethod.GET.equals(requestMethod)) {
			RequestBodyInfo requestBodyInfo = new RequestBodyInfo();
			if (operation.getRequestBody() != null)
				requestBodyInfo.setRequestBody(operation.getRequestBody());
			requestBodyService.calculateRequestBodyInfo(openAPI.getComponents(), methodAttributes,
					parameterInfo, requestBodyInfo);
			requestBuilder.applyBeanValidatorAnnotations(requestBodyInfo.getRequestBody(), parameterAnnotations, methodParameter.isOptional());
			operation.setRequestBody(requestBodyInfo.getRequestBody());
			Content content = operation.getRequestBody().getContent();
			springDocDataRestUtils.buildTextUriContent(content);
			operation.getRequestBody().setRequired(true);
		}
	}


	/**
	 * Is header to ignore boolean.
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean isHeaderToIgnore(MethodParameter methodParameter) {
		RequestHeader requestHeader = methodParameter.getParameterAnnotation(RequestHeader.class);
		return requestHeader != null && HttpHeaders.ACCEPT.equals(requestHeader.value());
	}
}
