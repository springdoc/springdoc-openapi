package org.springdoc.subclass;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.AbstractParameterBuilder;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBodyBuilder;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Set;

/**
 * Class which sub class AbstractRequestBuilder in a different package and makes sure base class access is not changed. .
 */
public class RequestBuilder extends AbstractRequestBuilder {

    private static final Set<Class<?>> CLASSES_TO_IGNORE = Collections.emptySet();
    private static final Set<Class<? extends Annotation>> ANNOTATIONS_TO_IGNORE = Collections.emptySet();

    protected RequestBuilder(AbstractParameterBuilder parameterBuilder,
                             RequestBodyBuilder requestBodyBuilder,
                             OperationBuilder operationBuilder) {
        super(parameterBuilder, requestBodyBuilder, operationBuilder);
    }

    @Override
    protected boolean isParamTypeToIgnore(Class<?> paramType) {
        return CLASSES_TO_IGNORE.contains(paramType);
    }

    @Override
    protected Operation customiseOperation(Operation operation, HandlerMethod handlerMethod) {
        return operation;
    }

    @Override
    protected boolean isParamToIgnore(Parameter parameter) {
        return hasAnnotationToIgnore(parameter) || super.isParamToIgnore(parameter);
    }

    private boolean hasAnnotationToIgnore(Parameter parameter) {
        return ANNOTATIONS_TO_IGNORE.stream().anyMatch(parameter::isAnnotationPresent);
    }

}
