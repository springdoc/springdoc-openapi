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

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Spring doc o auth 2 token.
 *
 * @author yuta.saito
 * @see <a href="https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-core/src/main/java/org/springframework/security/oauth2/core/endpoint/DefaultOAuth2AccessTokenResponseMapConverter.java">DefaultOAuth2AccessTokenResponseMapConverter</a>
 */
@Schema(name = "OAuth2Token")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SpringDocOAuth2Token {
	/**
	 * Gets access token.
	 *
	 * @return the access token
	 */
	String getAccessToken();

	/**
	 * Gets token type.
	 *
	 * @return the token type
	 */
	String getTokenType();

	/**
	 * Gets expires in.
	 *
	 * @return the expires in
	 */
	long getExpiresIn();

	/**
	 * Gets scope.
	 *
	 * @return the scope
	 */
	String getScope();

	/**
	 * Gets refresh token.
	 *
	 * @return the refresh token
	 */
	String getRefreshToken();
}
