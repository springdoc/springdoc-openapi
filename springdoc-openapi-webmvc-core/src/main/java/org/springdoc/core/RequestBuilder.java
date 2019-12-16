package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

public class RequestBuilder extends AbstractRequestBuilder {

    static {
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.ServletRequest.class);
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.ServletResponse.class);
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.http.HttpServletRequest.class);
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.http.HttpServletResponse.class);
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.http.HttpSession.class);
        PARAM_TYPES_TO_IGNORE.add(javax.servlet.http.HttpSession.class);
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
