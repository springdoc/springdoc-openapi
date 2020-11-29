package org.springdoc.webflux.ui;

import java.net.URI;
import java.util.Map;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerWelcome;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.SWAGGER_UI_URL;

public abstract class SwaggerWelcomeCommon extends AbstractSwaggerWelcome {

	/**
	 * The Web jars prefix url.
	 */
	protected String webJarsPrefixUrl;

	/**
	 * The Oauth prefix.
	 */
	protected UriComponentsBuilder oauthPrefix;

	/**
	 * Instantiates a new Abstract swagger welcome.
	 *  @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public SwaggerWelcomeCommon(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.webJarsPrefixUrl = springDocConfigProperties.getWebjars().getPrefix();
	}

	protected Mono<Void> redirectToUi(ServerHttpRequest request, ServerHttpResponse response) {
		String contextPath = this.fromCurrentContextPath(request);
		String sbUrl = this.buildUrl(contextPath, swaggerUiConfigParameters.getUiRootPath() + springDocConfigProperties.getWebjars().getPrefix() + SWAGGER_UI_URL);
		UriComponentsBuilder uriBuilder = getUriComponentsBuilder(sbUrl);
		response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
		response.getHeaders().setLocation(URI.create(uriBuilder.build().encode().toString()));
		return response.setComplete();
	}

	protected Map<String, Object> getSwaggerUiConfig(ServerHttpRequest request) {
		this.fromCurrentContextPath(request);
		return swaggerUiConfigParameters.getConfigParameters();
	}


	@Override
	protected void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder) {
		if (oauthPrefix == null && !swaggerUiConfigParameters.isValidUrl(swaggerUiConfigParameters.getOauth2RedirectUrl())) {
			this.oauthPrefix = uriComponentsBuilder.path(swaggerUiConfigParameters.getUiRootPath()).path(webJarsPrefixUrl);
			swaggerUiConfigParameters.setOauth2RedirectUrl(this.oauthPrefix.path(swaggerUiConfigParameters.getOauth2RedirectUrl()).build().toString());
		}
	}

	/**
	 * From current context path string.
	 *
	 * @param request the request
	 * @return the string
	 */
	private String fromCurrentContextPath(ServerHttpRequest request) {
		String contextPath = request.getPath().contextPath().value();
		String url = UriComponentsBuilder.fromHttpRequest(request).toUriString();
		url = url.replace(request.getPath().toString(), "");
		buildConfigUrl(contextPath, UriComponentsBuilder.fromUriString(url));
		return contextPath;
	}

}
