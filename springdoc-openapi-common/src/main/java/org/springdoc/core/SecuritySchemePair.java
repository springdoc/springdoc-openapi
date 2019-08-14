package org.springdoc.core;

import io.swagger.v3.oas.models.security.SecurityScheme;

public class SecuritySchemePair {

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

	public void setKey(String key) {
		this.key = key;
	}

	public SecurityScheme getSecurityScheme() {
		return securityScheme;
	}

	public void setSecurityScheme(SecurityScheme securityScheme) {
		this.securityScheme = securityScheme;
	}

}
