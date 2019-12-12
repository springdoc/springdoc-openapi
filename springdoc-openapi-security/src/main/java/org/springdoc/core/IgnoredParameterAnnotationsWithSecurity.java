package org.springdoc.core;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

@Primary
@Component
public class IgnoredParameterAnnotationsWithSecurity implements IgnoredParameterAnnotations {

    @Override
    public boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter) {
        return parameter.isAnnotationPresent(AuthenticationPrincipal.class);
    }
}
