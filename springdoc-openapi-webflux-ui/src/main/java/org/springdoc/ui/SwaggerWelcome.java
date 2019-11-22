package org.springdoc.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

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

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        String url = webJarsPrefixUrl +
                SWAGGER_UI_URL +
                apiDocsUrl +
                DEFAULT_VALIDATOR_URL;

        return route(GET(uiPath),
                req -> ServerResponse.temporaryRedirect(URI.create(url)).build());
    }
}
