package org.springdoc.webmvc.api;

import org.springdoc.api.OpenApiResourceNotFoundException;

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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;
import static org.springframework.web.servlet.function.ServerResponse.status;


@Configuration(proxyBeanMethods = false)
public class MultipleOpenApiRouter {

	private static final String GROUP = "group";
	private static final String GROUP_PATH_VARIABLE = DEFAULT_PATH_SEPARATOR + "{" + GROUP + "}";

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@Value(DEFAULT_API_DOCS_URL_YAML)
	private String apiDocsUrlYaml;

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	RouterFunction<ServerResponse> multipleOpenApiRouter(MultipleOpenApiResource multipleOpenApiResource) {
		return route().GET(apiDocsUrl + GROUP_PATH_VARIABLE, RequestPredicates.accept(MediaType.APPLICATION_JSON),
				req -> ok().body(multipleOpenApiResource.openapiJson(req.servletRequest(), apiDocsUrl, req.pathVariable(GROUP))))
				.GET(apiDocsUrlYaml +  GROUP_PATH_VARIABLE, RequestPredicates.accept(MediaType.valueOf(APPLICATION_OPENAPI_YAML)),
						req -> ok().body(multipleOpenApiResource.openapiYaml(req.servletRequest(), apiDocsUrlYaml, req.pathVariable(GROUP))))
				.onError(OpenApiResourceNotFoundException.class, (throwable, serverRequest) -> status(NOT_FOUND).body(throwable.getMessage()))
				.build();
	}

}

