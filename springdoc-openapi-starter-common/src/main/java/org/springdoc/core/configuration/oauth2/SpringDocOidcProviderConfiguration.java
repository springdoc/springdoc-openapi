package org.springdoc.core.configuration.oauth2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderMetadataClaimNames;

/**
 * The type Spring doc OpenID Provider Configuration
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/OidcProviderConfiguration.java">OidcProviderConfiguration</a>
 * @author yuta.saito
 */
@Schema(name = "OidcProviderConfiguration")
public interface SpringDocOidcProviderConfiguration {
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.ISSUER)
	String issuer();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT)
	String authorizationEndpoint();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT)
	String tokenEndpoint();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> tokenEndpointAuthMethodsSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI)
	String jwksUri();

	@JsonProperty(OidcProviderMetadataClaimNames.USER_INFO_ENDPOINT)
	String userInfoEndpoint();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED)
	List<String> responseTypesSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED)
	List<String> grantTypesSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT)
	String revocationEndpoint();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> revocationEndpointAuthMethodsSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT)
	String introspectionEndpoint();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> introspectionEndpointAuthMethodsSupported();

	@JsonProperty(OidcProviderMetadataClaimNames.SUBJECT_TYPES_SUPPORTED)
	String subjectTypesSupported();

	@JsonProperty(OidcProviderMetadataClaimNames.ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED)
	String idTokenSigningAlgValuesSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.SCOPES_SUPPORTED)
	String scopeSupported();

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
	List<String> codeChallengeMethodsSupported();
}
