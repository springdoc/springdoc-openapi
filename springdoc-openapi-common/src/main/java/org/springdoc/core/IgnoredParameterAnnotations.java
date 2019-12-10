package org.springdoc.core;

public interface IgnoredParameterAnnotations {

    default boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter) {
        return false;
    }

}
