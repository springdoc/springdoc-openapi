package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServerHttpResponse;
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

    @Override
    protected boolean isParamTypeToIgnore(Class<?> paramType) {
        return WebRequest.class.equals(paramType) || NativeWebRequest.class.equals(paramType)
                || java.security.Principal.class.equals(paramType) || HttpMethod.class.equals(paramType)
                || java.util.Locale.class.equals(paramType) || java.util.TimeZone.class.equals(paramType)
                || java.time.ZoneId.class.equals(paramType) || java.io.InputStream.class.equals(paramType)
                || java.io.Reader.class.equals(paramType) || java.io.OutputStream.class.equals(paramType)
                || java.io.Writer.class.equals(paramType) || java.util.Map.class.equals(paramType)
                || org.springframework.ui.Model.class.equals(paramType) || ServerHttpRequest.class.equals(paramType)
                || org.springframework.ui.ModelMap.class.equals(paramType) || ServerHttpResponse.class.equals(paramType)
                || Errors.class.equals(paramType) || BindingResult.class.equals(paramType) || ServerWebExchange.class.equals(paramType)
                || SessionStatus.class.equals(paramType) || UriComponentsBuilder.class.equals(paramType);
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}
