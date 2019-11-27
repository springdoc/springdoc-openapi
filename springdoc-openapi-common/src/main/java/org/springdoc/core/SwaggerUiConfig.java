package org.springdoc.core;


import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.TreeMap;

/**
 * Please refer to https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md
 * to get the idea what each parameter does.
 */
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class SwaggerUiConfig {
    // URL to fetch external configuration document from.
    private String configUrl;
    // The url pointing to API definition (normally
    // swagger.json/swagger.yaml/openapi.json/openapi.yaml).
    private String url;
    // If set, enables filtering. The top bar will show an edit box that
    // could be used to filter the tagged operations that are shown.
    private String filter;

    // Enables or disables deep linking for tags and operations.
    private Boolean deepLinking;
    //  Controls the display of operationId in operations list.
    private Boolean displayOperationId;
    // The default expansion depth for models (set to -1 completely hide the models).
    private Integer defaultModelsExpandDepth;
    // The default expansion depth for the model on the model-example section.
    private Integer defaultModelExpandDepth;

    // Controls how the model is shown when the API is first rendered.
    private String defaultModelRendering;
    // Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
    private Boolean displayRequestDuration;
    // Controls the default expansion setting for the operations and tags.
    private String docExpansion;
    //  If set, limits the number of tagged operations displayed to at most this many.
    private Integer maxDisplayedTags;
    // Controls the display of vendor extension (x-) fields and values.
    private Boolean showExtensions;
    // Controls the display of extensions
    private Boolean showCommonExtensions;
    // Set a different validator URL, for example for locally deployed validators
    private String validatorUrl;

    public Map<String, String> getConfigParameters() {
        final Map<String, String> params = new TreeMap<>();
        put("url", url, params);
        put("configUrl", configUrl, params);
        put("filter", filter, params);
        put("deepLinking", this.deepLinking, params);
        put("displayOperationId", displayOperationId, params);
        put("defaultModelsExpandDepth", defaultModelsExpandDepth, params);
        put("defaultModelExpandDepth", defaultModelExpandDepth, params);
        put("defaultModelRendering", defaultModelRendering, params);
        put("displayRequestDuration", displayRequestDuration, params);
        put("docExpansion", docExpansion, params);
        put("maxDisplayedTags", maxDisplayedTags, params);
        put("showExtensions", showExtensions, params);
        put("showCommonExtensions", showCommonExtensions, params);
        put("validatorUrl", validatorUrl, params);
        return params;
    }

    protected void put(final String name, final Integer value, final Map<String, String> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    protected void put(final String name, final Boolean value, final Map<String, String> params) {
        if (value != null) {
            params.put(name, value.toString());
        }
    }

    protected void put(final String name, final String value, final Map<String, String> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(name, value);
        }
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Boolean getDeepLinking() {
        return deepLinking;
    }

    public void setDeepLinking(Boolean deepLinking) {
        this.deepLinking = deepLinking;
    }

    public Boolean getDisplayOperationId() {
        return displayOperationId;
    }

    public void setDisplayOperationId(Boolean displayOperationId) {
        this.displayOperationId = displayOperationId;
    }

    public Integer getDefaultModelsExpandDepth() {
        return defaultModelsExpandDepth;
    }

    public void setDefaultModelsExpandDepth(Integer defaultModelsExpandDepth) {
        this.defaultModelsExpandDepth = defaultModelsExpandDepth;
    }

    public Integer getDefaultModelExpandDepth() {
        return defaultModelExpandDepth;
    }

    public void setDefaultModelExpandDepth(Integer defaultModelExpandDepth) {
        this.defaultModelExpandDepth = defaultModelExpandDepth;
    }

    public String getDefaultModelRendering() {
        return defaultModelRendering;
    }

    public void setDefaultModelRendering(String defaultModelRendering) {
        this.defaultModelRendering = defaultModelRendering;
    }

    public Boolean getDisplayRequestDuration() {
        return displayRequestDuration;
    }

    public void setDisplayRequestDuration(Boolean displayRequestDuration) {
        this.displayRequestDuration = displayRequestDuration;
    }

    public String getDocExpansion() {
        return docExpansion;
    }

    public void setDocExpansion(String docExpansion) {
        this.docExpansion = docExpansion;
    }

    public Integer getMaxDisplayedTags() {
        return maxDisplayedTags;
    }

    public void setMaxDisplayedTags(Integer maxDisplayedTags) {
        this.maxDisplayedTags = maxDisplayedTags;
    }

    public Boolean getShowExtensions() {
        return showExtensions;
    }

    public void setShowExtensions(Boolean showExtensions) {
        this.showExtensions = showExtensions;
    }

    public Boolean getShowCommonExtensions() {
        return showCommonExtensions;
    }

    public void setShowCommonExtensions(Boolean showCommonExtensions) {
        this.showCommonExtensions = showCommonExtensions;
    }

    public String getValidatorUrl() {
        return validatorUrl;
    }

    public void setValidatorUrl(String validatorUrl) {
        this.validatorUrl = validatorUrl;
    }
}