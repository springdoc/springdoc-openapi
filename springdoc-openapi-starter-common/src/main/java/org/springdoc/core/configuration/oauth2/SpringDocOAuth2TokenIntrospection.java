/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.configuration.oauth2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;

/**
 * The type Spring doc o auth 2 token introspection.
 *
 * @author bnasslahsen
 * @author yuta.saito
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/http/converter/OAuth2TokenIntrospectionHttpMessageConverter.java">OAuth2TokenIntrospectionHttpMessageConverter</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2TokenIntrospection.java">OAuth2TokenIntrospection</a>
 */
@Schema(name = "OAuth2TokenIntrospection")
public interface SpringDocOAuth2TokenIntrospection {
	/**
	 * Gets active.
	 *
	 * @return the active
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ACTIVE)
	boolean getActive();

	/**
	 * Gets scope.
	 *
	 * @return the scope
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SCOPE)
	String getScope();

	/**
	 * Gets client id.
	 *
	 * @return the client id
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.CLIENT_ID)
	String getClientId();

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.USERNAME)
	String getUsername();

	/**
	 * Gets token type.
	 *
	 * @return the token type
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.TOKEN_TYPE)
	String getTokenType();

	/**
	 * Gets expires at.
	 *
	 * @return the expires at
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.EXP)
	long getExpiresAt();

	/**
	 * Gets issue at.
	 *
	 * @return the issue at
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.IAT)
	long getIssueAt();

	/**
	 * Gets not before.
	 *
	 * @return the not before
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.NBF)
	long getNotBefore();

	/**
	 * Gets subject.
	 *
	 * @return the subject
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.SUB)
	String getSubject();

	/**
	 * Gets audience.
	 *
	 * @return the audience
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.AUD)
	List<String> getAudience();

	/**
	 * Gets issuer.
	 *
	 * @return the issuer
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.ISS)
	String getIssuer();

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	@JsonProperty(OAuth2TokenIntrospectionClaimNames.JTI)
	String getId();
}
