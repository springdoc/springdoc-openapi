package org.springdoc.core.configuration.oauth2;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.server.authorization.oidc.OidcClientMetadataClaimNames;

/**
 * The type Spring doc OpenID Client Registration Request
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/OidcClientRegistration.java">OidcClientRegistration</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/http/converter/OidcClientRegistrationHttpMessageConverter.java">OidcClientRegistrationHttpMessageConverter</a>
 * @author yuta.saito
 */
@Schema(name = "ClientRegistrationRequest")
public interface SpringDocOidcClientRegistrationRequest {
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_ID)
	String clientId();

	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_ID_ISSUED_AT)
	Instant clientIdIssuedAt();

	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_SECRET)
	String clientSecret();

	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_SECRET_EXPIRES_AT)
	Instant CLIENT_SECRET_EXPIRES_AT();

	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_NAME)
	String clientName();

	@JsonProperty(OidcClientMetadataClaimNames.REDIRECT_URIS)
	List<String> redirectUris();

	@JsonProperty(OidcClientMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHOD)
	String tokenEndpointAuthenticationMethod();

	@JsonProperty(OidcClientMetadataClaimNames.TOKEN_ENDPOINT_AUTH_SIGNING_ALG)
	String tokenEndpointAuthenticationSigningAlgorithm();

	@JsonProperty(OidcClientMetadataClaimNames.GRANT_TYPES)
	List<String> grantTypes();

	@JsonProperty(OidcClientMetadataClaimNames.RESPONSE_TYPES)
	List<String> responseType();

	@JsonProperty(OidcClientMetadataClaimNames.SCOPE)
	String scopes();

	@JsonProperty(OidcClientMetadataClaimNames.JWKS_URI)
	String jwkSetUrl();

	@JsonProperty(OidcClientMetadataClaimNames.ID_TOKEN_SIGNED_RESPONSE_ALG)
	String idTokenSignedResponseAlgorithm();
}
