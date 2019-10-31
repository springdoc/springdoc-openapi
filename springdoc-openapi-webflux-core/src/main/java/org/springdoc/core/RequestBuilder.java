package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

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

	@Override
	protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
		return operation;
	}
}
