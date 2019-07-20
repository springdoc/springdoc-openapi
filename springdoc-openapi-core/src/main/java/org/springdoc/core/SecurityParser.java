package org.springdoc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class SecurityParser {

	private SecurityParser() {
		super();
	}

	public static class SecuritySchemePair {
		String key;
		SecurityScheme securityScheme;
	}

	public static Optional<List<SecurityRequirement>> getSecurityRequirements(
			io.swagger.v3.oas.annotations.security.SecurityRequirement[] securityRequirementsApi) {
		if (securityRequirementsApi == null || securityRequirementsApi.length == 0) {
			return Optional.empty();
		}
		List<SecurityRequirement> securityRequirements = new ArrayList<>();
		for (io.swagger.v3.oas.annotations.security.SecurityRequirement securityRequirementApi : securityRequirementsApi) {
			if (StringUtils.isBlank(securityRequirementApi.name())) {
				continue;
			}
			SecurityRequirement securityRequirement = new SecurityRequirement();
			if (securityRequirementApi.scopes().length > 0) {
				securityRequirement.addList(securityRequirementApi.name(),
						Arrays.asList(securityRequirementApi.scopes()));
			} else {
				securityRequirement.addList(securityRequirementApi.name());
			}
			securityRequirements.add(securityRequirement);
		}
		if (securityRequirements.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(securityRequirements);
	}

	public static Optional<SecuritySchemePair> getSecurityScheme(
			io.swagger.v3.oas.annotations.security.SecurityScheme securityScheme) {
		if (securityScheme == null) {
			return Optional.empty();
		}
		String key = null;
		SecurityScheme securitySchemeObject = new SecurityScheme();

		if (StringUtils.isNotBlank(securityScheme.in().toString())) {
			securitySchemeObject.setIn(getIn(securityScheme.in().toString()));
		}
		if (StringUtils.isNotBlank(securityScheme.type().toString())) {
			securitySchemeObject.setType(getType(securityScheme.type().toString()));
		}

		if (StringUtils.isNotBlank(securityScheme.openIdConnectUrl())) {
			securitySchemeObject.setOpenIdConnectUrl(securityScheme.openIdConnectUrl());
		}
		if (StringUtils.isNotBlank(securityScheme.scheme())) {
			securitySchemeObject.setScheme(securityScheme.scheme());
		}

		if (StringUtils.isNotBlank(securityScheme.bearerFormat())) {
			securitySchemeObject.setBearerFormat(securityScheme.bearerFormat());
		}
		if (StringUtils.isNotBlank(securityScheme.description())) {
			securitySchemeObject.setDescription(securityScheme.description());
		}
		if (StringUtils.isNotBlank(securityScheme.paramName())) {
			securitySchemeObject.setName(securityScheme.paramName());
		}
		if (StringUtils.isNotBlank(securityScheme.ref())) {
			securitySchemeObject.set$ref(securityScheme.ref());
		}
		if (StringUtils.isNotBlank(securityScheme.name())) {
			key = securityScheme.name();
		}

		if (securityScheme.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(securityScheme.extensions());
			for (Map.Entry<String, Object> entry : extensions.entrySet()) {
				securitySchemeObject.addExtension(entry.getKey(), entry.getValue());
			}
		}

		getOAuthFlows(securityScheme.flows()).ifPresent(securitySchemeObject::setFlows);

		SecuritySchemePair result = new SecuritySchemePair();
		result.key = key;
		result.securityScheme = securitySchemeObject;
		return Optional.of(result);
	}

	public static Optional<OAuthFlows> getOAuthFlows(io.swagger.v3.oas.annotations.security.OAuthFlows oAuthFlows) {
		if (isEmpty(oAuthFlows)) {
			return Optional.empty();
		}
		OAuthFlows oAuthFlowsObject = new OAuthFlows();
		if (oAuthFlows.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(oAuthFlows.extensions());
			if (extensions != null) {
				for (Map.Entry<String, Object> entry : extensions.entrySet()) {
					oAuthFlowsObject.addExtension(entry.getKey(), entry.getValue());
				}
			}
		}

		getOAuthFlow(oAuthFlows.authorizationCode()).ifPresent(oAuthFlowsObject::setAuthorizationCode);
		getOAuthFlow(oAuthFlows.clientCredentials()).ifPresent(oAuthFlowsObject::setClientCredentials);
		getOAuthFlow(oAuthFlows.implicit()).ifPresent(oAuthFlowsObject::setImplicit);
		getOAuthFlow(oAuthFlows.password()).ifPresent(oAuthFlowsObject::setPassword);
		return Optional.of(oAuthFlowsObject);
	}

	public static Optional<OAuthFlow> getOAuthFlow(io.swagger.v3.oas.annotations.security.OAuthFlow oAuthFlow) {
		if (isEmpty(oAuthFlow)) {
			return Optional.empty();
		}
		OAuthFlow oAuthFlowObject = new OAuthFlow();
		if (StringUtils.isNotBlank(oAuthFlow.authorizationUrl())) {
			oAuthFlowObject.setAuthorizationUrl(oAuthFlow.authorizationUrl());
		}
		if (StringUtils.isNotBlank(oAuthFlow.refreshUrl())) {
			oAuthFlowObject.setRefreshUrl(oAuthFlow.refreshUrl());
		}
		if (StringUtils.isNotBlank(oAuthFlow.tokenUrl())) {
			oAuthFlowObject.setTokenUrl(oAuthFlow.tokenUrl());
		}
		if (oAuthFlow.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(oAuthFlow.extensions());
			if (extensions != null) {
				for (Map.Entry<String, Object> entry : extensions.entrySet()) {
					oAuthFlowObject.addExtension(entry.getKey(), entry.getValue());
				}
			}
		}

		getScopes(oAuthFlow.scopes()).ifPresent(oAuthFlowObject::setScopes);
		return Optional.of(oAuthFlowObject);
	}

	public static Optional<Scopes> getScopes(OAuthScope[] scopes) {
		if (isEmpty(scopes)) {
			return Optional.empty();
		}
		Scopes scopesObject = new Scopes();

		for (OAuthScope scope : scopes) {
			scopesObject.addString(scope.name(), scope.description());
		}
		return Optional.of(scopesObject);
	}

	private static SecurityScheme.In getIn(String value) {
		return Arrays.stream(SecurityScheme.In.values()).filter(i -> i.toString().equals(value)).findFirst()
				.orElse(null);
	}

	private static SecurityScheme.Type getType(String value) {
		return Arrays.stream(SecurityScheme.Type.values()).filter(i -> i.toString().equals(value)).findFirst()
				.orElse(null);
	}

	private static boolean isEmpty(io.swagger.v3.oas.annotations.security.OAuthFlows oAuthFlows) {
		boolean result = false;
		if (oAuthFlows == null) {
			result = true;
		} else if (!isEmpty(oAuthFlows.implicit())) {
			result = false;
		} else if (!isEmpty(oAuthFlows.authorizationCode())) {
			result = false;
		} else if (!isEmpty(oAuthFlows.clientCredentials())) {
			result = false;
		} else if (!isEmpty(oAuthFlows.password())) {
			result = false;
		} else if (oAuthFlows.extensions().length > 0) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	private static boolean isEmpty(io.swagger.v3.oas.annotations.security.OAuthFlow oAuthFlow) {
		boolean result = false;
		if (oAuthFlow == null) {
			result = true;
		}
		else if (!StringUtils.isBlank(oAuthFlow.authorizationUrl())) {
			result = false;
		}
		else if (!StringUtils.isBlank(oAuthFlow.refreshUrl())) {
			result = false;
		}
		else if (!StringUtils.isBlank(oAuthFlow.tokenUrl())) {
			result = false;
		}
		else if (!isEmpty(oAuthFlow.scopes())) {
			result = false;
		}
		else if (oAuthFlow.extensions().length > 0) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	private static boolean isEmpty(OAuthScope[] scopes) {
		if (scopes == null || scopes.length == 0) {
			return true;
		}
		return false;
	}

}