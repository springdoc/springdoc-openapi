package org.springdoc.core.configuration;

import java.util.Optional;

import org.springframework.security.web.SecurityFilterChain;

/**
 * The type Spring doc security o auth 2 endpoint utils.
 *
 * @param <T> the type parameter
 * @author bnasslahsen
 */
public final class SpringDocSecurityOAuth2EndpointUtils<T> {

	/**
	 * The Oauth 2 endpoint filter.
	 */
	private T oauth2EndpointFilter;

	/**
	 * Instantiates a new Spring doc security o auth 2 endpoint utils.
	 *
	 * @param oauth2EndpointFilter the oauth 2 endpoint filter
	 */
	public SpringDocSecurityOAuth2EndpointUtils(T oauth2EndpointFilter) {
		this.oauth2EndpointFilter = oauth2EndpointFilter;
	}

	/**
	 * Find endpoint object.
	 *
	 * @param filterChain the filter chain
	 * @return the object
	 */
	public Object findEndpoint(SecurityFilterChain filterChain) {
		Optional<?> oAuth2EndpointFilterOptional =
				 filterChain.getFilters().stream()
				   .filter(((Class <?>) oauth2EndpointFilter)::isInstance)
				   .map(((Class <?>) oauth2EndpointFilter)::cast)
				   .findAny();
		return oAuth2EndpointFilterOptional.orElse(null);
	}
}
