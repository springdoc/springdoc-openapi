package org.springdoc.ui;

import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.springdoc.core.Constants.*;
import static org.springdoc.core.SwaggerUiQueryParamsAppender.appendSwaggerUiQueryParams;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Controller
@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
public class SwaggerWelcome {

    @Value(API_DOCS_URL)
    private String apiDocsUrl;

    @Value(SWAGGER_UI_PATH)
    private String uiPath;

    @Value(WEB_JARS_PREFIX_URL)
    private String webJarsPrefixUrl;

    @Autowired
    private SwaggerUiConfigProperties swaggerUiConfig;

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    RouterFunction<ServerResponse> routerFunction() {
        String baseUrl = webJarsPrefixUrl + SWAGGER_UI_URL;

        final Map<String, String> swaggerUiParams = swaggerUiConfig.getConfigParameters();
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        final UriComponentsBuilder builder = appendSwaggerUiQueryParams(uriBuilder, swaggerUiParams, apiDocsUrl);

        return route(GET(uiPath),
                req -> ServerResponse.temporaryRedirect(URI.create(builder.build().encode().toString())).build());
    }
}
