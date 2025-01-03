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
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderMetadataClaimNames;

/**
 * The type Spring doc OpenID Provider Configuration
 *
 * @author yuta.saito
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/OidcProviderConfiguration.java">OidcProviderConfiguration</a>
 */
@Schema(name = "OidcProviderConfiguration")
public interface SpringDocOidcProviderConfiguration {
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
	 * User info endpoint string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcProviderMetadataClaimNames.USER_INFO_ENDPOINT)
	String userInfoEndpoint();

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
	 * Subject types supported string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcProviderMetadataClaimNames.SUBJECT_TYPES_SUPPORTED)
	String subjectTypesSupported();

	/**
	 * Id token signing alg values supported string.
	 *
	 * @return the string
	 */
	@JsonProperty(OidcProviderMetadataClaimNames.ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED)
	String idTokenSigningAlgValuesSupported();

	/**
	 * Scope supported string.
	 *
	 * @return the string
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.SCOPES_SUPPORTED)
	String scopeSupported();

	/**
	 * Code challenge methods supported list.
	 *
	 * @return the list
	 */
	@JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
	List<String> codeChallengeMethodsSupported();
}
