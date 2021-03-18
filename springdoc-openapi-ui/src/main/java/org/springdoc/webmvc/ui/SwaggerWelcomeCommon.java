package org.springdoc.webmvc.ui;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerWelcome;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.SWAGGER_UI_URL;

/**
 * The type Swagger welcome common.
 * @author bnasslashen
 */
public abstract class SwaggerWelcomeCommon extends AbstractSwaggerWelcome {
	/**
	 * Instantiates a new Abstract swagger welcome.
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public SwaggerWelcomeCommon(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,
			SwaggerUiConfigParameters swaggerUiConfigParameters) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
	}

	/**
	 * Redirect to ui response entity.
	 *
	 * @param request the request
	 * @return the response entity
	 */
	protected ResponseEntity<Void> redirectToUi(HttpServletRequest request) {
		buildFromCurrentContextPath(request);
		String sbUrl = contextPath + swaggerUiConfigParameters.getUiRootPath() + SWAGGER_UI_URL;
		UriComponentsBuilder uriBuilder = getUriComponentsBuilder(sbUrl);

		// forward all queryParams from original request
		request.getParameterMap().forEach(uriBuilder::queryParam);

		return ResponseEntity.status(HttpStatus.FOUND)
				.location(uriBuilder.build().encode().toUri())
				.build();
	}

	/**
	 * Openapi json map.
	 *
	 * @param request the request
	 * @return the map
	 */
	protected Map<String, Object> openapiJson(HttpServletRequest request) {
		buildFromCurrentContextPath(request);
		return swaggerUiConfigParameters.getConfigParameters();
	}

	@Override
	protected void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder) {
		if (!swaggerUiConfigParameters.isValidUrl(swaggerUiConfigParameters.getOauth2RedirectUrl()) || springDocConfigProperties.isCacheDisabled())
			swaggerUiConfigParameters.setOauth2RedirectUrl(uriComponentsBuilder
					.path(swaggerUiConfigParameters.getUiRootPath())
					.path(getOauth2RedirectUrl()).build().toString());
	}

	/**
	 * From current context path.
	 *
	 * @param request the request
	 */
	private void buildFromCurrentContextPath(HttpServletRequest request) {
		contextPath = request.getContextPath();
		buildConfigUrl(ServletUriComponentsBuilder.fromCurrentContextPath());
	}
}
