package org.springdoc.core;

import io.swagger.v3.oas.models.security.SecurityScheme;

class SecuritySchemePair {

    private final String key;
    private final SecurityScheme securityScheme;

    public SecuritySchemePair(String key, SecurityScheme securityScheme) {
        super();
        this.key = key;
        this.securityScheme = securityScheme;
    }

    public String getKey() {
        return key;
    }

    public SecurityScheme getSecurityScheme() {
        return securityScheme;
    }

}
