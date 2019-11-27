package org.springdoc.ui;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.SwaggerUiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
 class SwaggerWelcome {

    @Value(API_DOCS_URL)
    private String apiDocsUrl;

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Autowired
    private SwaggerUiConfig swaggerUiConfig;

    @Operation(hidden = true)
    @GetMapping(SWAGGER_UI_PATH)
    public String redirectToUi(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String uiRootPath = "";
        if (swaggerPath.contains("/")) {
            uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf('/'));
        }
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(REDIRECT_URL_PREFIX);
        sbUrl.append(uiRootPath);
        sbUrl.append(SWAGGER_UI_URL);
        if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
            sbUrl.append(contextPath).append(apiDocsUrl);
        } else {
            sbUrl.append(contextPath).append(apiDocsUrl);
        }
        sbUrl.append(DEFAULT_VALIDATOR_URL);

        final Map<String, String> params = swaggerUiConfig.getConfigParameters();

        final UriComponentsBuilder builder = params
                .entrySet()
                .stream()
                .reduce(
                        UriComponentsBuilder
                                .fromUriString(sbUrl.toString()),
                        (b, e) -> b.queryParam(e.getKey(), e.getValue()),
                        (left, right) -> left);

        return builder.build().encode().toString();
    }
}