package org.springdoc.core.fn.builders;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public class SecurityRequirementBuilder {
	/**
	 * This name must correspond to a declared SecurityRequirement.
	 *
	 */
	private String name;

	/**
	 * If the security scheme is of type "oauth2" or "openIdConnect", then the value is a list of scope names required for the execution.
	 * For other security scheme types, the array must be empty.
	 *
	 */
	private String[] scopes = {};


	private SecurityRequirementBuilder() {
	}

	public static SecurityRequirementBuilder builder() {
		return new SecurityRequirementBuilder();
	}

	public SecurityRequirementBuilder name(String name) {
		this.name = name;
		return this;
	}

	public SecurityRequirementBuilder scopes(String[] scopes) {
		this.scopes = scopes;
		return this;
	}

	public SecurityRequirement build() {
		SecurityRequirement securityRequirement = new SecurityRequirement(){
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String[] scopes() {
				return scopes;
			}
		};
		return securityRequirement;
	}
}
