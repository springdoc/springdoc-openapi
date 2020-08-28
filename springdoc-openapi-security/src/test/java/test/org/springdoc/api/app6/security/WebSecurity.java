package test.org.springdoc.api.app6.security;

import org.springdoc.core.SpringDocConfigProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@Order(200)
public class WebSecurity extends WebSecurityConfigurerAdapter {


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


	@Override
	protected void configure(HttpSecurity http)
			throws Exception {
		String apiDocsPath = configProperties.getApiDocs().getPath();
		http.cors()
				.and()
				.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers(apiDocsPath + "/**")
				.permitAll()
				.antMatchers(apiDocsPath.substring(0, apiDocsPath.lastIndexOf("/") + 1) + "api-docs.yaml")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.exceptionHandling()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), lifetime, key))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), key))
				// this disables session creation on Spring Security
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}


	@Override
	public void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}


	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();

		configuration.addExposedHeader(HeaderString);
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}