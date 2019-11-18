package org.springdoc.core;

import io.swagger.v3.oas.models.security.SecurityScheme;

class SecuritySchemePair {

    private String key;
    private SecurityScheme securityScheme;

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
