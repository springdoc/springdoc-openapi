package org.springdoc.core.configuration.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Spring doc o auth 2 token.
 *
 * @see <a href="https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-core/src/main/java/org/springframework/security/oauth2/core/endpoint/DefaultOAuth2AccessTokenResponseMapConverter.java">DefaultOAuth2AccessTokenResponseMapConverter</a>
 * @author yuta.saito
 */
@Schema(name = "OAuth2Token")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SpringDocOAuth2Token {
	String getAccessToken();

	String getTokenType();

	long getExpiresIn();

	String getScope();

	String getRefreshToken();
}
