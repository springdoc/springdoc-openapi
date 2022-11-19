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

package org.springdoc.core.providers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.SpringDocAnnotationsUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.annotations.RouterOperations;
import org.springdoc.core.fn.RouterOperation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Spring cloud function provider.
 * @author bnasslahsen
 */
public class SpringCloudFunctionProvider implements CloudFunctionProvider, ApplicationContextAware {

	/**
	 * The constant supplierRequestMethods.
	 */
	private static final RequestMethod[] supplierRequestMethods = new RequestMethod[] { GET };

	/**
	 * The constant consumerRequestMethods.
	 */
	private static final RequestMethod[] consumerRequestMethods = new RequestMethod[] { POST };

	/**
	 * The constant functionRequestMethods.
	 */
	private static final RequestMethod[] functionRequestMethods = new RequestMethod[] { GET, POST };

	/**
	 * The constant defaultMediaTypes.
	 */
	private static final String[] defaultMediaTypes = new String[] { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE };

	/**
	 * The Function catalog.
	 */
	private final Optional<FunctionCatalog> functionCatalogOptional;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * The spring cloud function prefix.
	 */
	@Value("${spring.cloud.function.web.path:}")
	private String prefix = "";

	/**
	 * Instantiates a new Spring cloud function provider.
	 * @param functionCatalogOptional the function catalog
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public SpringCloudFunctionProvider(Optional<FunctionCatalog> functionCatalogOptional, SpringDocConfigProperties springDocConfigProperties) {
		this.functionCatalogOptional = functionCatalogOptional;
		this.springDocConfigProperties = springDocConfigProperties;
	}

	@Override
	public List<RouterOperation> getRouterOperations(OpenAPI openAPI) {
		GenericResponseService genericResponseService = applicationContext.getBean(GenericResponseService.class);
		List<RouterOperation> routerOperationList = new ArrayList<>();
		functionCatalogOptional.ifPresent(
				functionCatalog -> {
					Set<String> names = functionCatalog.getNames(null);
					for (String name : names) {
						if (!RoutingFunction.FUNCTION_NAME.equals(name) && applicationContext.containsBean(name)) {
							FunctionInvocationWrapper function = functionCatalog.lookup(name);
							if (function.isFunction()) {
								for (RequestMethod requestMethod : functionRequestMethods) {
									RouterOperation routerOperation = buildRouterOperation(name, " function", requestMethod, routerOperationList);
									buildRequest(openAPI, name, function, requestMethod, routerOperation);
									ApiResponses apiResponses = buildResponses(openAPI, function, defaultMediaTypes, genericResponseService);
									routerOperation.getOperationModel().responses(apiResponses);
									if (StringUtils.isEmpty(prefix)) {
										if (GET.equals(requestMethod))
											routerOperation.setPath(AntPathMatcher.DEFAULT_PATH_SEPARATOR + name + AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{" + name + "}");
										else
											routerOperation.setPath(AntPathMatcher.DEFAULT_PATH_SEPARATOR + name);
									}
									else {
										if (GET.equals(requestMethod))
											routerOperation.setPath(prefix + AntPathMatcher.DEFAULT_PATH_SEPARATOR + name + AntPathMatcher.DEFAULT_PATH_SEPARATOR + "{" + name + "}");
										else
											routerOperation.setPath(prefix + AntPathMatcher.DEFAULT_PATH_SEPARATOR + name);
									}

									RouterOperation userRouterOperation = this.getRouterFunctionPaths(name, requestMethod);
									if (userRouterOperation != null)
										mergeRouterOperation(routerOperation, userRouterOperation);
								}
							}
							else if (function.isConsumer()) {
								for (RequestMethod requestMethod : consumerRequestMethods) {
									RouterOperation routerOperation = buildRouterOperation(name, " consumer", requestMethod, routerOperationList);
									buildRequest(openAPI, name, function, requestMethod, routerOperation);
									ApiResponses apiResponses = new ApiResponses();
									ApiResponse apiResponse = new ApiResponse();
									apiResponse.setContent(new Content());
									apiResponses.put(String.valueOf(HttpStatus.ACCEPTED.value()), apiResponse.description(HttpStatus.ACCEPTED.getReasonPhrase()));
									getRouterOperationsCommon(name, requestMethod, routerOperation, apiResponses);
								}
							}
							else if (function.isSupplier()) {
								for (RequestMethod requestMethod : supplierRequestMethods) {
									RouterOperation routerOperation = buildRouterOperation(name, " supplier", requestMethod, routerOperationList);
									ApiResponses apiResponses = buildResponses(openAPI, function, new String[] { springDocConfigProperties.getDefaultProducesMediaType() }, genericResponseService);
									getRouterOperationsCommon(name, requestMethod, routerOperation, apiResponses);
								}
							}
						}
					}
				}
		);
		return routerOperationList;
	}

	/**
	 * Gets router operation common.
	 *
	 * @param name the name
	 * @param requestMethod the request method
	 * @param routerOperation the router operation
	 * @param apiResponses the api responses
	 */
	private void getRouterOperationsCommon(String name, RequestMethod requestMethod, RouterOperation routerOperation, ApiResponses apiResponses) {
		routerOperation.getOperationModel().responses(apiResponses);
		if (StringUtils.isEmpty(prefix))
			routerOperation.setPath(AntPathMatcher.DEFAULT_PATH_SEPARATOR + name);
		else
			routerOperation.setPath(prefix + AntPathMatcher.DEFAULT_PATH_SEPARATOR + name);
		RouterOperation userRouterOperation = this.getRouterFunctionPaths(name, requestMethod);
		if (userRouterOperation != null)
			mergeRouterOperation(routerOperation, userRouterOperation);
	}

