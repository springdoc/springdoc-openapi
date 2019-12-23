package org.springdoc.core;

import org.springdoc.core.customizer.OperationCustomizer;
import org.springdoc.core.customizer.ParameterCustomizer;

import java.util.List;

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
                          OperationBuilder operationBuilder, List<OperationCustomizer> customizers,
                          List<ParameterCustomizer> parameterCustomizers) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder, customizers, parameterCustomizers);
    }
}
