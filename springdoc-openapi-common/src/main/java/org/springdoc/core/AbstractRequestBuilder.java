package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

public abstract class AbstractRequestBuilder {

	private ParameterBuilder parameterBuilder;

	private RequestBodyBuilder requestBodyBuilder;

	protected AbstractRequestBuilder(ParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyBuilder = requestBodyBuilder;
	}

	abstract boolean isParamTypeToIgnore(Class<?> paramType);

	public Operation build(Components components, HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, MediaAttributes mediaAttributes) {
		// Documentation
		operation.setOperationId(handlerMethod.getMethod().getName());
		// requests
		LocalVariableTableParameterNameDiscoverer d = new LocalVariableTableParameterNameDiscoverer();
		String[] pNames = d.getParameterNames(handlerMethod.getMethod());
		List<Parameter> operationParameters = new ArrayList<>();
		java.lang.reflect.Parameter[] parameters = handlerMethod.getMethod().getParameters();

		for (int i = 0; i < pNames.length; i++) {
			// check if query param
			Parameter parameter = null;
			Class<?> paramType = parameters[i].getType();
			io.swagger.v3.oas.annotations.Parameter parameterDoc = parameterBuilder.getParameterAnnotation(
					handlerMethod, parameters[i], i, io.swagger.v3.oas.annotations.Parameter.class);

			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden()) {
					continue;
				}
				parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, null);
			}

			if (!isParamTypeToIgnore(paramType)) {
				parameter = buildParams(pNames[i], components, parameters[i], i, parameter, handlerMethod,
						requestMethod);
				if (parameter != null && parameter.getName() != null) {
					applyBeanValidatorAnnotations(parameter, Arrays.asList(parameters[i].getAnnotations()));
					operationParameters.add(parameter);
				} else if (!RequestMethod.GET.equals(requestMethod)) {
					RequestBody requestBody = requestBodyBuilder.calculateRequestBody(components, handlerMethod,
							operation, mediaAttributes, pNames, parameters, i, parameterDoc);
					operation.setRequestBody(requestBody);
				}
			}
		}
		if (!CollectionUtils.isEmpty(operationParameters)) {
			operation.setParameters(operationParameters);
		}

		return operation;
	}

	private Parameter buildParams(String pName, Components components, java.lang.reflect.Parameter parameters,
			int index, Parameter parameter, HandlerMethod handlerMethod, RequestMethod requestMethod) {
		RequestHeader requestHeader = parameterBuilder.getParameterAnnotation(handlerMethod, parameters, index,
				RequestHeader.class);
		RequestParam requestParam = parameterBuilder.getParameterAnnotation(handlerMethod, parameters, index,
				RequestParam.class);
		PathVariable pathVar = parameterBuilder.getParameterAnnotation(handlerMethod, parameters, index,
				PathVariable.class);

		if (requestHeader != null) {
			String name = StringUtils.isBlank(requestHeader.value()) ? pName : requestHeader.value();
			parameter = this.buildParam(HEADER_PARAM, components, parameters, requestHeader.required(), name,
					parameter);
		} else if (requestParam != null) {
			String name = StringUtils.isBlank(requestParam.value()) ? pName : requestParam.value();
			parameter = this.buildParam(QUERY_PARAM, components, parameters, requestParam.required(), name, parameter);
		} else if (pathVar != null) {
			String name = StringUtils.isBlank(pathVar.value()) ? pName : pathVar.value();
			// check if PATH PARAM
			parameter = this.buildParam(PATH_PARAM, components, parameters, Boolean.TRUE, name, parameter);
		}
		// By default
		if (RequestMethod.GET.equals(requestMethod) && parameter == null) {
			parameter = this.buildParam(QUERY_PARAM, null, parameters, Boolean.TRUE, pName, null);
		} else if (RequestMethod.GET.equals(requestMethod) && parameter.getName() == null) {
			parameter.setName(pName);
		}
		return parameter;
	}

	private Parameter buildParam(String in, Components components, java.lang.reflect.Parameter parameters,
			Boolean required, String name, Parameter parameter) {
		if (parameter == null)
			parameter = new Parameter();
		parameter.setIn(in);
		parameter.setRequired(required);
		parameter.setName(name);
		Schema<?> schema = parameterBuilder.calculateSchema(components, parameters, name);
		parameter.setSchema(schema);
		return parameter;
	}

	/**
	 * This is mostly a duplicate of
	 * {@link io.swagger.v3.core.jackson.ModelResolver#applyBeanValidatorAnnotations}.
	 *
	 * @param parameter
	 * @param annotations
	 */
	private void applyBeanValidatorAnnotations(final Parameter parameter, final List<Annotation> annotations) {
		Map<String, Annotation> annos = new HashMap<>();
		if (annotations != null) {
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getName(), annotation));
		}

		if (annos.containsKey(NotNull.class.getName())) {
			parameter.setRequired(true);
		}

		Schema<?> schema = parameter.getSchema();

		if (annos.containsKey(Min.class.getName())) {
			Min min = (Min) annos.get(Min.class.getName());
			schema.setMinimum(BigDecimal.valueOf(min.value()));
		}
		if (annos.containsKey(Max.class.getName())) {
			Max max = (Max) annos.get(Max.class.getName());
			schema.setMaximum(BigDecimal.valueOf(max.value()));
		}
		calculateSize(annos, schema);
		if (annos.containsKey(DecimalMin.class.getName())) {
			DecimalMin min = (DecimalMin) annos.get(DecimalMin.class.getName());
			if (min.inclusive()) {
				schema.setMinimum(BigDecimal.valueOf(Double.valueOf(min.value())));
			} else {
				schema.setExclusiveMinimum(!min.inclusive());
			}
		}
		if (annos.containsKey(DecimalMax.class.getName())) {
			DecimalMax max = (DecimalMax) annos.get(DecimalMax.class.getName());
			if (max.inclusive()) {
				schema.setMaximum(BigDecimal.valueOf(Double.valueOf(max.value())));
			} else {
				schema.setExclusiveMaximum(!max.inclusive());
			}
		}
		if (annos.containsKey(Pattern.class.getName())) {
			Pattern pattern = (Pattern) annos.get(Pattern.class.getName());
			schema.setPattern(pattern.regexp());
		}
	}

	private void calculateSize(Map<String, Annotation> annos, Schema<?> schema) {
		if (annos.containsKey(Size.class.getName())) {
			Size size = (Size) annos.get(Size.class.getName());
			if (OPENAPI_ARRAY_TYPE.equals(schema.getType())) {
				schema.setMinItems(size.min());
				schema.setMaxItems(size.max());
			} else if (OPENAPI_STRING_TYPE.equals(schema.getType())) {
				schema.setMinLength(size.min());
				schema.setMaxLength(size.max());
			}
		}
	}
}
