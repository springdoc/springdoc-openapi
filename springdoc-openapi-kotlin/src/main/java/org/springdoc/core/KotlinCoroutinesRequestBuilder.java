package org.springdoc.core;

import io.swagger.v3.oas.models.Operation;
import kotlin.coroutines.Continuation;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Primary
@Component
public class KotlinCoroutinesRequestBuilder extends AbstractRequestBuilder {

    private List<AbstractRequestBuilder> requestBuilders;

    public KotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                          OperationBuilder operationBuilder, List<AbstractRequestBuilder> requestBuilders) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
        this.requestBuilders = requestBuilders;
    }

    @Override
    protected boolean isParamTypeToIgnore(Class<?> paramType) {
        return paramType.isAssignableFrom(Continuation.class) || requestBuilders.stream().anyMatch(builder -> builder.isParamTypeToIgnore(paramType));
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }
}
