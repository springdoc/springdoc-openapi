package org.springdoc.security.oauth2;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimAccessor;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;

/**
 * @author bnasslahsen
 */
@Schema(name = "OAuth2AuthorizationServerMetadata")
public class SpringDocOAuth2AuthorizationServerMetadata implements OAuth2AuthorizationServerMetadataClaimAccessor {


	@Override
	public Map<String, Object> getClaims() {
		return null;
	}

	@Override
	public <T> T getClaim(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaim(claim);
	}

	@Override
	public boolean hasClaim(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.hasClaim(claim);
	}

	@Override
	public Boolean containsClaim(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.containsClaim(claim);
	}

	@Override
	public String getClaimAsString(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsString(claim);
	}

	@Override
	public Boolean getClaimAsBoolean(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsBoolean(claim);
	}

	@Override
	public Instant getClaimAsInstant(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsInstant(claim);
	}

	@Override
	public URL getClaimAsURL(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsURL(claim);
	}

	@Override
	public Map<String, Object> getClaimAsMap(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsMap(claim);
	}

	@Override
	public List<String> getClaimAsStringList(String claim) {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClaimAsStringList(claim);
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.ISSUER)
	public URL getIssuer() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getIssuer();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT)
	public URL getAuthorizationEndpoint() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getAuthorizationEndpoint();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT)
	public URL getTokenEndpoint() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenEndpoint();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED)
	public List<String> getTokenEndpointAuthenticationMethods() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenEndpointAuthenticationMethods();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI)
	public URL getJwkSetUrl() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getJwkSetUrl();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.SCOPES_SUPPORTED)
	public List<String> getScopes() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getScopes();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED)
	public List<String> getResponseTypes() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getResponseTypes();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED)
	public List<String> getGrantTypes() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getGrantTypes();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT)
	public URL getTokenRevocationEndpoint() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenRevocationEndpoint();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	public List<String> getTokenRevocationEndpointAuthenticationMethods() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenRevocationEndpointAuthenticationMethods();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT)
	public URL getTokenIntrospectionEndpoint() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenIntrospectionEndpoint();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	public List<String> getTokenIntrospectionEndpointAuthenticationMethods() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getTokenIntrospectionEndpointAuthenticationMethods();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REGISTRATION_ENDPOINT)
	public URL getClientRegistrationEndpoint() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getClientRegistrationEndpoint();
	}

	@Override
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
	public List<String> getCodeChallengeMethods() {
		return OAuth2AuthorizationServerMetadataClaimAccessor.super.getCodeChallengeMethods();
	}
}
