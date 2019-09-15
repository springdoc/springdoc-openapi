package org.springdoc.core;

import org.springframework.stereotype.Component;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

	public RequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder) {
		super(parameterBuilder, requestBodyBuilder, operationBuilder);
	}

	@Override
	protected boolean isParamTypeToIgnore(Class<?> paramType) {
		return false;
	}
}
