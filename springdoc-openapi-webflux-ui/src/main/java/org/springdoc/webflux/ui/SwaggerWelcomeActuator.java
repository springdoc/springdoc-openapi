package org.springdoc.webflux.ui;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

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
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public SwaggerWelcomeActuator(SwaggerUiConfigProperties swaggerUiConfig
			, SpringDocConfigProperties springDocConfigProperties,
			SwaggerUiConfigParameters swaggerUiConfigParameters,
			WebEndpointProperties webEndpointProperties) {
		super(swaggerUiConfig, springDocConfigProperties,swaggerUiConfigParameters);
		this.webEndpointProperties = webEndpointProperties;
	}

	/**
	 * Redirect to ui mono.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the mono
	 */
	@Operation(hidden = true)
	@GetMapping("/")
	public Mono<Void> redirectToUi(ServerHttpRequest request, ServerHttpResponse response) {
	  return super.redirectToUi(request,response);
	}


	/**
	 * Gets swagger ui config.
	 *
	 * @param request the request
	 * @return the swagger ui config
	 */
	@Operation(hidden = true)
	@GetMapping(value = SWAGGER_CONFIG_ACTUATOR_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getSwaggerUiConfig(ServerHttpRequest request) {
		return super.getSwaggerUiConfig(request);
	}

	/**
	 * Build config url.
	 *
	 * @param contextPath the context path
	 * @param uriComponentsBuilder the uri components builder
	 */
	@Override
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
	protected void calculateUiRootPath(StringBuilder... sbUrls) {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(webEndpointProperties.getBasePath());
		calculateUiRootCommon(sbUrl, sbUrls);
	}

}
