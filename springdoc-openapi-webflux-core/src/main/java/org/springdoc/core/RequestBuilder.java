package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RequestBuilder extends AbstractRequestBuilder {

    public RequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                          OperationBuilder operationBuilder) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
    }

    static {
        PARAM_TYPES_TO_IGNORE.add(WebRequest.class);
        PARAM_TYPES_TO_IGNORE.add(NativeWebRequest.class);
        PARAM_TYPES_TO_IGNORE.add(java.security.Principal.class);
        PARAM_TYPES_TO_IGNORE.add(HttpMethod.class);
        PARAM_TYPES_TO_IGNORE.add(java.util.Locale.class);
        PARAM_TYPES_TO_IGNORE.add(java.util.TimeZone.class);
        PARAM_TYPES_TO_IGNORE.add(java.io.InputStream.class);
        PARAM_TYPES_TO_IGNORE.add(java.time.ZoneId.class);
        PARAM_TYPES_TO_IGNORE.add(java.io.Reader.class);
        PARAM_TYPES_TO_IGNORE.add(java.io.OutputStream.class);
        PARAM_TYPES_TO_IGNORE.add(java.io.Writer.class);
        PARAM_TYPES_TO_IGNORE.add(java.util.Map.class);
        PARAM_TYPES_TO_IGNORE.add(org.springframework.ui.Model.class);
        PARAM_TYPES_TO_IGNORE.add(ServerHttpRequest.class);
        PARAM_TYPES_TO_IGNORE.add(org.springframework.ui.ModelMap.class);
        PARAM_TYPES_TO_IGNORE.add(Errors.class);
        PARAM_TYPES_TO_IGNORE.add(BindingResult.class);
        PARAM_TYPES_TO_IGNORE.add(ServerWebExchange.class);
        PARAM_TYPES_TO_IGNORE.add(SessionStatus.class);
        PARAM_TYPES_TO_IGNORE.add(UriComponentsBuilder.class);
    }


    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}
