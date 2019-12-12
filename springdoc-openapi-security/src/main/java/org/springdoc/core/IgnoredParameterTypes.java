package org.springdoc.core;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class IgnoredParameterTypes {

    public IgnoredParameterTypes() {
        AbstractRequestBuilder.PARAM_TYPES_TO_IGNORE.add(Authentication.class);
    }

}
