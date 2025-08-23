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

package test.org.springdoc.api.v31.app6.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springdoc.core.utils.SpringDocUtils.cloneViaJson;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


	private final AuthenticationManager authenticationManager;


	private final long lifetime;


	private final String key;


	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, long lifetime,
			String key) {
		this.authenticationManager = authenticationManager;
		this.lifetime = lifetime;
		this.key = key;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			UserCredentials credentials = cloneViaJson(req.getInputStream(), UserCredentials.class,new ObjectMapper());
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getUsername(),
							credentials.getPassword(), new ArrayList<>()));

		}
		catch (IOException e) {
			throw new InternalAuthenticationServiceException("Error processing credentials", e);
		}
	}


	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			FilterChain chain, Authentication auth)
			throws IOException, ServletException {
		Date notBefore = new Date();
		Date expirationDate = new Date(notBefore.getTime() + lifetime);

		String token = Jwts.builder()
				.setClaims(new HashMap<>())
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setNotBefore(notBefore)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
		res.addHeader(WebSecurity.HeaderString, WebSecurity.TokenPrefix + token);

	}


	private static class UserCredentials {


		private String username;


		private String password;


		String getUsername() {
			return username;
		}


		public void setUsername(String username) {
			this.username = username;
		}


		String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}

	}

}