	/**
	 * Build request.
	 *
	 * @param openAPI the open api
	 * @param name the name
	 * @param function the function
	 * @param requestMethod the request method
	 * @param routerOperation the router operation
	 */
	private void buildRequest(OpenAPI openAPI, String name, FunctionInvocationWrapper function, RequestMethod requestMethod, RouterOperation routerOperation) {
		Type paramType = function.getInputType();
		Schema<?> schema = SpringDocAnnotationsUtils.extractSchema(openAPI.getComponents(), paramType, null, null);
		if (GET.equals(requestMethod)) {
			Parameter parameter = new PathParameter().name(name).schema(schema);
			routerOperation.getOperationModel().addParametersItem(parameter);
		}
		else {
			RequestBody requestBody = new RequestBody();
			Content content = new Content();
			for (String defaultMediaType : defaultMediaTypes) {
				content.addMediaType(defaultMediaType, new io.swagger.v3.oas.models.media.MediaType().schema(schema));
			}
			requestBody.setContent(content);
			routerOperation.getOperationModel().setRequestBody(requestBody);
		}
	}

	/**
	 * Build router operation router operation.
	 *
	 * @param name the name
	 * @param type the type
	 * @param requestMethod the request method
	 * @param routerOperationList the router operation list
	 * @return the router operation
	 */
	private RouterOperation buildRouterOperation(String name, String type, RequestMethod requestMethod, List<RouterOperation> routerOperationList) {
		Operation operation = new Operation().operationId(name + "_" + requestMethod);
		RouterOperation routerOperation = new RouterOperation();
		routerOperation.setConsumes(defaultMediaTypes);
		routerOperation.setProduces(defaultMediaTypes);
		routerOperation.setMethods(new RequestMethod[] { requestMethod });
		operation.description(name + type);
		routerOperation.setOperationModel(operation);
		routerOperationList.add(routerOperation);
		return routerOperation;
	}

	/**
	 * Build responses api responses.
	 *
	 * @param openAPI the open api
	 * @param function the function
	 * @param mediaTypes the media types
	 * @param genericResponseService the generic response service
	 * @return the api responses
	 */
	private ApiResponses buildResponses(OpenAPI openAPI, FunctionInvocationWrapper function, String[] mediaTypes, GenericResponseService genericResponseService) {
		Type returnType = function.getOutputType();
		Content content = genericResponseService.buildContent(openAPI.getComponents(), null, mediaTypes, null, returnType);
		ApiResponses apiResponses = new ApiResponses();
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setContent(content);
		apiResponses.put(String.valueOf(HttpStatus.OK.value()), apiResponse.description(HttpStatus.OK.getReasonPhrase()));
		return apiResponses;
	}


	/**
	 * Gets router function paths.
	 *
	 * @param beanName the bean name
	 * @param requestMethod the request method
	 * @return the router function paths
	 */
	protected RouterOperation getRouterFunctionPaths(String beanName, RequestMethod requestMethod) {
		RouterOperations routerOperations = applicationContext.findAnnotationOnBean(beanName, RouterOperations.class);
		RouterOperation routerOperationResult = null;
		if (routerOperations == null) {
			org.springdoc.core.annotations.RouterOperation routerOperation = applicationContext.findAnnotationOnBean(beanName, org.springdoc.core.annotations.RouterOperation.class);
			if (routerOperation != null)
				routerOperationResult = new RouterOperation(routerOperation);
		}
		else {
			org.springdoc.core.annotations.RouterOperation[] routerOperationArray = routerOperations.value();
			if (ArrayUtils.isNotEmpty(routerOperationArray)) {
				if (routerOperationArray.length == 1)
					routerOperationResult = new RouterOperation(routerOperationArray[0]);
				else {
					Optional<org.springdoc.core.annotations.RouterOperation> routerOperationOptional = Arrays.stream(routerOperationArray)
							.filter(routerOperation -> Arrays.asList(routerOperation.method()).contains(requestMethod)).findAny();
					if (routerOperationOptional.isPresent())
						routerOperationResult = new RouterOperation(routerOperationOptional.get(), requestMethod);
				}
			}
		}
		return routerOperationResult;
	}


	/**
	 * Fill router operation.
	 *
	 * @param routerOperation the router operation
	 * @param userRouterOperation the user router operation
	 */
	private void mergeRouterOperation(RouterOperation routerOperation, RouterOperation userRouterOperation) {
		if (!ArrayUtils.isEmpty(userRouterOperation.getConsumes()))
			routerOperation.setConsumes(userRouterOperation.getConsumes());
		if (!ArrayUtils.isEmpty(userRouterOperation.getProduces()))
			routerOperation.setProduces(userRouterOperation.getProduces());
		if (!ArrayUtils.isEmpty(userRouterOperation.getHeaders()))
			routerOperation.setHeaders(userRouterOperation.getHeaders());
		if (!ArrayUtils.isEmpty(userRouterOperation.getMethods()))
			routerOperation.setMethods(userRouterOperation.getMethods());
		if (!CollectionUtils.isEmpty(userRouterOperation.getQueryParams()))
			routerOperation.setQueryParams(userRouterOperation.getQueryParams());
		if (!Void.class.equals(userRouterOperation.getBeanClass()))
			routerOperation.setBeanClass(userRouterOperation.getBeanClass());
		if (userRouterOperation.getOperation() != null)
			routerOperation.setOperation(userRouterOperation.getOperation());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
