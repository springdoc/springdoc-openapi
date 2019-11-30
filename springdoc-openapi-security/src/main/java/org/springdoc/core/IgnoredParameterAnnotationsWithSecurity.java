package org.springdoc.core;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Primary
@Component
public class IgnoredParameterAnnotationsWithSecurity implements IgnoredParameterAnnotations {

    public boolean isAnnotationToIgnore(java.lang.reflect.Parameter parameter) {
        if (parameter.isAnnotationPresent(AuthenticationPrincipal.class)) {
            return true;
        }
        return false;
    }

}
