package org.springdoc.core;

import kotlin.coroutines.Continuation;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Optional;

public class KotlinCoroutinesReturnTypeParser implements ReturnTypeParser {

    @Override
    public Type getReturnType(Method method) {
        Type returnType = Object.class;
        Optional<Parameter> continuationParameter = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.getType().getCanonicalName().equals(Continuation.class.getCanonicalName()))
                .findFirst();
        if (continuationParameter.isPresent()) {
            Type continuationType = continuationParameter.get().getParameterizedType();
            if (continuationType instanceof ParameterizedType) {
                Type actualTypeArguments = ((ParameterizedType) continuationType).getActualTypeArguments()[0];
                if (actualTypeArguments instanceof WildcardType) {
                    returnType = ((WildcardType) actualTypeArguments).getLowerBounds()[0];
                }
            }
        }
        return returnType;
    }
}
