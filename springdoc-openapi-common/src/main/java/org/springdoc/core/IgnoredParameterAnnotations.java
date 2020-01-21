package org.springdoc.core;

interface IgnoredParameterAnnotations {
    default boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter) {
        return false;
    }
}

class IgnoredParameterAnnotationsDefault implements IgnoredParameterAnnotations { }
