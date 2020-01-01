package org.springdoc.core;

import kotlin.coroutines.Continuation;
import org.springdoc.core.customizer.OperationCustomizer;
import org.springdoc.core.customizer.ParameterCustomizer;

import java.util.List;


public class KotlinCoroutinesRequestBuilder extends AbstractRequestBuilder {


    public KotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                          OperationBuilder operationBuilder, List<OperationCustomizer> customizers,
                                          List<ParameterCustomizer> parameterCustomizers) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder, customizers, parameterCustomizers);
    }

    static {
        PARAM_TYPES_TO_IGNORE.add(Continuation.class);
    }
}
