package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import kotlin.coroutines.Continuation;
import org.springframework.web.method.HandlerMethod;


public class KotlinCoroutinesRequestBuilder extends AbstractRequestBuilder {


    public KotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                          OperationBuilder operationBuilder) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
    }

    @Override
    protected boolean isParamTypeToIgnore(Class<?> paramType) {
        return paramType.isAssignableFrom(Continuation.class);
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}
