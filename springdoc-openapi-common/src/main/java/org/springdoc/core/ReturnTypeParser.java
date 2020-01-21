package org.springdoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

interface ReturnTypeParser {
    default Type getReturnType(Method method) {
        return method.getGenericReturnType();
    }
}

class GenericReturnTypeParser implements ReturnTypeParser{}
