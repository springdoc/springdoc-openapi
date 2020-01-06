package org.springdoc.ui;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Controller
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerWelcome {

    @Autowired
    public SwaggerUiConfigProperties swaggerUiConfig;
    @Value(API_DOCS_URL)
    private String apiDocsUrl;
    @Value(SWAGGER_UI_PATH)
    private String uiPath;
    @Value(WEB_JARS_PREFIX_URL)
    private String webJarsPrefixUrl;

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    RouterFunction<ServerResponse> routerFunction() {
        String baseUrl = webJarsPrefixUrl + SWAGGER_UI_URL;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        buildConfigUrl();
        uriBuilder.queryParam(SwaggerUiConfigProperties.CONFIG_URL_PROPERTY, swaggerUiConfig.getConfigUrl());
        return route(GET(uiPath),
                req -> ServerResponse.temporaryRedirect(URI.create(uriBuilder.build().encode().toString())).build());
    }

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    RouterFunction<ServerResponse> getSwaggerUiConfig() {
        buildConfigUrl();
        return RouterFunctions.route(GET(swaggerUiConfig.getConfigUrl()).and(accept(MediaType.APPLICATION_JSON)), req -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(swaggerUiConfig.getConfigParameters()));
    }

    private void buildConfigUrl() {
        if (StringUtils.isEmpty(swaggerUiConfig.getConfigUrl())) {
            String swaggerConfigUrl = apiDocsUrl + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
            swaggerUiConfig.setConfigUrl(swaggerConfigUrl);

            if (SwaggerUiConfigProperties.getSwaggerUrls().isEmpty())
                swaggerUiConfig.setUrl(apiDocsUrl);
            else
                SwaggerUiConfigProperties.addUrl(apiDocsUrl);

        }
    }

}
