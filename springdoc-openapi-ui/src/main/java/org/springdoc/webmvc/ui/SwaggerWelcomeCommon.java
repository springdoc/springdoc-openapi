package org.springdoc.webmvc.ui;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerWelcome;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.SWAGGER_UI_URL;

public abstract class SwaggerWelcomeCommon extends AbstractSwaggerWelcome {
	/**
	 * Instantiates a new Abstract swagger welcome.
	 *  @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public SwaggerWelcomeCommon(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
	}

	protected String redirectToUi(HttpServletRequest request) {
		buildConfigUrl(request.getContextPath(), ServletUriComponentsBuilder.fromCurrentContextPath());
		String sbUrl =   swaggerUiConfigParameters.getUiRootPath() + SWAGGER_UI_URL;
		UriComponentsBuilder uriBuilder = getUriComponentsBuilder(sbUrl);

		// forward all queryParams from original request
		request.getParameterMap().forEach(uriBuilder::queryParam);

		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + uriBuilder.build().encode().toString();
	}

	protected Map<String, Object> openapiJson(HttpServletRequest request) {
		buildConfigUrl(request.getContextPath(), ServletUriComponentsBuilder.fromCurrentContextPath());
		return swaggerUiConfigParameters.getConfigParameters();
	}

	@Override
	protected void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder) {
		if (!swaggerUiConfigParameters.isValidUrl(swaggerUiConfigParameters.getOauth2RedirectUrl()))
			swaggerUiConfigParameters.setOauth2RedirectUrl(uriComponentsBuilder.path(swaggerUiConfigParameters.getUiRootPath()).path(swaggerUiConfigParameters.getOauth2RedirectUrl()).build().toString());
	}
}
