package test.org.springdoc.api.app6.security;

import java.io.IOException;
import java.util.ArrayList;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


	public static final String AUTH_ERROR_ATTRIBUTE = "authError";


	private final String key;


	public JWTAuthorizationFilter(AuthenticationManager authManager, String key) {
		super(authManager);
		this.key = key;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(WebSecurity.HeaderString);

		if (header == null || !header.startsWith(WebSecurity.TokenPrefix)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(req, res);
	}


	/**
	 * Check the validity of the JWT (JWS, more precisely) as submitted via the
	 * {@link HttpServletRequest}.
	 *
	 * @param request the {@link HttpServletRequest} containing a JWS.
	 * @return a {@link UsernamePasswordAuthenticationToken} if the JWS is
	 *   valid, {@code null} otherwise.
	 */

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(WebSecurity.HeaderString);
		if (token != null) {
			String user = null;
			try {
				user = Jwts.parser()
						.setSigningKey(key)
						.parseClaimsJws(token.replace(WebSecurity.TokenPrefix, ""))
						.getBody()
						.getSubject();
			}
			catch (ExpiredJwtException e) {
				request.setAttribute(AUTH_ERROR_ATTRIBUTE, e.getMessage());
			}
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}

}