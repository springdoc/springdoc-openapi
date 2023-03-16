package org.springdoc.security.oauth2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;

/**
 * The type Spring doc o auth 2 token introspection.
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/http/converter/OAuth2TokenIntrospectionHttpMessageConverter.java">OAuth2TokenIntrospectionHttpMessageConverter</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2TokenIntrospection.java">OAuth2TokenIntrospection</a>
 * @author bnasslahsen
 * @author yuta.saito
 */
@Schema(name = "OAuth2TokenIntrospection")
public interface SpringDocOAuth2TokenIntrospection {
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ACTIVE)
	boolean getActive();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SCOPE)
	String getScope();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.CLIENT_ID)
	String getClientId();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.USERNAME)
	String getUsername();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.TOKEN_TYPE)
	String getTokenType();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.EXP)
	long getExpiresAt();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.IAT)
	long getIssueAt();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.NBF)
	long getNotBefore();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SUB)
	String getSubject();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.AUD)
	List<String> getAudience();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ISS)
	String getIssuer();

	@JsonProperty(OAuth2TokenIntrospectionClaimNames.JTI)
	String getId();
}
