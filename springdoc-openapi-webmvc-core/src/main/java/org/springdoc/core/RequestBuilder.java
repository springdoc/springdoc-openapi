package org.springdoc.core;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;

import java.util.List;
import java.util.Optional;

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
                          OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> customizers,
                          Optional<List<ParameterCustomizer>> parameterCustomizers) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder, customizers, parameterCustomizers);
    }
}
