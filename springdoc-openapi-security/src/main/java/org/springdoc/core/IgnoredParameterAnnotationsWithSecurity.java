package org.springdoc.core;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

public class IgnoredParameterAnnotationsWithSecurity implements IgnoredParameterAnnotations {

    @Override
    public boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter) {
        return parameter.isAnnotationPresent(AuthenticationPrincipal.class);
    }
}
