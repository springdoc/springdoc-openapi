package org.springdoc.webmvc.api;

import org.springdoc.core.SpringDocConfigProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springdoc.core.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Configuration(proxyBeanMethods = false)
public class OpenApiRouter {

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@Value(DEFAULT_API_DOCS_URL_YAML)
	private String apiDocsUrlYaml;

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	RouterFunction<ServerResponse> openApiRoutes(OpenApiResource openApiResource, SpringDocConfigProperties springDocConfigProperties) {
		return route()
				.GET(apiDocsUrl, RequestPredicates.accept(MediaType.APPLICATION_JSON), req -> ok().body(openApiResource.openapiJson(req.servletRequest(), apiDocsUrl)))
				.GET(apiDocsUrlYaml, RequestPredicates.accept(MediaType.valueOf(APPLICATION_OPENAPI_YAML)), req -> ok().body(openApiResource.openapiYaml(req.servletRequest(), apiDocsUrlYaml)))
				.build();
	}

}
