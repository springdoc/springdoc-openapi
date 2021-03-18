package org.springdoc.webmvc.ui;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springdoc.core.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.core.Constants.DEFAULT_SWAGGER_UI_ACTUATOR_PATH;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger actuator welcome.
 */
@ControllerEndpoint(id = DEFAULT_SWAGGER_UI_ACTUATOR_PATH)
public class SwaggerWelcomeActuator extends SwaggerWelcomeCommon {

	/**
	 * The Web endpoint properties.
	 */
	private WebEndpointProperties webEndpointProperties;

	private static final String SWAGGER_CONFIG_ACTUATOR_URL = DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;

	/**
	 * Instantiates a new Swagger welcome.
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param webEndpointProperties the web endpoint properties
	 */
	public SwaggerWelcomeActuator(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, WebEndpointProperties webEndpointProperties) {
		super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters);
		this.webEndpointProperties = webEndpointProperties;
	}

	/**
	 * Redirect to ui string.
	 *
	 * @param request the request
	 * @return the string
	 */
	@Operation(hidden = true)
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	@Override
	public ResponseEntity<Void> redirectToUi(HttpServletRequest request) {
		return super.redirectToUi(request);
	}

	/**
	 * Openapi yaml map.
	 *
	 * @param request the request
	 * @return the map
	 */
	@Operation(hidden = true)
	@GetMapping(value = SWAGGER_CONFIG_ACTUATOR_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Override
	public Map<String, Object> openapiJson(HttpServletRequest request) {
		return super.openapiJson(request);
	}

	@Override
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(webEndpointProperties.getBasePath());
		calculateUiRootCommon(sbUrl, sbUrls);
	}

	@Override
	protected String buildApiDocUrl() {
		return buildUrl(contextPath + webEndpointProperties.getBasePath(), DEFAULT_API_DOCS_ACTUATOR_URL);
	}

	@Override
	protected String buildSwaggerConfigUrl() {
		return   contextPath + webEndpointProperties.getBasePath()
				+ DEFAULT_PATH_SEPARATOR + DEFAULT_SWAGGER_UI_ACTUATOR_PATH
				+ DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
	}

}
