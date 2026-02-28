/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v30.app6.security;

import org.springdoc.core.properties.SpringDocConfigProperties;
import test.org.springdoc.api.v31.app6.security.JWTAuthenticationFilter;
import test.org.springdoc.api.v31.app6.security.JWTAuthorizationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

@Configuration
@EnableWebSecurity
@Order(200)
public class WebSecurity {


	public static final String TokenPrefix = "Bearer ";


	public static final String HeaderString = "Authorization";


	private final UserDetailsService userDetailsService;


	@Autowired
	SpringDocConfigProperties configProperties;


	private long lifetime = 123456789L;


	private String key =
			"YRv13MrZah/rHJPMGIN6AjdjB09F9gpIC7i9mdFwdIDZ296doUg/nhG/mQ/CnlxPNtcWR6z6RCKtW5cCspGM9w==";


	public WebSecurity(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;

	}


	@Bean
	public SecurityFilterChain securityWebFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		String apiDocsPath = configProperties.getApiDocs().getPath();
		String apiDocsYaml = apiDocsPath.substring(0, apiDocsPath.lastIndexOf('/') + 1) + "api-docs.yaml";

		return http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(apiDocsPath + ALL_PATTERN).permitAll()
						.requestMatchers(apiDocsYaml).permitAll()
						.anyRequest().authenticated()
				)
				.addFilter(new JWTAuthenticationFilter(authenticationManager, lifetime, key))
				.addFilter(new JWTAuthorizationFilter(authenticationManager, key))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}



	@Autowired
	public void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}


	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();

		configuration.addExposedHeader(HeaderString);
		source.registerCorsConfiguration(ALL_PATTERN, configuration);

		return source;
	}

}