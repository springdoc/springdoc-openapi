package org.springdoc.core;

import org.springframework.stereotype.Component;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

	public RequestBuilder(ParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder) {
		super(parameterBuilder, requestBodyBuilder);
	}

	boolean isParamTypeToIgnore(Class<?> paramType) {
		return false;
	}
}
