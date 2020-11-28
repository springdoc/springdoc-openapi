package org.springdoc.webmvc.ui;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerWelcome;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.core.Constants.DEFAULT_SWAGGER_UI_ACTUATOR_PATH;
import static org.springdoc.core.Constants.SWAGGER_UI_URL;
import static org.springdoc.core.Constants.SWAGGGER_CONFIG_FILE;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Swagger actuator welcome.
 */
@ControllerEndpoint(id = DEFAULT_SWAGGER_UI_ACTUATOR_PATH)
public class ActuatorSwaggerWelcome extends AbstractSwaggerWelcome {

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
	public ActuatorSwaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, WebEndpointProperties webEndpointProperties) {
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
	@GetMapping("/")
	public String redirectToUi(HttpServletRequest request) {
		buildConfigUrl(request.getContextPath(), ServletUriComponentsBuilder.fromCurrentContextPath());
		String sbUrl = swaggerUiConfigParameters.getUiRootPath() + SWAGGER_UI_URL;
		UriComponentsBuilder uriBuilder = getUriComponentsBuilder(sbUrl);
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + uriBuilder.build().encode().toString();
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
	public Map<String, Object> openapiJson(HttpServletRequest request) {
		buildConfigUrl(request.getContextPath(), ServletUriComponentsBuilder.fromCurrentContextPath());
		return swaggerUiConfigParameters.getConfigParameters();
	}

	@Override
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(webEndpointProperties.getBasePath());
		if (ArrayUtils.isNotEmpty(sbUrls))
			sbUrl = sbUrls[0];
		String swaggerPath = swaggerUiConfigParameters.getPath();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			sbUrl.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		swaggerUiConfigParameters.setUiRootPath(sbUrl.toString());
	}


	protected void buildConfigUrl(String contextPath, UriComponentsBuilder uriComponentsBuilder) {
		String apiDocsUrl = DEFAULT_API_DOCS_ACTUATOR_URL;
		if (StringUtils.isEmpty(swaggerUiConfig.getConfigUrl())) {
			String url = buildUrl(contextPath + webEndpointProperties.getBasePath(), apiDocsUrl);
			String swaggerConfigUrl = contextPath + webEndpointProperties.getBasePath()
					+ DEFAULT_PATH_SEPARATOR + DEFAULT_SWAGGER_UI_ACTUATOR_PATH
					+ DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;

			swaggerUiConfigParameters.setConfigUrl(swaggerConfigUrl);
			if (CollectionUtils.isEmpty(swaggerUiConfigParameters.getUrls())) {
				String swaggerUiUrl = swaggerUiConfig.getUrl();
				if (StringUtils.isEmpty(swaggerUiUrl))
					swaggerUiConfigParameters.setUrl(url);
				else
					swaggerUiConfigParameters.setUrl(swaggerUiUrl);
			}
			else
				swaggerUiConfigParameters.addUrl(url);
		}
		calculateOauth2RedirectUrl(uriComponentsBuilder);
	}

	@Override
	protected void calculateOauth2RedirectUrl(UriComponentsBuilder uriComponentsBuilder) {
		if (!swaggerUiConfigParameters.isValidUrl(swaggerUiConfigParameters.getOauth2RedirectUrl()))
			swaggerUiConfigParameters.setOauth2RedirectUrl(uriComponentsBuilder.path(swaggerUiConfigParameters.getUiRootPath()).path(swaggerUiConfigParameters.getOauth2RedirectUrl()).build().toString());
	}
}
