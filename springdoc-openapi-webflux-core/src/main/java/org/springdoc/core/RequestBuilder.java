package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;

public class RequestBuilder extends AbstractRequestBuilder {

    static {
        PARAM_TYPES_TO_IGNORE.add(ServerWebExchange.class);
        PARAM_TYPES_TO_IGNORE.add(ServerHttpRequest.class);
        PARAM_TYPES_TO_IGNORE.add(ServerHttpResponse.class);
    }

    public RequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                          OperationBuilder operationBuilder) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}
