package org.springdoc.core;

import org.springdoc.core.customizer.OperationCustomizer;
import org.springdoc.core.customizer.ParameterCustomizer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class RequestBuilder extends AbstractRequestBuilder {

    static {
        PARAM_TYPES_TO_IGNORE.add(ServerWebExchange.class);
        PARAM_TYPES_TO_IGNORE.add(ServerHttpRequest.class);
        PARAM_TYPES_TO_IGNORE.add(ServerHttpResponse.class);
    }

    public RequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                          OperationBuilder operationBuilder, List<OperationCustomizer> operationCustomizers,
                          List<ParameterCustomizer> parameterCustomizers) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder, operationCustomizers, parameterCustomizers);
    }
}
