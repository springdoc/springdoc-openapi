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

import org.springframework.security.oauth2.server.authorization.oidc.OidcClientMetadataClaimNames;

/**
 * The type Spring doc OpenID Client Registration Request
 *
 * @author yuta.saito
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/OidcClientRegistration.java">OidcClientRegistration</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/http/converter/OidcClientRegistrationHttpMessageConverter.java">OidcClientRegistrationHttpMessageConverter</a>
 */
@Schema(name = "ClientRegistrationResponse")
public interface SpringDocOidcClientRegistrationResponse {
	/**
	 * Client id string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_ID)
	String clientId();

	/**
	 * Client id issued at long.
	 *
	 * @return the long
	 */
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_ID_ISSUED_AT)
	long clientIdIssuedAt();

	/**
	 * Client secret string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_SECRET)
	String clientSecret();

	/**
	 * Client secret expires at long.
	 *
	 * @return the long
	 */
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_SECRET_EXPIRES_AT)
	long CLIENT_SECRET_EXPIRES_AT();

	/**
	 * Client name string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.CLIENT_NAME)
	String clientName();

	/**
	 * Redirect uris list.
	 *
	 * @return the list
	 */
	@JsonProperty(OidcClientMetadataClaimNames.REDIRECT_URIS)
	List<String> redirectUris();

	/**
	 * Token endpoint authentication method string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHOD)
	String tokenEndpointAuthenticationMethod();

	/**
	 * Token endpoint authentication signing algorithm string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.TOKEN_ENDPOINT_AUTH_SIGNING_ALG)
	String tokenEndpointAuthenticationSigningAlgorithm();

	/**
	 * Grant types list.
	 *
	 * @return the list
	 */
	@JsonProperty(OidcClientMetadataClaimNames.GRANT_TYPES)
	List<String> grantTypes();

	/**
	 * Response type list.
	 *
	 * @return the list
	 */
	@JsonProperty(OidcClientMetadataClaimNames.RESPONSE_TYPES)
	List<String> responseType();

	/**
	 * Scopes string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.SCOPE)
	String scopes();

	/**
	 * Jwk set url string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.JWKS_URI)
	String jwkSetUrl();

	/**
	 * Id token signed response algorithm string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.ID_TOKEN_SIGNED_RESPONSE_ALG)
	String idTokenSignedResponseAlgorithm();

	/**
	 * Registration access token string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.REGISTRATION_ACCESS_TOKEN)
	String registrationAccessToken();

	/**
	 * Registration client url string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcClientMetadataClaimNames.REGISTRATION_CLIENT_URI)
	String registrationClientUrl();
}
