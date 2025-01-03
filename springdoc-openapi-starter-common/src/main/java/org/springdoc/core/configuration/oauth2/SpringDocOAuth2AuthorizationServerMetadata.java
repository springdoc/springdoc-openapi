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

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;

/**
 * The type Spring doc o auth 2 authorization server metadata.
 *
 * @author bnasslahsen
 * @author yuta.saito
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2AuthorizationServerMetadata.java">OAuth2AuthorizationServerMetadata</a>
 */
@Schema(name = "OAuth2AuthorizationServerMetadata")
public interface SpringDocOAuth2AuthorizationServerMetadata {
	/**
	 * Issuer string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.ISSUER)
	String issuer();

	/**
	 * Authorization endpoint string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT)
	String authorizationEndpoint();

	/**
	 * Token endpoint string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT)
	String tokenEndpoint();

	/**
	 * Token endpoint auth methods supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> tokenEndpointAuthMethodsSupported();

	/**
	 * Jwks uri string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI)
	String jwksUri();

	/**
	 * Response types supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED)
	List<String> responseTypesSupported();

	/**
	 * Grant types supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED)
	List<String> grantTypesSupported();

	/**
	 * Revocation endpoint string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT)
	String revocationEndpoint();

	/**
	 * Revocation endpoint auth methods supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> revocationEndpointAuthMethodsSupported();

	/**
	 * Introspection endpoint string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT)
	String introspectionEndpoint();

	/**
	 * Introspection endpoint auth methods supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED)
	List<String> introspectionEndpointAuthMethodsSupported();

	/**
	 * Code challenge methods supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
	List<String> codeChallengeMethodsSupported();
}
