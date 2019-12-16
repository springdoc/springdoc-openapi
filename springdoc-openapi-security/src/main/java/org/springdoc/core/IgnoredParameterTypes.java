package org.springdoc.core;

import org.springframework.security.core.Authentication;

public class IgnoredParameterTypes {

    public IgnoredParameterTypes() {
        AbstractRequestBuilder.PARAM_TYPES_TO_IGNORE.add(Authentication.class);
    }

}
