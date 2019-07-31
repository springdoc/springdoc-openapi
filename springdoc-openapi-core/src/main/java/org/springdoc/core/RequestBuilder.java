package org.springdoc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

	public Operation build(Components components, HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, String[] allConsumes) {
		// Documentation
		operation.setOperationId(handlerMethod.getMethod().getName());
		// requests
		LocalVariableTableParameterNameDiscoverer d = new LocalVariableTableParameterNameDiscoverer();
		String[] pNames = d.getParameterNames(handlerMethod.getMethod());
		List<Parameter> operationParameters = new ArrayList<>();
		java.lang.reflect.Parameter[] parameters = handlerMethod.getMethod().getParameters();

		MethodUtils.getAnnotation(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.Parameter.class, true, true);

		for (int i = 0; i < pNames.length; i++) {
			// check if query param
			Parameter parameter = null;
			Class<?> paramType = parameters[i].getType();

			io.swagger.v3.oas.annotations.Parameter parameterDoc = getParameterAnnotation(handlerMethod, parameters[i],
					i, io.swagger.v3.oas.annotations.Parameter.class);

			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden()) {
					continue;
				}
				parameter = buildParameterFromDoc(parameterDoc);
			}

			if (!isParamTypeToIgnore(paramType)) {

				parameter = buildParams(pNames[i], components, parameters[i], i, parameter, handlerMethod);
				// By default
				parameter = buildParamDefault(requestMethod, pNames[i], parameters[i], parameter);

				if (parameter != null && parameter.getName() != null) {
					applyBeanValidatorAnnotations(parameter, Arrays.asList(parameters[i].getAnnotations()));
					operationParameters.add(parameter);
				} else if (!RequestMethod.GET.equals(requestMethod)) {
					RequestBody requestBody = buildRequestBody(components, allConsumes, parameters[i], parameterDoc);
					operation.setRequestBody(requestBody);
				}
			}
		}

		if (!CollectionUtils.isEmpty(operationParameters)) {
			operation.setParameters(operationParameters);
		}

		return operation;
	}

	private boolean isParamTypeToIgnore(Class<?> paramType) {
		return WebRequest.class.equals(paramType) || NativeWebRequest.class.equals(paramType)
				|| javax.servlet.ServletRequest.class.equals(paramType)
				|| javax.servlet.ServletResponse.class.equals(paramType)
				|| javax.servlet.http.HttpSession.class.equals(paramType)
				|| java.security.Principal.class.equals(paramType) || HttpMethod.class.equals(paramType)
				|| java.util.Locale.class.equals(paramType) || java.util.TimeZone.class.equals(paramType)
				|| java.time.ZoneId.class.equals(paramType) || java.io.InputStream.class.equals(paramType)
				|| java.io.Reader.class.equals(paramType) || java.io.OutputStream.class.equals(paramType)
				|| java.io.Writer.class.equals(paramType) || java.util.Map.class.equals(paramType)
				|| org.springframework.ui.Model.class.equals(paramType)
				|| org.springframework.ui.ModelMap.class.equals(paramType) || RedirectAttributes.class.equals(paramType)
				|| Errors.class.equals(paramType) || BindingResult.class.equals(paramType)
				|| SessionStatus.class.equals(paramType) || UriComponentsBuilder.class.equals(paramType);
	}
}
