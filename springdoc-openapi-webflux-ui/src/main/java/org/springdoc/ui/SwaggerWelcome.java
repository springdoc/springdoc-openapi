package org.springdoc.ui;

import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.springdoc.core.Constants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Controller
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
        String url = webJarsPrefixUrl +
                SWAGGER_UI_URL +
                apiDocsUrl +
                DEFAULT_VALIDATOR_URL;

        final Map<String, String> params = swaggerUiConfig.getConfigParameters();


        final UriComponentsBuilder builder = params
                .entrySet()
                .stream()
                .reduce(
                        UriComponentsBuilder
                                .fromUriString(url),
                        (b, e) -> b.queryParam(e.getKey(), e.getValue()),
                        (left, right) -> left);


        return route(GET(uiPath),
                req -> ServerResponse.temporaryRedirect(URI.create(builder.build().encode().toString())).build());
    }
}
