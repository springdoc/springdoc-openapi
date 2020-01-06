package org.springdoc.ui;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(OpenAPIBuilder.class)
class SwaggerWelcome implements InitializingBean {

    @Value(API_DOCS_URL)
    private String apiDocsUrl;

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Value(MVC_SERVLET_PATH)
    private String mvcServletPath;

    @Autowired
    private SwaggerUiConfigProperties swaggerUiConfig;

    private String uiRootPath;


    @Override
    public void afterPropertiesSet() {
        calculateUiRootPath();
    }

    private void calculateUiRootPath() {
        StringBuilder sbUrl = new StringBuilder();
        if (StringUtils.isNotBlank(mvcServletPath))
            sbUrl.append(mvcServletPath);
        if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
            sbUrl.append(swaggerPath.substring(0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR)));
        this.uiRootPath=sbUrl.toString();
    }

    @Operation(hidden = true)
    @GetMapping(SWAGGER_UI_PATH)
    public String redirectToUi(HttpServletRequest request) {
        StringBuilder sbUrl = new StringBuilder(REDIRECT_URL_PREFIX).append(this.uiRootPath);
        sbUrl.append(SWAGGER_UI_URL);
        buildConfigUrl(request);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(sbUrl.toString());
        return uriBuilder.queryParam(SwaggerUiConfigProperties.CONFIG_URL_PROPERTY, swaggerUiConfig.getConfigUrl()).build().encode().toString();
    }

    @Operation(hidden = true)
    @GetMapping(value = SWAGGER_CONFIG_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> openapiYaml(HttpServletRequest request) {
        buildConfigUrl(request);
        return swaggerUiConfig.getConfigParameters();
    }

    private String buildUrl(final HttpServletRequest request, final String docsUrl) {
        String contextPath = request.getContextPath();
        if (StringUtils.isNotBlank(mvcServletPath))
            contextPath += mvcServletPath;
        if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
            return contextPath.substring(0, contextPath.length() - 1) + docsUrl;
        }
        return contextPath + docsUrl;
    }

    private void buildConfigUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(swaggerUiConfig.getConfigUrl())) {
            String url = buildUrl(request, apiDocsUrl);
            String swaggerConfigUrl = url + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
            swaggerUiConfig.setConfigUrl(swaggerConfigUrl);
            if (SwaggerUiConfigProperties.getSwaggerUrls().isEmpty())
                swaggerUiConfig.setUrl(url);
            else
                SwaggerUiConfigProperties.addUrl(url);
        }
        if (!swaggerUiConfig.isValidUrl(swaggerUiConfig.getOauth2RedirectUrl())) {
            swaggerUiConfig.setOauth2RedirectUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path(this.uiRootPath).path(swaggerUiConfig.getOauth2RedirectUrl()).build().toString());
        }
    }

}
