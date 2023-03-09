package org.springdoc.security.oauth2;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;

/**
 * @author bnasslahsen
 */
@Schema(name = "OAuth2TokenIntrospection")
public class SpringDocOAuth2TokenIntrospection implements OAuth2TokenIntrospectionClaimAccessor {

	@Override
	public Map<String, Object> getClaims() {
		return null;
	}

	@Override
	public <T> T getClaim(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaim(claim);
	}

	@Override
	public boolean hasClaim(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.hasClaim(claim);
	}

	@Override
	public Boolean containsClaim(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.containsClaim(claim);
	}

	@Override
	public String getClaimAsString(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsString(claim);
	}

	@Override
	public Boolean getClaimAsBoolean(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsBoolean(claim);
	}

	@Override
	public Instant getClaimAsInstant(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsInstant(claim);
	}

	@Override
	public URL getClaimAsURL(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsURL(claim);
	}

	@Override
	public Map<String, Object> getClaimAsMap(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsMap(claim);
	}

	@Override
	public List<String> getClaimAsStringList(String claim) {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClaimAsStringList(claim);
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ACTIVE)
	public boolean isActive() {
		return OAuth2TokenIntrospectionClaimAccessor.super.isActive();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.USERNAME)
	public String getUsername() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getUsername();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.CLIENT_ID)
	public String getClientId() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getClientId();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SCOPE)
	public List<String> getScopes() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getScopes();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.TOKEN_TYPE)
	public String getTokenType() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getTokenType();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.EXP)
	public Instant getExpiresAt() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getExpiresAt();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.IAT)
	public Instant getIssuedAt() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getIssuedAt();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.NBF)
	public Instant getNotBefore() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getNotBefore();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SUB)
	public String getSubject() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getSubject();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.AUD)
	public List<String> getAudience() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getAudience();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ISS)
	public URL getIssuer() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getIssuer();
	}

	@Override
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.JTI)
	public String getId() {
		return OAuth2TokenIntrospectionClaimAccessor.super.getId();
	}
}
