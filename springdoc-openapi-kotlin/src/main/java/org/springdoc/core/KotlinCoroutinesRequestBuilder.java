package org.springdoc.core;

import kotlin.coroutines.Continuation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;

import java.util.List;
import java.util.Optional;


public class KotlinCoroutinesRequestBuilder extends AbstractRequestBuilder {


    public KotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                          OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> customizers,
                                          Optional<List<ParameterCustomizer>> parameterCustomizers) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder, customizers, parameterCustomizers);
    }

    static {
        PARAM_TYPES_TO_IGNORE.add(Continuation.class);
    }
}
