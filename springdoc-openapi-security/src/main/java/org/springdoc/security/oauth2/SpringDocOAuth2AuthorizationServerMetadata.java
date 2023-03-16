package org.springdoc.security.oauth2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;

/**
 * The type Spring doc o auth 2 authorization server metadata.
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2AuthorizationServerMetadata.java">OAuth2AuthorizationServerMetadata</a>
 * @author bnasslahsen
 * @author yuta.saito
 */
@Schema(name = "OAuth2AuthorizationServerMetadata")
public interface SpringDocOAuth2AuthorizationServerMetadata {
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

	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
	List<String> codeChallengeMethodsSupported();
}
