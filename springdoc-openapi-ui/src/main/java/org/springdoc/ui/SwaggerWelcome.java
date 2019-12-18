package org.springdoc.ui;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springdoc.core.Constants.*;
import static org.springdoc.core.SwaggerUiQueryParamsAppender.appendSwaggerUiQueryParams;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
class SwaggerWelcome {

    @Value(API_DOCS_URL)
    private String apiDocsUrl;

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Value(MVC_SERVLET_PATH)
    private String mvcServletPath;

    @Autowired
    private SwaggerUiConfigProperties swaggerUiConfig;

    @Operation(hidden = true)
    @GetMapping(SWAGGER_UI_PATH)
    public String redirectToUi(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        if (StringUtils.isNotBlank(mvcServletPath))
            contextPath += mvcServletPath;
        String uiRootPath = "";
        if (swaggerPath.contains("/")) {
            uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf('/'));
        }
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(REDIRECT_URL_PREFIX);
        if (StringUtils.isNotBlank(mvcServletPath))
            sbUrl.append(mvcServletPath);
        sbUrl.append(uiRootPath);
        sbUrl.append(SWAGGER_UI_URL);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(sbUrl.toString());
        Map<String, String> swaggerUiParams = swaggerUiConfig.getConfigParameters();
        String url = buildUrl(contextPath, apiDocsUrl);

        return appendSwaggerUiQueryParams(uriBuilder, swaggerUiParams, url)
                .build().encode().toString();
    }

    private String buildUrl(final String contextPath, final String docsUrl) {
        if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
            return contextPath.substring(0, contextPath.length() - 1) + docsUrl;
        }
        return contextPath + docsUrl;
    }
}